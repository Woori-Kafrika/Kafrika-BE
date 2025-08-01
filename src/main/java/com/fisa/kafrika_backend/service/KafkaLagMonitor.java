package com.fisa.kafrika_backend.service;

import static com.fisa.kafrika_backend.service.ChatKafkaListener.KAFKA_CONSUMER_GROUP_ID;
import static com.fisa.kafrika_backend.service.ChatKafkaListener.KAFKA_TOPIC;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaLagMonitor {

    private final SimpMessagingTemplate messagingTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private AdminClient adminClient;
    private KafkaConsumer<String, String> consumer;

    private static final long AVERAGE_PROCESS_TIME_MS = 2000;

    @PostConstruct
    public void init() {
        Properties adminProps = new Properties();
        adminProps.put("bootstrap.servers", bootstrapServers);
        adminClient = AdminClient.create(adminProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "lag-checker");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(consumerProps);
    }

    @Scheduled(fixedRate = 2000)
    public void monitorLag() {
        try {
            List<TopicPartition> partitions = getPartitions();
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);

            ListConsumerGroupOffsetsResult offsetsResult = adminClient.listConsumerGroupOffsets(KAFKA_CONSUMER_GROUP_ID);
            Map<TopicPartition, OffsetAndMetadata> currentOffsets = offsetsResult.partitionsToOffsetAndMetadata().get();

            long totalLag = 0L;
            for (TopicPartition tp : partitions) {
                long latest = endOffsets.getOrDefault(tp, 0L);
                long committed = currentOffsets.getOrDefault(tp, new OffsetAndMetadata(0L)).offset();
                totalLag += (latest - committed);
            }

            long estimatedDelaySec = (totalLag * AVERAGE_PROCESS_TIME_MS) / 1000;

            Map<String, Object> result = Map.of(
                    "waitingCount", totalLag,
                    "estimatedDelaySec", estimatedDelaySec
            );
            messagingTemplate.convertAndSend("/topic/chat/status", result);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to monitor Kafka lag", e);
        }
    }

    private List<TopicPartition> getPartitions() {
        List<TopicPartition> partitions = new ArrayList<>();
        consumer.partitionsFor(KAFKA_TOPIC).forEach(info ->
                partitions.add(new TopicPartition(KAFKA_TOPIC, info.partition()))
        );
        return partitions;
    }
}

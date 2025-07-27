package com.fisa.kafrika_backend.common.util;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class BindingResultUtils {

    public static String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(". ")) + ".";
    }

}

package com.daniyal.dto;

public record DecryptRequest(
        String encrypted,
        String key
) {
}

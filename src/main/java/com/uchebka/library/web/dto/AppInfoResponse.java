package com.uchebka.library.web.dto;

public record AppInfoResponse(
        String applicationName,
        String greeting,
        String formalGreeting,
        String serverTime,
        String hint
) {
}

package com.skynill.timetracker.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record TimeDto(Long id,
                      @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") Instant timestamp) {
}

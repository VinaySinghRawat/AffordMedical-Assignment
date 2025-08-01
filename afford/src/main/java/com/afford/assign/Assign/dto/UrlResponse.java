package com.afford.assign.Assign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlResponse {
    private String shortLink;
    private LocalDateTime expiry;
}

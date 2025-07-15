package com.afford.assign.Assign.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlMapping {
    @Id
    private String shortcode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private int clickCount;
}

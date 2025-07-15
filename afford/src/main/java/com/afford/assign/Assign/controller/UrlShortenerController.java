package com.afford.assign.Assign.controller;

import com.afford.assign.Assign.dto.UrlRequest;
import com.afford.assign.Assign.dto.UrlResponse;
import com.afford.assign.Assign.entity.UrlMapping;
import com.afford.assign.Assign.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    private final UrlService urlService;

    @PostMapping("/shorturls")
    public ResponseEntity<UrlResponse> create(@RequestBody UrlRequest request) {
        logger.info("Received request to shorten URL: {}", request.getUrl());
        return new ResponseEntity<>(urlService.createShortUrl(request), HttpStatus.CREATED);
    }

    @GetMapping("/shorturls/:{shortcode}")
    public ResponseEntity<UrlMapping> getStats(@PathVariable String shortcode) {
        logger.info("Fetching stats for shortcode: {}", shortcode);
        return ResponseEntity.ok(urlService.getUrl(shortcode));
    }
    @GetMapping("/{shortcode}")
    public ResponseEntity<Map<String, Object>> redirect(@PathVariable String shortcode, HttpServletRequest request) {
        logger.info("Redirect request received for shortcode: {}", shortcode);

        UrlMapping url = urlService.getUrl(shortcode);

        if (url.getExpiryAt().isBefore(LocalDateTime.now())) {
            logger.warn("Shortcode {} has expired", shortcode);
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(Map.of("error", "This URL has expired."));
        }

        urlService.trackClick(shortcode, request);

        Map<String, Object> response = new HashMap<>();
        response.put("originalUrl", url.getOriginalUrl());
        response.put("clickCount", url.getClickCount());
        response.put("timestamp", LocalDateTime.now());
        response.put("ipAddress", request.getRemoteAddr());
        response.put("userAgent", request.getHeader("User-Agent"));

        return ResponseEntity.ok(response);
    }

    
}
package com.afford.assign.Assign.service;

import com.afford.assign.Assign.dto.UrlRequest;
import com.afford.assign.Assign.dto.UrlResponse;
import com.afford.assign.Assign.entity.UrlMapping;

import jakarta.servlet.http.HttpServletRequest;

public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    UrlMapping getUrl(String shortcode);
    void trackClick(String shortcode, HttpServletRequest request);
}

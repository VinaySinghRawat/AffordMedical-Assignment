package com.afford.assign.Assign.service;

import com.afford.assign.Assign.dto.UrlRequest;
import com.afford.assign.Assign.dto.UrlResponse;
import com.afford.assign.Assign.entity.ClickEvent;
import com.afford.assign.Assign.entity.UrlMapping;
import com.afford.assign.Assign.repository.ClickEventRepository;
import com.afford.assign.Assign.repository.UrlRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    private final ClickEventRepository clickRepo;
  
private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Override
    public UrlResponse createShortUrl(UrlRequest request) {
        logger.debug("Creating short URL for: {}", request.getUrl());
        String code = request.getShortcode() != null ? request.getShortcode() : UUID.randomUUID().toString().substring(0, 6);
        if (urlRepository.existsById(code)) {
            throw new RuntimeException("Shortcode already in use.");
        }
        int validity = request.getValidity() != null ? request.getValidity() : 30;
        UrlMapping mapping = new UrlMapping(
                code,
                request.getUrl(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(validity),
                0
        );
        urlRepository.save(mapping);
        return new UrlResponse("http://localhost:8080/" + code, mapping.getExpiryAt());
    }

    @Override
    public UrlMapping getUrl(String shortcode) {
        logger.debug("Retrieving URL mapping for shortcode: {}", shortcode);
        return urlRepository.findById(shortcode)
                .orElseThrow(() -> new RuntimeException("Shortcode not found"));
    }

    @Override
    public void trackClick(String shortcode, HttpServletRequest request) {
        UrlMapping mapping = getUrl(shortcode);
        mapping.setClickCount(mapping.getClickCount() + 1);
        urlRepository.save(mapping);

        String ip = request.getRemoteAddr();
        String agent = request.getHeader("User-Agent");

        ClickEvent click = new ClickEvent();
        click.setShortcode(shortcode);
        click.setIpAddress(ip);
        click.setUserAgent(agent);
        click.setClickedAt(LocalDateTime.now());

        clickRepo.save(click);
    }
}
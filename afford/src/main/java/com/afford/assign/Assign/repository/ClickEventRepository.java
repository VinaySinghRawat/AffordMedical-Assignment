package com.afford.assign.Assign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afford.assign.Assign.entity.ClickEvent;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByShortcode(String shortcode);
}
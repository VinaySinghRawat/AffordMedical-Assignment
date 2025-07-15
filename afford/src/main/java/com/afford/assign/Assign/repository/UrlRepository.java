package com.afford.assign.Assign.repository;

import com.afford.assign.Assign.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlMapping, String> {
}

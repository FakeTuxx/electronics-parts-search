package com.example.electronics.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {

    List<Part> findByPartNumberContainingIgnoreCase(String partNumber);

    List<Part> findByNameContainingIgnoreCase(String name);

    List<Part> findByTypeContainingIgnoreCase(String type);
}
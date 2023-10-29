package com.hackerrank.api.repository;

import com.hackerrank.api.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findByOrigin(String origin);
}

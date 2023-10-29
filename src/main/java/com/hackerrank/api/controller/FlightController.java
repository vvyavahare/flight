package com.hackerrank.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.api.model.Flight;
import com.hackerrank.api.repository.FlightRepository;

@RestController
public class FlightController {

  @Autowired
  private FlightRepository repository;

  @PostMapping("/flight")
  public ResponseEntity<Flight> createFlight(@RequestBody Flight flight){
    repository.save(flight);
    return null;
  }

  @GetMapping("/flight")
  public ResponseEntity<List<Flight>> getAllFlights(){
    return ResponseEntity.ok().body(repository.findAll());
  }

  @GetMapping("/flight")
  public ResponseEntity<Flight> findFlightById(Integer id){
    return ResponseEntity.ok().body(repository.findById(id).get());
  }
}

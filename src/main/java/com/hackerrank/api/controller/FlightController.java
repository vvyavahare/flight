package com.hackerrank.api.controller;

import java.util.List;
import java.util.Optional;

import com.hackerrank.api.model.FlightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hackerrank.api.model.Flight;
import com.hackerrank.api.repository.FlightRepository;

@RestController
public class FlightController {

    @Autowired
    private FlightRepository repository;

    @PostMapping("/flight")
    public ResponseEntity<FlightModel> createFlight(@RequestBody Flight flight) {
        Flight flightCreated = repository.save(flight);
        FlightModel model = new FlightModel();
        model.setId(flightCreated.getId().intValue());
        model.setDestination(flight.getDestination());
        model.setFlight(flight.getFlight());
        model.setOrigin(flightCreated.getOrigin());
        model.setSpeedSeries(flightCreated.getSpeedSeries());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getAllFlights(@RequestParam Optional<String> origin, @RequestParam Optional<String> orderBy) {
        if (origin.isPresent()) {
            return ResponseEntity.ok().body(repository.findByOrigin(origin.get()));
        }
        if (orderBy.isPresent()) {
            String val = orderBy.get();
            if ("destination".equalsIgnoreCase(val)) {
                return ResponseEntity.ok().body(repository.findAll(Sort.by("destination")));
            } else if ("-destination".equalsIgnoreCase(val)) {
                return ResponseEntity.ok().body(repository.findAll(Sort.by(Sort.Direction.DESC, "destination")));
            }
        }
        return ResponseEntity.ok().body(repository.findAll());
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<Flight> findFlightById(@PathVariable Integer id) {
        Optional<Flight> flightOptional = repository.findById(id);
        if (flightOptional.isPresent()) {
            return ResponseEntity.ok().body(repository.findById(id).get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

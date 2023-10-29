package com.hackerrank.api.controller;

import java.util.List;
import java.util.Optional;

import com.hackerrank.api.exception.ResponseNotFoundException;
import com.hackerrank.api.mapper.FlightMapper;
import com.hackerrank.api.model.FlightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hackerrank.api.model.Flight;
import com.hackerrank.api.repository.FlightRepository;

import static com.hackerrank.api.constants.FlightsConstants.*;

@RestController
public class FlightController {


    @Autowired
    private FlightRepository repository;

    @PostMapping("/flight")
    public ResponseEntity<FlightModel> createFlight(@RequestBody Flight flight) {
        Flight flightCreated = repository.save(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(FlightMapper.mapToDto(flightCreated, new FlightModel()));
    }

    @GetMapping("/flight")
    public ResponseEntity<List<Flight>> getAllFlights(@RequestParam Optional<String> origin, @RequestParam Optional<String> orderBy) {
        if (origin.isPresent()) {
            return ResponseEntity.ok().body(repository.findByOrigin(origin.get()));
        }
        if (orderBy.isPresent()) {
            Optional<ResponseEntity<List<Flight>>> response = getOrderedListResponse(orderBy);
            if (response.isPresent()) {
                return response.get();
            } else {
                throw new ResponseNotFoundException(FLIGHT_RESOURCE, ID_RESOURCE, ORDERBY_RESOURCE);
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
            throw new ResponseNotFoundException(FLIGHT_RESOURCE, ID_RESOURCE, id.toString());
        }
    }

    private Optional<ResponseEntity<List<Flight>>> getOrderedListResponse(Optional<String> orderBy) {
        String val = orderBy.get();
        if (DESTINATION.equalsIgnoreCase(val)) {
            return Optional.of(ResponseEntity.ok().body(repository.findAll(Sort.by("destination"))));
        } else if (REVERSE_DESTINATION.equalsIgnoreCase(val)) {
            return Optional.of(ResponseEntity.ok().body(repository.findAll(Sort.by(Sort.Direction.DESC, "destination"))));
        }
        return Optional.empty();
    }
}

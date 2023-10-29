package com.hackerrank.api.mapper;

import com.hackerrank.api.model.Flight;
import com.hackerrank.api.model.FlightModel;

public class FlightMapper {
    public static FlightModel mapToDto(Flight flight, FlightModel model) {
        model.setId(flight.getId().intValue());
        model.setDestination(flight.getDestination());
        model.setFlight(flight.getFlight());
        model.setOrigin(flight.getOrigin());
        model.setSpeedSeries(flight.getSpeedSeries());
        return model;
    }
}

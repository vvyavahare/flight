package com.hackerrank.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightModel {
    protected Integer id;
    protected String flight;
    protected String origin;
    protected String destination;
    protected List<Integer> speedSeries;

    public FlightModel(String flight, String origin, String destination, List<Integer> speedSeries) {
        this.flight = flight;
        this.origin = origin;
        this.destination = destination;
        this.speedSeries = speedSeries;
    }
}
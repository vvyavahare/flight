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
}
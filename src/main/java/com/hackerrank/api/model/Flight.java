package com.hackerrank.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Flight {
    @Id
    private Long id;
}

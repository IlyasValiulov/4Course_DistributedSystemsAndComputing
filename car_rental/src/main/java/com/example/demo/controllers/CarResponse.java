package com.example.demo.controllers;

import com.example.demo.entities.CarStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private Integer productionYear;
    private CarStatus status;
}

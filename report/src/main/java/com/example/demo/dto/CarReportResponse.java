package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarReportResponse {

    private long inSalon;

    private long inRent;

    private long writtenOff;
}

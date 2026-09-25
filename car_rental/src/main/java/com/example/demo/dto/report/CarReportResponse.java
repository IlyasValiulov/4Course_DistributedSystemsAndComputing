package com.example.demo.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarReportResponse {

    private long inSalon;

    private long inRent;

    private long writtenOff;
}
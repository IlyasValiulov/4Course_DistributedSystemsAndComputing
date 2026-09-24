package com.example.demo.mappers;

import com.example.demo.dto.car.CarRequest;
import com.example.demo.dto.car.CarResponse;
import com.example.demo.entities.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarEntity toEntity(CarRequest dto) {
        CarEntity car = new CarEntity();
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setProductionYear(dto.getProductionYear());
        return car;
    }

    public CarResponse toDto(CarEntity entity) {
        return new CarResponse(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                entity.getProductionYear(),
                entity.getStatus()
        );
    }

    public void updateEntity(CarEntity entity, CarRequest dto) {
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setProductionYear(dto.getProductionYear());
    }
}

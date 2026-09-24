package com.example.demo.services;

import com.example.demo.dto.car.CarRequest;
import com.example.demo.dto.car.CarResponse;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.CarStatus;
import com.example.demo.mappers.CarMapper;
import com.example.demo.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    private CarEntity findEntity(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобиль с id " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getAll() {
        return carRepository.findAll().stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CarResponse getById(Long id) {
        return carMapper.toDto(findEntity(id));
    }

    public CarResponse create(CarRequest dto) {
        CarEntity car = carMapper.toEntity(dto);
        if (car.getStatus() == CarStatus.WRITTEN_OFF) {
            throw new RuntimeException("Нельзя редактировать списанный автомобиль");
        }
        car.setStatus(CarStatus.SALON);
        return carMapper.toDto(carRepository.save(car));
    }

    public CarResponse update(Long id, CarRequest dto) {
        CarEntity car = findEntity(id);
        carMapper.updateEntity(car, dto);
        return carMapper.toDto(carRepository.save(car));
    }

    public void delete(Long id) {
        CarEntity car = findEntity(id);
        if (car.getStatus() == CarStatus.SALON) {
            throw new RuntimeException("Нельзя удалить автомобиль из салона");
        }
        if (car.getStatus() == CarStatus.RENTED) {
            throw new RuntimeException("Нельзя удалить автомобиль в аренде");
        }
        carRepository.delete(car);
    }

    @Transactional(readOnly = true)
    public CarResponse getDeletedById(Long id) {
        return carRepository.findDeletedById(id)
                .map(carMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Удалённый автомобиль с id " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getAllDeleted() {
        return carRepository.findAllDeleted().stream()
                .map(carMapper::toDto)
                .toList();
    }

    public CarResponse forRent(Long id) {
        CarEntity car = findEntity(id);
        if (car.getStatus() != CarStatus.SALON) {
            throw new RuntimeException("Выдать можно только автомобиль из салона");
        }
        car.setStatus(CarStatus.RENTED);
        return carMapper.toDto(carRepository.save(car));
    }

    public CarResponse fromRent(Long id) {
        CarEntity car = findEntity(id);
        if (car.getStatus() != CarStatus.RENTED) {
            throw new RuntimeException("Забрать можно только автомобиль из проката");
        }
        car.setStatus(CarStatus.SALON);
        return carMapper.toDto(carRepository.save(car));
    }

    public CarResponse writeOff(Long id) {
        CarEntity car = findEntity(id);
        if (car.getStatus() == CarStatus.RENTED) {
            throw new RuntimeException("Нельзя списать автомобиль в аренде");
        }
        if (car.getStatus() == CarStatus.WRITTEN_OFF) {
            throw new RuntimeException("Автомобиль уже списан");
        }
        car.setStatus(CarStatus.WRITTEN_OFF);
        return carMapper.toDto(carRepository.save(car));
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getAllByStatus(CarStatus status) {
        return carRepository.findByStatus(status).stream()
                .map(carMapper::toDto)
                .toList();
    }
}

package com.multithreading.demo.service;

import com.multithreading.demo.entity.Car;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CarService {
    List<Car> getAllCars();
    CompletableFuture<List<Car>> getAllCarsAsync();
}

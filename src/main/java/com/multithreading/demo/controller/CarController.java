package com.multithreading.demo.controller;

import com.multithreading.demo.entity.Car;
import com.multithreading.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CarController {
    @Autowired
    CarService carService;
    @GetMapping("/flux/cars")
    public List<Car> getCars(){
        return carService.getAllCars();
    }
    @GetMapping("/async/cars")
    public List<Car> getCarsAsync() throws ExecutionException, InterruptedException {
        return carService.getAllCarsAsync().get();
    }
    @GetMapping("/async2/cars")
    public ResponseEntity getCarsAsync2(){
        CompletableFuture<List<Car>> cars1 = carService.getAllCarsAsync();
        CompletableFuture<List<Car>> cars2 = carService.getAllCarsAsync();
        CompletableFuture<List<Car>> cars3 = carService.getAllCarsAsync();
        CompletableFuture.allOf(cars1,cars2,cars3).join();
        return ResponseEntity.ok().build();
    }
}

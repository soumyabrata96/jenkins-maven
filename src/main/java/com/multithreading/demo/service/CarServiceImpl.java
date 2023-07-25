package com.multithreading.demo.service;

import com.multithreading.demo.entity.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    WebClient webClient;
    private Logger logger= LoggerFactory.getLogger(CarServiceImpl.class);
    @Override
    public List<Car> getAllCars() {
        return getCarsAsFlux().collectList().block();
    }
    @Async
    @Override
    public CompletableFuture<List<Car>> getAllCarsAsync() {
        logger.info("Current Thread : "+Thread.currentThread().getName());
        return getCarsAsFlux().collectList().subscribeOn(Schedulers.boundedElastic()).toFuture();
    }
    private Flux<Car> getCarsAsFlux(){
        return webClient.get().uri("/v1/cars/list").retrieve().bodyToFlux(Car.class);
    }
}

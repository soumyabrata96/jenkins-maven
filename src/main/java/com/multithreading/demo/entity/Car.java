package com.multithreading.demo.entity;

import jakarta.persistence.*;

import java.io.Serializable;
@Entity
@Table(name = "cars")
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String manufacturer;
    private String model;
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

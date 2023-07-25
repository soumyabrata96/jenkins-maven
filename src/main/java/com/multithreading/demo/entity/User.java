package com.multithreading.demo.entity;

import com.multithreading.demo.utilities.Genders;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Entity
@Table(name = "users")
public class User extends RepresentationModel<User> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Genders gender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Genders getGender() {
        return gender;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}

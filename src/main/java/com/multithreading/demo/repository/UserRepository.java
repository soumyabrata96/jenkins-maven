package com.multithreading.demo.repository;

import com.google.common.base.Stopwatch;
import com.multithreading.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.util.concurrent.TimeUnit.SECONDS;

public interface UserRepository extends JpaRepository<User,Long> {
    default void wasteTime() {
        Stopwatch watch = Stopwatch.createStarted();
        // delay for 2 seconds
        while (watch.elapsed(SECONDS) < 5) {
            int i = Integer.MIN_VALUE;
            while (i < Integer.MAX_VALUE) {
                i++;
            }
        }
    }
}

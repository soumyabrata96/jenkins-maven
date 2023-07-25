package com.multithreading.demo.service;

import com.multithreading.demo.entity.User;
import com.multithreading.demo.utilities.Genders;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    void saveAllUsers(MultipartFile file);
    CompletableFuture<Page<User>> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    CompletableFuture<User> getUserById(long id);

    @Async
    CompletableFuture<Map<Genders,List<User>>> getUsersByGenderType();
}

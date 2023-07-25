package com.multithreading.demo.controller;

import com.multithreading.demo.entity.User;
import com.multithreading.demo.service.UserService;
import com.multithreading.demo.utilities.AppConstants;
import com.multithreading.demo.utilities.Genders;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @PostMapping(value = "/users/save",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> saveUsers(@RequestParam("files") MultipartFile[] files){
        for(MultipartFile file:files){
            userService.saveAllUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(value = "/v1/users/list",produces = {MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity<Page<User>>> getUsers1(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return userService.getAllUsers(pageNo,pageSize,sortBy,sortDir).thenApply(ResponseEntity::ok);
    }
   /* @GetMapping(value = "/v2/users/list",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getUsers2(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        try {
            CompletableFuture<List<User>> users1 = userService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
            CompletableFuture<List<User>> users2 = userService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
            CompletableFuture<List<User>> users3 = userService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
            CompletableFuture.allOf(users1, users2, users3).join();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    @TimeLimiter(name = "test-timeout",fallbackMethod = "fallbackMethod")
    @GetMapping("/users/{id}")
    public CompletableFuture<ResponseEntity<User>> getUserById(@PathVariable long id) throws ExecutionException, InterruptedException {
        return userService.getUserById(id).thenApply(ResponseEntity::ok);
    }
    public CompletableFuture<ResponseEntity<Object>> fallbackMethod(long id,Exception e){
        logger.info("Exception : "+e.getMessage());
        return CompletableFuture.completedFuture(new ResponseEntity<>("Time-Out",HttpStatus.SERVICE_UNAVAILABLE));
    }

    @GetMapping(value = "/v3/users/list",produces = {MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<User> getUsers3(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) throws ExecutionException, InterruptedException {
        List<User> users=userService.getAllUsers(pageNo,pageSize,sortBy,sortDir).get().getContent();
        for(User user:users){
            user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        }
        CollectionModel<User> collectionModel=CollectionModel.of(users);
        collectionModel.add(linkTo(methodOn(UserController.class).getUsers3(pageNo,pageSize,sortBy,sortDir)).withSelfRel());
        return collectionModel;
    }

    @GetMapping("/usersByGender")
    public ResponseEntity<Map<Genders,List<User>>> getUsersByGenderType() throws ExecutionException, InterruptedException {
        Map<Genders,List<User>> getUsersByGenderType=userService.getUsersByGenderType().get();
        getUsersByGenderType.forEach((key, value) -> value
                .forEach(user -> {
                    try {
                        user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
        return new ResponseEntity<>(getUsersByGenderType,HttpStatus.OK);
    }
}

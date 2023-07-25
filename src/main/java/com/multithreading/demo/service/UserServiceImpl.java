package com.multithreading.demo.service;

import com.multithreading.demo.entity.User;
import com.multithreading.demo.repository.UserRepository;
import com.multithreading.demo.utilities.Genders;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    private final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     *
     * @param file
     */
    @Async
    @Override
    public void saveAllUsers(MultipartFile file) {
        logger.info("Current Thread : "+Thread.currentThread().getName());
        long start=System.currentTimeMillis();
        List<User> users=parseCsvFile(file);
        users=userRepository.saveAll(users);
        logger.info("Elapsed Time : "+(System.currentTimeMillis()-start));
        CompletableFuture.completedFuture(users);
    }

    /**
     *
     * @param file
     * @return the list of users parsed from csv file
     */
    private List<User> parseCsvFile(MultipartFile file) {
        List<User> users=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line;
            while((line=br.readLine())!=null){
                String[] data=line.split(",");
                User user=new User();
                user.setFirstName(data[1]);
                user.setLastName(data[2]);
                user.setEmail(data[3]);
                user.setGender(Genders.valueOfGenderType(data[4]));
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Page<User>> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir){
        logger.info("Current Thread : "+Thread.currentThread().getName());
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);
        Page<User> users=userRepository.findAll(pageable);
        return CompletableFuture.completedFuture(users);
    }

    @Async
    @Override
    public CompletableFuture<User> getUserById(long id) {
        logger.info("Current Thread : "+Thread.currentThread().getName());
        userRepository.wasteTime();
        return CompletableFuture.completedFuture(userRepository.findById(id).orElseThrow());
    }

    @Async
    @Override
    public CompletableFuture<Map<Genders,List<User>>> getUsersByGenderType(){
        Map<Genders,List<User>> usersByGender= userRepository.findAll().stream().collect(Collectors.groupingBy(User::getGender));
        return CompletableFuture.completedFuture(usersByGender);
    }
}

package com.example.auth.services;

import com.example.auth.dto.SortedUserResponse;
import com.example.auth.dto.SortedUsersResponse;
import com.example.auth.entities.User;
import com.example.auth.repositories.UserRepository;
import com.example.mergesort.ParallelMergeSorter;
import com.example.mergesort.ParallelSorter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class SortedUsersService {

    private static final int MIN_THREADS = 1;
    private static final int MAX_THREADS = 32;

    private final UserRepository userRepository;
    private final ParallelSorter<User> parallelSorter;

    public SortedUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.parallelSorter = new ParallelMergeSorter<>();
    }

    public SortedUsersResponse getSortedUsers(int threadCount) {
        if (threadCount < MIN_THREADS || threadCount > MAX_THREADS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Thread count must be between 1 and 32"
            );
        }

        List<User> users = userRepository.findAll();
        long startTime = System.nanoTime();
        List<User> sortedUsers = parallelSorter.sort(
                users,
                Comparator.comparing(User::getId),
                threadCount
        );
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        List<SortedUserResponse> result = sortedUsers.stream()
                .map(user -> new SortedUserResponse(
                        user.getId(),
                        user.getUsername()
                ))
                .toList();
                
        return new SortedUsersResponse(threadCount, executionTimeMs, result);
    }
}

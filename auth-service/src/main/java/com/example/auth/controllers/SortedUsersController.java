package com.example.auth.controllers;

import com.example.auth.dto.SortedUsersResponse;
import com.example.auth.services.SortedUsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/users")
public class SortedUsersController {

    private final SortedUsersService sortedUsersService;

    public SortedUsersController(SortedUsersService sortedUsersService) {
        this.sortedUsersService = sortedUsersService;
    }

    @GetMapping("/sorted")
    public SortedUsersResponse getSortedUsers(@RequestParam(defaultValue = "5") int threads) {
        return sortedUsersService.getSortedUsers(threads);
    }
}

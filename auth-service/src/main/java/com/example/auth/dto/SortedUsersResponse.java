package com.example.auth.dto;

import java.util.List;

public record SortedUsersResponse(
        int threadCount,
        double executionTimeMs,
        List<SortedUserResponse> users
) {
}

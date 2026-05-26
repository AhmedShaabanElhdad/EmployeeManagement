package com.example.employeeservice.dtos;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        int totalPage,
        int currentPage,
        long totalItems,
        boolean hasNext,
        boolean hasPrevious,
        String nextPageUrl,
        String previousPageUrl
) {
}

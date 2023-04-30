package ru.practicum.ewm;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationHelper {
    private PaginationHelper() {
    }

    public static Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}

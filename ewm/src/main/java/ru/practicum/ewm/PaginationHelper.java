package ru.practicum.ewm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaginationHelper {
    public static Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}

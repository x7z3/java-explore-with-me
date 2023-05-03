package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentFullDto {
    private Long id;
    private String text;
    private UserDto author;
    private Long eventId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Setter
    @Getter
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
    }
}

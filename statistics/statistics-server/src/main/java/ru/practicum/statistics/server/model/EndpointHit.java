package ru.practicum.statistics.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits", schema = "public")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app", length = 50)
    private String app;

    @Column(name = "uri", length = 200)
    private String uri;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;
}

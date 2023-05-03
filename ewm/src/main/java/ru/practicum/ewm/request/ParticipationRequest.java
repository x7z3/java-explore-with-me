package ru.practicum.ewm.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.enums.ParticipationRequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime created;

    @Column(nullable = false, unique = true)
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status", length = 32)
    private ParticipationRequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    public static ParticipationRequest of(Long userId, Long eventId) {
        ParticipationRequest request = new ParticipationRequest();
        request.setRequesterId(userId);
        Event event = new Event();
        event.setId(eventId);
        request.setEvent(event);
        request.setStatus(ParticipationRequestStatus.PENDING);
        return request;
    }
}

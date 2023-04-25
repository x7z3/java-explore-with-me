package ru.practicum.statistics.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.statistics.dto.EndpointHitDto;
import ru.practicum.statistics.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsRestClient extends AbstractRestClient {
    private final String application;
    private final String statsServiceUri;

    @Autowired
    public StatisticsRestClient(@Value("${spring.application.name}") String application,
                       @Value("${services.stats-server.uri:http://stats-server:9090}") String statsServiceUri,
                       ObjectMapper json,
                       RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(statsServiceUri))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.application = application;
        this.statsServiceUri = statsServiceUri;
    }

    public void hit(HttpServletRequest userRequest) {
        EndpointHitDto hit = EndpointHitDto.builder()
                .app(application)
                .ip(userRequest.getRemoteAddr())
                .uri(userRequest.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        post("/hit", hit);
    }

    public List<ViewStatsDto> getStats(List<String> uris) {
        if (uris == null || uris.isEmpty()) return new ArrayList<>();
        log.info("URIS:");
        log.info(uris.toString());
        String baseUrl = statsServiceUri + "/stats";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("uris", StringUtils.join(uris, ','))
                .queryParam("start", "2000-01-01 00:00:00")
                .queryParam("end", LocalDateTime.now().format(formatter)
                ).build();
        ViewStatsDto[] stats = rest.getForObject(uri.toString(), ViewStatsDto[].class);
        return Arrays.stream(stats).collect(Collectors.toUnmodifiableList());
    }
}

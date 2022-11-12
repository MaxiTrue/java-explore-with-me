package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.event.dto.EndpointHit;
import ru.practicum.explorewithme.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class StatClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatClient(@Value("${stat-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public Long getView(Event event, boolean unique) {

        String rangeStart = event.getPublishedOn().format(FORMATTER);
        String rangeEnd = LocalDateTime.now().format(FORMATTER);

        ResponseEntity<Object> responseEntity = getStats(rangeStart, rangeEnd, List.of("events/" + event.getId()), unique);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<LinkedHashMap<String, Object>> stats = (List<LinkedHashMap<String, Object>>) responseEntity.getBody();
            if (stats != null && stats.size() > 0) {
                Integer views = (Integer) stats.get(0).get("hits");
                return (long) views;
            }
        }

        return 0L;
    }

    public Map<Long, Long> getViews(List<Event> events, boolean unique) {

        if (events.size() == 0) return new HashMap<>();

        List<String> uriList = new ArrayList<>();
        Map<String, Long> urisMap = new HashMap<>();

        for (Event event : events) {
            String uri = "events/" + event.getId();
            uriList.add(uri);
            urisMap.put(uri, event.getId());
        }

        LocalDateTime rangeStart = events.stream().min(Comparator.comparing(Event::getCreatedOn)).get().getCreatedOn();
        LocalDateTime rangeEnd = LocalDateTime.now();

        ResponseEntity<Object> responseEntity = getStats(
                rangeStart.format(FORMATTER),
                rangeEnd.format(FORMATTER),
                uriList,
                unique);

        Map<Long, Long> viewsMap = new HashMap<>();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            List<LinkedHashMap<String, Object>> stats = (List<LinkedHashMap<String, Object>>) responseEntity.getBody();
            if (stats != null) {
                for (LinkedHashMap<String, Object> str : stats) {
                    Integer views = (Integer) str.get("hits");
                    viewsMap.put(urisMap.get(str.get("uri")), views.longValue());
                }
            }

        }

        return viewsMap;
    }


    public ResponseEntity<Object> saveHit(EndpointHit hit) {
        return saveHits(List.of(hit));
    }

    public ResponseEntity<Object> saveHits(List<EndpointHit> endpointHitList) {
        return post("/hit", endpointHitList);
    }

    private ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        StringBuilder strUris = new StringBuilder();
        int count = 1;
        for (String uri : uris) {
            if (count == uris.size()) {
                strUris.append(uri);
            } else {
                strUris.append(uri).append(",");
            }
        }

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", strUris,
                "unique", unique);
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        return get(path, parameters);
    }


}

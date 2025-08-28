package com.server.territoryapi.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DynmapProxyController {

    private final RestTemplate restTemplate;
    private final String DYNMAP_BASE_URL = "http://localhost:8123";

    public DynmapProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Проксируем ВСЁ: API, CSS, JS, изображения
     */
    @GetMapping("/dynmap/**")
    public ResponseEntity<byte[]> proxyDynmapGet(HttpServletRequest request) {
        try {
            // Извлекаем путь после /api/dynmap
            String path = extractDynmapPath(request);
            URI targetUrl = UriComponentsBuilder.fromHttpUrl(DYNMAP_BASE_URL)
                    .path(path)
                    .query(request.getQueryString())
                    .build()
                    .toUri();

            // Выполняем запрос к оригинальному Dynmap
            ResponseEntity<byte[]> response = restTemplate.getForEntity(targetUrl, byte[].class);

            // Копируем заголовки, удаляя проблемные
            HttpHeaders headers = new HttpHeaders();
            for (Map.Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
                String headerName = entry.getKey();
                if (!"X-Frame-Options".equalsIgnoreCase(headerName) &&
                    !"Content-Security-Policy".equalsIgnoreCase(headerName) &&
                    !"Access-Control-Allow-Origin".equalsIgnoreCase(headerName)) {
                    headers.addAll(headerName, entry.getValue());
                }
            }

            // Устанавливаем безопасные заголовки
            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Content-Security-Policy", "frame-ancestors 'self' https://elaisy.ru;");
            if (response.getHeaders().getContentType() != null) {
                headers.setContentType(response.getHeaders().getContentType());
            }

            return ResponseEntity.status(response.getStatusCode())
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            String jsonError = "{\"error\": \"Dynmap недоступен\", \"details\": \"" +
                    e.getMessage().replace("\"", "\\\"") + "\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Content-Security-Policy", "frame-ancestors 'self' https://elaisy.ru;");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(jsonError.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * CORS preflight
     */
    @RequestMapping(value = "/dynmap/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.set("Access-Control-Max-Age", "3600");
        headers.set("Content-Security-Policy", "frame-ancestors 'self' https://elaisy.ru;");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Извлекает путь после /api/dynmap
     */
    private String extractDynmapPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestUri.substring(contextPath.length());

        // Удаляем /api/dynmap
        if (path.startsWith("/api/dynmap")) {
            path = path.substring("/api/dynmap".length());
        }

        // Если путь пустой, возвращаем корень
        return path.isEmpty() ? "/" : path;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Security-Policy", "frame-ancestors 'self' https://elaisy.ru;")
                .body("Test OK");
    }
}
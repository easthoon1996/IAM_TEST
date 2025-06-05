package com.dreamsecurity.iam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class EmployeeDownloadController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sap.mock.auth}")
    private String sapMockAuth;

    @Value("${sap.mock.base-url}")
    private String baseUrl;

    @Value("${sap.mock.download-api}")
    private String downloadApi;

    @GetMapping("/download/employees")
    public void proxyDownloadCsv(HttpServletResponse response) throws Exception {
        String sapCsvUrl = baseUrl + downloadApi;

        // ✅ Basic Auth 헤더 추가
        String base64Creds = Base64.getEncoder().encodeToString(sapMockAuth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                sapCsvUrl,
                HttpMethod.GET,
                entity,
                byte[].class
        );

        HttpHeaders sapHeaders = responseEntity.getHeaders();
        String contentType = sapHeaders.getContentType() != null ? sapHeaders.getContentType().toString() : "application/octet-stream";
        String contentDisposition = sapHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION);

        response.setContentType(contentType);
        if (contentDisposition != null) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        } else {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"employees.csv\"");
        }

        byte[] body = responseEntity.getBody();
        if (body != null) {
            response.setContentLength(body.length);
            try (OutputStream out = response.getOutputStream()) {
                out.write(body);
                out.flush();
            }
        }
    }
}

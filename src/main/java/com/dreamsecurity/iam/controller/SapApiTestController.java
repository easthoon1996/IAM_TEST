package com.dreamsecurity.iam.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class SapApiTestController {

    @GetMapping("/test-sap-api")
    public String testSapApi() {
        String apiUrl = "http://localhost:8080/sap/opu/odata/sap/EMPLOYEE_BASIC_SRV/Employees?$skip=0&$top=5";

        // Basic Auth: admin:secret
        String plainCreds = "admin:secret";
        String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // 응답 반환
        return response.getBody();
    }
}

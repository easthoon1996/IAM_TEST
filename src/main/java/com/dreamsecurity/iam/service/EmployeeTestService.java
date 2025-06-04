package com.dreamsecurity.iam.service;

import com.dreamsecurity.iam.model.EmployeeDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmployeeTestService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeTestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * JSON 응답을 EmployeeDto 리스트로 변환
     */
    public List<EmployeeDto> parseEmployeeResponse(String jsonResponse) throws Exception {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return Collections.emptyList();
        }

        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("d").path("results");

        if (!resultsNode.isArray()) {
            return Collections.emptyList();
        }

        List<EmployeeDto> employeeList = new ArrayList<>();

        for (JsonNode node : resultsNode) {
            EmployeeDto dto = new EmployeeDto();

            // firstName + lastName → ename
            String firstName = node.path("firstName").asText("");
            String lastName = node.path("lastName").asText("");
            String ename = (firstName + " " + lastName).trim();

            dto.setPernr(node.path("employeeId").asText(""));
            dto.setEname(ename);
            dto.setOrgeh(node.path("department").asText(""));
            dto.setPlans(node.path("jobTitle").asText(""));
            dto.setEmail(node.path("workEmail").asText(""));
            dto.setBegda(node.path("hireDate").asText(""));
            dto.setEndda(node.path("terminationDate").asText(""));

            employeeList.add(dto);
        }

        return employeeList;
    }

    /**
     * 인증 헤더 생성 메서드 (재사용 가능)
     */
    private HttpHeaders createAuthHeaders(String username, String password) {
        String plainCreds = username + ":" + password;
        String base64Creds = Base64.getEncoder()
                .encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

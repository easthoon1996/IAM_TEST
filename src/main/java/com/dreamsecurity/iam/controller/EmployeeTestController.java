package com.dreamsecurity.iam.controller;

import com.dreamsecurity.iam.config.RestTemplateConfig;
import com.dreamsecurity.iam.model.EmployeeDto;
import com.dreamsecurity.iam.model.EmployeeFormDto;
import com.dreamsecurity.iam.service.EmployeeTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
public class EmployeeTestController {

    @Autowired
    private RestTemplate restTemplate; // RestTemplateConfig에서 만든 Bean을 사용!

    @Autowired
    private EmployeeTestService employeeTestService;

    @Value("${sap.mock.auth}")
    private String sapMockAuth;

    @Value("${sap.mock.base-url}")
    private String baseUrl;

    @Value("${sap.mock.employees-api}")
    private String employeesApi;

    @Value("${sap.mock.generate-api}")
    private String generateApi;

    @Value("${sap.mock.add-api}")
    private String addApi;

    @GetMapping("/test-sap-api")
    public String testSapApi(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) throws Exception {
        int skip = (page - 1) * size;
        String apiUrl = String.format(baseUrl + employeesApi + "?$skip=%d&$top=%d", skip, size);

        // 인증 헤더
        String base64Creds = Base64.getEncoder().encodeToString(sapMockAuth.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 요청
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // JSON 응답을 EmployeeDto List로 변환
        List<EmployeeDto> employeeList = employeeTestService.parseEmployeeResponse(response.getBody());
        model.addAttribute("employeeList", employeeList);

        // 페이지 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "sap-api-test";
    }

    @GetMapping("/add")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeFormDto());
        return "add-employee";
    }

    @GetMapping("/generate-employees")
    public String generateEmployees(
            @RequestParam(name = "count", defaultValue = "20") int count, // 사용자로부터 개수 입력 받기
            Model model
    ) throws Exception {
        String apiUrl = baseUrl + generateApi +"?count=" + count;

        // Basic Auth 헤더
        String base64Creds = Base64.getEncoder().encodeToString(sapMockAuth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // restTemplate을 사용하여 POST 요청
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return "redirect:/test-sap-api";
    }

    @PostMapping("/add-employee")
    public String addEmployee(
            @ModelAttribute EmployeeFormDto employeeForm,
            RedirectAttributes redirectAttributes
    ) {
        String sapMockUrl = baseUrl + employeesApi;

        try {
            String base64Creds = Base64.getEncoder().encodeToString(sapMockAuth.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Creds);
            headers.setContentType(MediaType.APPLICATION_JSON); // ✅ JSON 요청 명시

            // 🔥 EmployeeFormDto를 그대로 JSON 요청으로 전송
            HttpEntity<EmployeeFormDto> request = new HttpEntity<>(employeeForm, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(sapMockUrl, request, String.class);

            // 응답 로깅
            System.out.println("응답 상태코드: " + response.getStatusCode());
            System.out.println("응답 본문: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("message", "사용자가 추가되었습니다!");
            } else {
                redirectAttributes.addFlashAttribute("error", "사용자 추가 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "오류: " + e.getMessage());
        }

        return "redirect:/test-sap-api";
    }



}
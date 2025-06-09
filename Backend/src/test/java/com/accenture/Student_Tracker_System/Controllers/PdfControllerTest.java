
package com.accenture.Student_Tracker_System.Controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PdfControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Map<String, Object> createSampleStudentData() {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "John");
        data.put("lastName", "Doe");
        data.put("regNo", "1");
        data.put("standard", "10");
        data.put("admissionDate", "2024-01-01");
        data.put("address", "123 Main St");
        data.put("mobileNo", "1234567890");
        data.put("emailId", "john.doe@example.com");
        data.put("DOB", "2000-01-01");
        data.put("guardianName", "Jane Doe");
        data.put("reasonOfLeaving", "PASSED_AND_LEFT");
        data.put("remarks", "EXCELLENT");
        return data;
    }

    @Test
    void generatePdf_ValidRegNo_ReturnsPdfDocument() throws IOException {
        // Arrange
        String regNo = "1";
        Map<String, Object> studentData = createSampleStudentData();
        when(restTemplate.getForObject(
                eq("http://localhost:8080/student/" + regNo),
                eq(Map.class)
        )).thenReturn(studentData);

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(regNo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Verify PDF validity
        try (PDDocument document = PDDocument.load(response.getBody())) {
            assertNotNull(document);
            assertEquals(1, document.getNumberOfPages());
        }

        // Verify headers
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.APPLICATION_PDF, headers.getContentType());
        assertTrue(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("tc_" + regNo + ".pdf"));
    }

    @Test
    void generatePdf_StudentNotFound_ReturnsNotFound() throws IOException {
        // Arrange
        String regNo = "999";
        when(restTemplate.getForObject(
                eq("http://localhost:8080/student/" + regNo),
                eq(Map.class)
        )).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(regNo);

        // Assert
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void generatePdf_WithEmptyData_HandlesGracefully() throws IOException {
        // Arrange
        String regNo = "1";
        Map<String, Object> studentData = new HashMap<>();
        when(restTemplate.getForObject(
                eq("http://localhost:8080/student/" + regNo),
                eq(Map.class)
        )).thenReturn(studentData);

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(regNo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Verify PDF validity
        try (PDDocument document = PDDocument.load(response.getBody())) {
            assertNotNull(document);
            assertEquals(1, document.getNumberOfPages());
        }
    }

    @Test
    void generatePdf_WithMissingFields_HandlesGracefully() throws IOException {
        // Arrange
        String regNo = "1";
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("firstName", "John");
        // Missing other fields
        when(restTemplate.getForObject(
                eq("http://localhost:8080/student/" + regNo),
                eq(Map.class)
        )).thenReturn(studentData);

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(regNo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Verify PDF validity
        try (PDDocument document = PDDocument.load(response.getBody())) {
            assertNotNull(document);
            assertEquals(1, document.getNumberOfPages());
        }
    }

    @Test
    void generatePdf_WithNullValues_HandlesGracefully() throws IOException {
        // Arrange
        String regNo = "1";
        Map<String, Object> studentData = createSampleStudentData();
        studentData.put("firstName", null);
        studentData.put("lastName", null);
        when(restTemplate.getForObject(
                eq("http://localhost:8080/student/" + regNo),
                eq(Map.class)
        )).thenReturn(studentData);

        // Act
        ResponseEntity<byte[]> response = pdfController.generatePdf(regNo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Verify PDF validity
        try (PDDocument document = PDDocument.load(response.getBody())) {
            assertNotNull(document);
            assertEquals(1, document.getNumberOfPages());
        }
    }
}
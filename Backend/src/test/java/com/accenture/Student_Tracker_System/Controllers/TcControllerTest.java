package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import com.accenture.Student_Tracker_System.Services.TCService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TcControllerTest {

    @Mock
    private TCService tcService;

    @InjectMocks
    private TcController tcController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTC_ValidRegNo_ReturnsDTO() {
        // Arrange
        String regNo = "1";
        TransferCertificate tc = createSampleTC();
        when(tcService.getTC(1)).thenReturn(tc);

        // Act
        TransferCertificateDTO result = tcController.getTC(regNo);

        // Assert
        assertNotNull(result);
        assertEquals(tc.getUniqueID(), result.getUniqueID());
        assertEquals(tc.getRemark(), result.getRemark());
        verify(tcService).getTC(1);
    }

    @Test
    void getTC_InvalidRegNo_ThrowsBadRequest() {
        // Arrange
        String regNo = "invalid";

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tcController.getTC(regNo));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("INVALID REG NUMBER", exception.getReason());
    }

    @Test
    void getTC_StudentNotFound_ThrowsNotFound() {
        // Arrange
        String regNo = "999";
        when(tcService.getTC(999)).thenThrow(new RuntimeException());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> tcController.getTC(regNo));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Student not found", exception.getReason());
    }

    @Test
    void createTC_ValidDTO_ReturnsTC() {
        // Arrange
        TransferCertificateDTO tcDTO = new TransferCertificateDTO();
        tcDTO.setStudentId(1);
        TransferCertificate expectedTC = createSampleTC();
        when(tcService.issueTC(eq(1), any(TransferCertificateDTO.class))).thenReturn(expectedTC);

        // Act
        TransferCertificate result = tcController.createTC(tcDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTC, result);
        verify(tcService).issueTC(eq(1), any(TransferCertificateDTO.class));
    }

    private TransferCertificate createSampleTC() {
        TransferCertificate tc = new TransferCertificate();
        tc.setUniqueID(1L);
        tc.setRemark(Remarks.GOOD);
        tc.setReasonOfLeaving(ReasonOfLeaving.PASSED_AND_LEFT);
        tc.setIssuedDate(LocalDate.now());

        Student student = new Student();
        student.setRegNo(1);
        tc.setStudent(student);

        return tc;
    }

    @Test
    void createTC_PassedAndLeft() {
        // Arrange
        TransferCertificateDTO tcDTO = new TransferCertificateDTO();
        tcDTO.setStudentId(1);
        tcDTO.setRemark(Remarks.GOOD);
        tcDTO.setReasonOfLeaving(ReasonOfLeaving.PASSED_AND_LEFT);

        TransferCertificate expectedTC = createSampleTC();
        expectedTC.setReasonOfLeaving(ReasonOfLeaving.PASSED_AND_LEFT);
        when(tcService.issueTC(eq(1), any(TransferCertificateDTO.class))).thenReturn(expectedTC);

        // Act
        TransferCertificate result = tcController.createTC(tcDTO);

        // Assert
        assertNotNull(result);
        assertEquals(ReasonOfLeaving.PASSED_AND_LEFT, result.getReasonOfLeaving());
        verify(tcService).issueTC(eq(1), any(TransferCertificateDTO.class));
    }

    @Test
    void createTC_Rusticated() {
        // Arrange
        TransferCertificateDTO tcDTO = new TransferCertificateDTO();
        tcDTO.setStudentId(1);
        tcDTO.setRemark(Remarks.GOOD);
        tcDTO.setReasonOfLeaving(ReasonOfLeaving.RUSTICATED);

        TransferCertificate expectedTC = createSampleTC();
        expectedTC.setReasonOfLeaving(ReasonOfLeaving.RUSTICATED);
        when(tcService.issueTC(eq(1), any(TransferCertificateDTO.class))).thenReturn(expectedTC);

        // Act
        TransferCertificate result = tcController.createTC(tcDTO);

        // Assert
        assertNotNull(result);
        assertEquals(ReasonOfLeaving.RUSTICATED, result.getReasonOfLeaving());
        verify(tcService).issueTC(eq(1), any(TransferCertificateDTO.class));
    }

    @Test
    void createTC_AllReasonOfLeavingValues() {
        for (ReasonOfLeaving reason : ReasonOfLeaving.values()) {
            // Arrange
            TransferCertificateDTO tcDTO = new TransferCertificateDTO();
            tcDTO.setStudentId(1);
            tcDTO.setRemark(Remarks.GOOD);
            tcDTO.setReasonOfLeaving(reason);

            TransferCertificate expectedTC = createSampleTC();
            expectedTC.setReasonOfLeaving(reason);
            when(tcService.issueTC(eq(1), any(TransferCertificateDTO.class))).thenReturn(expectedTC);

            // Act
            TransferCertificate result = tcController.createTC(tcDTO);

            // Assert
            assertEquals(reason, result.getReasonOfLeaving());
        }
    }

}
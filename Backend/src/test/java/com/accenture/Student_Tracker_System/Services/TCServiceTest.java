package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TCServiceTest {

    @Mock
    private TCRepository tcRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private TCService tcService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Student createSampleStudent() {
        Student student = new Student();
        student.setRegNo(1);
        student.setStandard(12);
        student.setStatus(Status.ACTIVE);
        return student;
    }

    private TransferCertificateDTO createSampleDTO(ReasonOfLeaving reason) {
        TransferCertificateDTO dto = new TransferCertificateDTO();
        dto.setRemark(Remarks.GOOD);
        dto.setReasonOfLeaving(reason);
        dto.setStudentId(1);
        dto.setDOB(new Date());
        dto.setGuardianName("John Doe");
        return dto;
    }

    @Test
    void issueTC_PassedAndLeft_Grade12() {
        // Arrange
        Student student = createSampleStudent();
        TransferCertificateDTO dto = createSampleDTO(ReasonOfLeaving.PASSED_AND_LEFT);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(tcRepository.save(any(TransferCertificate.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        TransferCertificate result = tcService.issueTC(dto.getStudentId(), dto);

        // Assert
        assertNotNull(result);
        assertEquals(Status.GRADUATED, student.getStatus());
        assertNull(student.getRollNo());
        assertEquals(ReasonOfLeaving.PASSED_AND_LEFT, result.getReasonOfLeaving());
        verify(tcRepository).save(any(TransferCertificate.class));
    }

    @Test
    void issueTC_PassedAndLeft_NotGrade12() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(11);
        TransferCertificateDTO dto = createSampleDTO(ReasonOfLeaving.PASSED_AND_LEFT);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(tcRepository.save(any(TransferCertificate.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        TransferCertificate result = tcService.issueTC(dto.getStudentId(), dto);

        // Assert
        assertNotNull(result);
        assertEquals(Status.RESCINDED, student.getStatus());
        assertNull(student.getRollNo());
        assertEquals(ReasonOfLeaving.PASSED_AND_LEFT, result.getReasonOfLeaving());
        verify(tcRepository).save(any(TransferCertificate.class));
    }

    @Test
    void issueTC_Rusticated() {
        // Arrange
        Student student = createSampleStudent();
        TransferCertificateDTO dto = createSampleDTO(ReasonOfLeaving.RUSTICATED);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(tcRepository.save(any(TransferCertificate.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        TransferCertificate result = tcService.issueTC(dto.getStudentId(), dto);

        // Assert
        assertNotNull(result);
        assertEquals(Status.RESCINDED, student.getStatus());
        assertNull(student.getRollNo());
        assertEquals(ReasonOfLeaving.RUSTICATED, result.getReasonOfLeaving());
        verify(tcRepository).save(any(TransferCertificate.class));
    }

    // Other existing tests...
}
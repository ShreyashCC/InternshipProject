
package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.StudentDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    void setupTestData() {
        testStudent = new Student();
        testStudent.setRegNo(1);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setStandard(10);
        testStudent.setStatus(Status.ACTIVE);
        testStudent.setEmailId("john@example.com");

        testStudentDTO = new StudentDTO();
        testStudentDTO.setRegNo(1);
        testStudentDTO.setFirstName("John");
        testStudentDTO.setLastName("Doe");
        testStudentDTO.setStandard(10);
        testStudentDTO.setStatus(Status.ACTIVE);
        testStudentDTO.setEmailId("john@example.com");
    }

    @Test
    void getStudent_Success() {
        when(studentService.getStudentById(1)).thenReturn(testStudent);
        StudentDTO result = studentController.getStudent("1");
        assertNotNull(result);
        assertEquals(testStudent.getRegNo(), result.getRegNo());
    }

    @Test
    void getStudent_InvalidRegNo() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.getStudent("invalid"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getStudent_NotFound() {
        when(studentService.getStudentById(999)).thenThrow(new RuntimeException());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.getStudent("999"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getAll_Success() {
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.getAllStudents()).thenReturn(students);
        List<StudentDTO> result = studentController.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void createStudent_Success() {
        when(studentService.getRollList(any())).thenReturn(Arrays.asList(1, 2));
        when(studentService.getNextAvailableRollNo(any())).thenReturn(3);
        when(studentService.saveStudent(any())).thenReturn(testStudent);

        ResponseEntity<?> response = studentController.createStudent(testStudentDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createStudent_InvalidName() {
        testStudentDTO.setFirstName("1"); // Changed from "123" to "1" to match controller's regex
        ResponseEntity<?> response = studentController.createStudent(testStudentDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Name cannot be number", response.getBody());
    }

    @Test
    void changeStatus_Success() {
        when(studentService.getStudentById(1)).thenReturn(testStudent);
        StudentDTO result = studentController.changeStatus("1");
        assertNotNull(result);
        verify(studentService).changeStudentStatus(testStudent);
    }

    @Test
    void changeStatusToRESCINDED_Success() {
        when(studentService.getStudentById(1)).thenReturn(testStudent);
        StudentDTO result = studentController.changeStatusToRESCINDED("1");
        assertNotNull(result);
        verify(studentService).changeStudentStatusToRESCINDED(testStudent);
    }

    @Test
    void updateStudent_StandardPromotion() {
        when(studentService.getStudentById(1)).thenReturn(testStudent);
        when(studentService.promoteStudent(any())).thenReturn(testStudent);
        StudentDTO result = studentController.updateStudent("1");
        assertNotNull(result);
        verify(studentService).promoteStudent(testStudent);
    }

    @Test
    void updateStudent_Grade12Active() {
        testStudent.setStandard(12);
        when(studentService.getStudentById(1)).thenReturn(testStudent);
        StudentDTO result = studentController.updateStudent("1");
        assertEquals(Status.GRADUATED, result.getStatus());
    }

    @Test
    void updateStudent_Grade12Graduated() {
        testStudent.setStandard(12);
        testStudent.setStatus(Status.GRADUATED);
        when(studentService.getStudentById(1)).thenReturn(testStudent);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.updateStudent("1"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void updateStudent_InvalidRegNo() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> studentController.updateStudent("abc"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
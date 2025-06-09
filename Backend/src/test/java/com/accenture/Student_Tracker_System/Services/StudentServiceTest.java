package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TCRepository tcRepository;

    @InjectMocks
    private StudentService studentService;

    private Student createSampleStudent() {
        Student student = new Student();
        student.setRegNo(1);
        student.setStandard(10);
        student.setRollNo(1);
        student.setStatus(Status.ACTIVE);
        return student;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNextAvailableRollNo_EmptyList() {
        // Arrange
        List<Integer> existingRollNos = Collections.emptyList();

        // Act
        int result = studentService.getNextAvailableRollNo(existingRollNos);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void getNextAvailableRollNo_WithGap() {
        // Arrange
        List<Integer> existingRollNos = Arrays.asList(1, 2, 4, 5);

        // Act
        int result = studentService.getNextAvailableRollNo(existingRollNos);

        // Assert
        assertEquals(3, result);
    }

    @Test
    void getNextAvailableRollNo_NoGap() {
        // Arrange
        List<Integer> existingRollNos = Arrays.asList(1, 2, 3, 4);

        // Act
        int result = studentService.getNextAvailableRollNo(existingRollNos);

        // Assert
        assertEquals(5, result);
    }

    @Test
    void getNextAvailableRollNo_SingleElement() {
        // Arrange
        List<Integer> existingRollNos = Collections.singletonList(2);

        // Act
        int result = studentService.getNextAvailableRollNo(existingRollNos);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void saveStudent_Success() {
        // Arrange
        Student student = createSampleStudent();
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student result = studentService.saveStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(student.getRegNo(), result.getRegNo());
        verify(studentRepository).save(student);
    }

    @Test
    void getStudentById_Success() {
        // Arrange
        Student student = createSampleStudent();
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // Act
        Student result = studentService.getStudentById(1);

        // Assert
        assertNotNull(result);
        assertEquals(student.getRegNo(), result.getRegNo());
        verify(studentRepository).findById(1);
    }

    @Test
    void getAllStudents_Success() {
        // Arrange
        List<Student> students = Arrays.asList(createSampleStudent(), createSampleStudent());
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getAllStudents_EmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository).findAll();
    }

    @Test
    void displayStudentDetails_Success() {
        // Arrange
        Student student = createSampleStudent();

        // Act & Assert
        assertDoesNotThrow(() -> studentService.displayStudentDetails(student));
    }

    @Test
    void promoteStudent_Success() {
        // Arrange
        Student student = createSampleStudent();
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.findAllRollNosByStandard(11)).thenReturn(Arrays.asList(1, 2));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student result = studentService.promoteStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(11, result.getStandard());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void promoteStudent_ToGraduated() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(12);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.findAllRollNosByStandard(13)).thenReturn(Collections.emptyList());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student result = studentService.promoteStudent(student);

        // Assert
        assertNotNull(result);
        assertEquals(13, result.getStandard());
        assertEquals(Status.GRADUATED, result.getStatus());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void promoteStudent_NotFound() {
        // Arrange
        Student student = createSampleStudent();
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.promoteStudent(student));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void changeStudentStatus_ToGraduated() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(12);

        // Act
        studentService.changeStudentStatus(student);

        // Assert
        assertEquals(Status.GRADUATED, student.getStatus());
        verify(studentRepository).save(student);
    }

    @Test
    void changeStudentStatus_ToRescinded() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(11);

        // Act
        studentService.changeStudentStatus(student);

        // Assert
        assertEquals(Status.RESCINDED, student.getStatus());
        verify(studentRepository).save(student);
    }

    @Test
    void changeStudentStatusToRESCINDED() {
        // Arrange
        Student student = createSampleStudent();

        // Act
        studentService.changeStudentStatusToRESCINDED(student);

        // Assert
        assertEquals(Status.RESCINDED, student.getStatus());
        verify(studentRepository).save(student);
    }

    @Test
    void getRollList_Success() {
        // Arrange
        List<Integer> expectedRolls = Arrays.asList(1, 2, 3);
        when(studentRepository.findAllRollNosByStandard(10)).thenReturn(expectedRolls);

        // Act
        List<Integer> result = studentService.getRollList(10);

        // Assert
        assertEquals(expectedRolls, result);
        verify(studentRepository).findAllRollNosByStandard(10);
    }

    @Test
    void getRollList_EmptyList() {
        // Arrange
        when(studentRepository.findAllRollNosByStandard(10)).thenReturn(Collections.emptyList());

        // Act
        List<Integer> result = studentService.getRollList(10);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository).findAllRollNosByStandard(10);
    }

    @Test
    void updateStudentStatus_Active() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(11);

        // Act
        studentService.updateStudentStatus(student);

        // Assert
        assertEquals(12, student.getStandard());
        assertEquals(Status.ACTIVE, student.getStatus());
    }

    @Test
    void updateStudentStatus_Graduated() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(12);

        // Act
        studentService.updateStudentStatus(student);

        // Assert
        assertEquals(13, student.getStandard());
        assertEquals(Status.GRADUATED, student.getStatus());
    }

    @Test
    void updateStudentStatus_AlreadyGraduated() {
        // Arrange
        Student student = createSampleStudent();
        student.setStandard(13);

        // Act
        studentService.updateStudentStatus(student);

        // Assert
        assertEquals(14, student.getStandard());
        assertEquals(Status.GRADUATED, student.getStatus());
    }
}
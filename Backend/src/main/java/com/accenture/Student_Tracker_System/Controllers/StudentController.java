package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.StudentDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Services.StudentService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    @GetMapping("/{regNo}")
    public StudentDTO getStudent(@PathVariable String regNo) {
        try {
            logger.info("Fetching student with regNo: {}", regNo);
            Student student = studentService.getStudentById(Integer.parseInt(regNo));
            return StudentToDTO(student);
        } catch (NumberFormatException e) {
            logger.error("Invalid registration number format: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid registration number format");
        } catch (Exception e) {
            logger.error("Student not found with regNo: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    @GetMapping
    public List<StudentDTO> getAll() {
        try {
            logger.info("Fetching all students");
            List<Student> students = studentService.getAllStudents();
            return students.stream().map(this::StudentToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to fetch students", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving students");
        }
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        logger.info("Creating student: {}", studentDTO.getFirstName());
        if (studentDTO.getFirstName().matches("\\d")) {
            logger.warn("Invalid first name: {}", studentDTO.getFirstName());
            return ResponseEntity.badRequest().body("Name cannot be number");
        }
        try {
            Student newStudent = DTOToStudent(studentDTO);
            List<Integer> existingRollNos = studentService.getRollList(newStudent.getStandard());
            newStudent.setRollNo(studentService.getNextAvailableRollNo(existingRollNos));
            logger.info("Student created successfully with Roll No: {}", newStudent.getRollNo());
            return ResponseEntity.ok(studentService.saveStudent(newStudent));
        } catch (Exception e) {
            logger.error("Failed to create student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create student");
        }
    }

    @GetMapping("/status/{regNo}")
    public StudentDTO changeStatus(@PathVariable String regNo) {
        try {
            logger.info("Changing status for regNo: {}", regNo);
            Student student = studentService.getStudentById(Integer.parseInt(regNo));
            studentService.changeStudentStatus(student);
            logger.info("Status changed successfully for regNo: {}", regNo);
            return StudentToDTO(student);
        } catch (Exception e) {
            logger.error("Failed to change status for regNo: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FAILED TO GENERATE PDF");
        }
    }

    @GetMapping("/status/tc/{regNo}")
    public StudentDTO changeStatusToRESCINDED(@PathVariable String regNo) {
        try {
            logger.info("Changing status to RESCINDED for regNo: {}", regNo);
            Student student = studentService.getStudentById(Integer.parseInt(regNo));
            studentService.changeStudentStatusToRESCINDED(student);
            logger.info("Status changed to RESCINDED for regNo: {}", regNo);
            return StudentToDTO(student);
        } catch (Exception e) {
            logger.error("Failed to change status to RESCINDED for regNo: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FAILED TO GENERATE PDF");
        }
    }

    @GetMapping("/promoted/{regNo}")
    public StudentDTO updateStudent(@PathVariable String regNo) {
        try {
            logger.info("Promoting student with regNo: {}", regNo);
            Student student = studentService.getStudentById(Integer.parseInt(regNo));
            if (student.getStandard() == 12 && student.getStatus() == Status.GRADUATED) {
                logger.warn("Student already graduated: regNo {}", regNo);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student from standard 12 cannot be promoted");
            } else if (student.getStandard() == 12 && student.getStatus() == Status.ACTIVE) {
                studentService.changeStudentStatus(student);
                student.setStatus(Status.GRADUATED);
                logger.info("Student graduated: regNo {}", regNo);
                return StudentToDTO(student);
            }
            Student promotedStudent = studentService.promoteStudent(student);
            logger.info("Student promoted successfully: regNo {}", regNo);
            return StudentToDTO(promotedStudent);
        } catch (NumberFormatException e) {
            logger.error("Invalid registration number format: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid registration number format");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Promotion failed for regNo: {}", regNo, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Registration No");
        }
    }

    private Student DTOToStudent(StudentDTO studentDTO) {
        try {
            Student student = new Student();
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());
            student.setRollNo(studentDTO.getRollNo());
            student.setStandard(studentDTO.getStandard());
            student.setAdmissionDate(studentDTO.getAdmissionDate());
            student.setAddress(studentDTO.getAddress());
            student.setMobileNo(studentDTO.getMobileNo());
            student.setEmailId(studentDTO.getEmailId());
            student.setStatus(studentDTO.getStatus());
            student.setRegNo(studentDTO.getRegNo());
            return student;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid student DTO input", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input");
        }
    }

    private StudentDTO StudentToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setRollNo(student.getRollNo());
        studentDTO.setStandard(student.getStandard());
        studentDTO.setAdmissionDate(student.getAdmissionDate());
        studentDTO.setAddress(student.getAddress());
        studentDTO.setMobileNo(student.getMobileNo());
        studentDTO.setEmailId(student.getEmailId());
        studentDTO.setStatus(student.getStatus());
        studentDTO.setRegNo(student.getRegNo());
        return studentDTO;
    }
}

// add students
// show student details
// promote student to new class

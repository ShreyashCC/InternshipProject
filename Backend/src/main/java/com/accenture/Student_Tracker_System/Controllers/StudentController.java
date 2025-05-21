package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.StudentDTO;
import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

//    Showing student details based upon a unique rollNo
    @GetMapping("/{rollNo}")
    public StudentDTO getStudent(@PathVariable String rollNo)  {
        try {
            Student student = studentService.getStudentById(Integer.parseInt(rollNo));
            return StudentToDTO(student);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid roll number format");
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }
    @GetMapping
    public List<StudentDTO> getAll() throws ResponseStatusException {
        List<Student> students = studentService.getAllStudents();
        return students.stream().map(this::StudentToDTO).collect(Collectors.toList());
    }
    @PostMapping
    public Student createStudent(@RequestBody StudentDTO studentDTO) {
        Student newStudent = DTOToStudent(studentDTO);
        List<Integer> existingRollNos = studentService.getRollList(newStudent.getStandard());
        newStudent.setRollNo(studentService.getNextAvailableRollNo(newStudent.getStandard(), existingRollNos));
        return studentService.saveStudent(newStudent);
    }
    @GetMapping("/promoted/{rollNo}")
    public StudentDTO updateStudent(@PathVariable String rollNo) {
        try {
            Student student = studentService.getStudentById(Integer.parseInt(rollNo));
            Student promotedStudent = studentService.promoteStudent(student);
            return StudentToDTO(promotedStudent);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid roll number format");
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Roll No");
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
            return student;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Input");
        }
    }

    private StudentDTO StudentToDTO(Student student){
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
        return studentDTO;
    }
}
// add students
// show student details
// promote student to new class

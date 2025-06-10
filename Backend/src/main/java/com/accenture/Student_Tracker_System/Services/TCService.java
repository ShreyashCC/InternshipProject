package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@Transactional
public class TCService {

    private static final Logger logger = LoggerFactory.getLogger(TCService.class);

    @Autowired
    TCRepository tcRepository;

    @Autowired
    StudentRepository studentRepository;

    public TransferCertificate getTC (Integer id){
        return tcRepository.findById(id).orElseThrow(() -> {
            logger.error("TransferCertificate not found with id: {}", id);
            return new RuntimeException("TransferCertificate not found");
        });
    }

    public TransferCertificate issueTC(Integer studentId, TransferCertificateDTO dto) {
        logger.info("Issuing TransferCertificate for studentId: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student not found with id: {}", studentId);
                    return new RuntimeException("Student not found");
                });

        TransferCertificate tc = new TransferCertificate();
        tc.setIssuedDate(LocalDate.now());
        tc.setRemark(dto.getRemark());
        tc.setReasonOfLeaving(dto.getReasonOfLeaving());
        student.setStandard(student.getStandard());
        student.setRollNo(null);
        if (student.getStandard() == 12 && dto.getReasonOfLeaving() == ReasonOfLeaving.PASSED_AND_LEFT) {
            student.setStatus(Status.GRADUATED);
            logger.info("Student {} graduated", studentId);
        } else  {
            student.setStatus(Status.RESCINDED);
            logger.info("Student {} status set to RESCINDED", studentId);
        }
        student.setTC(tc);
        tc.setStudent(student);
        if (dto.getDOB() != null) tc.setDOB(dto.getDOB());
        if (dto.getGuardianName() != null) tc.setGuardianName(dto.getGuardianName());

        return tcRepository.save(tc);
    }
}
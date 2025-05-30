package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@Transactional
public class TCService {

    @Autowired
    TCRepository tcRepository;

    @Autowired
    StudentRepository studentRepository;

    public TransferCertificate getTC (Integer id){
        return tcRepository.findById(id).get();
    }

    public TransferCertificate issueTC(Integer studentId, TransferCertificateDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        TransferCertificate tc = new TransferCertificate();
        tc.setIssuedDate(LocalDate.now());
        tc.setRemark(dto.getRemark());
        tc.setReasonOfLeaving(dto.getReasonOfLeaving());
        student.setStandard(student.getStandard());
        student.setRollNo(null);
        if (student.getStandard() == 12 && dto.getReasonOfLeaving() == ReasonOfLeaving.PASSED_AND_LEFT) {
            student.setStatus(Status.GRADUATED);
        } else  {
            student.setStatus(Status.RESCINDED);
        }
        student.setTC(tc);
        tc.setStudent(student);
        if (dto.getDOB() != null) tc.setDOB(dto.getDOB());
        if (dto.getGuardianName() != null) tc.setGuardianName(dto.getGuardianName());

        return tcRepository.save(tc);
    }
}

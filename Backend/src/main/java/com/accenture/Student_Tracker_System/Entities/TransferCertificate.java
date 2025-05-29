package com.accenture.Student_Tracker_System.Entities;

import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class TransferCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueID;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Remarks remark;
    private ReasonOfLeaving reasonOfLeaving;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate issuedDate;

    private Date DOB;
    private String GuardianName;


    public Long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Long uniqueID) {
        this.uniqueID = uniqueID;
    }


    public Remarks getRemark() {
        return remark;
    }

    public void setRemark(Remarks remark) {
        this.remark = remark;
    }

    public ReasonOfLeaving getReasonOfLeaving() {
        return reasonOfLeaving;
    }

    public void setReasonOfLeaving(ReasonOfLeaving reasonOfLeaving) {
        this.reasonOfLeaving = reasonOfLeaving;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public void setGuardianName(String guardianName) {
        GuardianName = guardianName;
    }


    public String getGuardianName() {
        return GuardianName;
    }
}

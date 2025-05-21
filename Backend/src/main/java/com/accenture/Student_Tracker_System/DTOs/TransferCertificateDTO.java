package com.accenture.Student_Tracker_System.DTOs;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Date;

public class TransferCertificateDTO {

    private Long uniqueID;
    private Student student;
    private Remarks remark;
    private ReasonOfLeaving reasonOfLeaving;
    private Date issuedDate;

    public Long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
}

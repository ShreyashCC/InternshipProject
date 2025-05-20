package com.accenture.Student_Tracker_System.Entities;

import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class TransferCertificate {

    @Id
    @GeneratedValue
    private String uniqueID;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Remarks remark;
    private ReasonOfLeaving reasonOfLeaving;
    private Date issuedDate;

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
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

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

package com.accenture.Student_Tracker_System.DTOs;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;

import java.time.LocalDate;
import java.util.Date;

public class TransferCertificateDTO {

    private Long uniqueID;
//    private Student student;
    private Remarks remark;
    private ReasonOfLeaving reasonOfLeaving;
    private LocalDate issuedDate;
    private Date DOB;
    private String GuardianName;
    private Integer studentId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
//
//    public void setStudentDetails(){
//        this.studentDetails = student.toString();
//    }
//    public String getStudentDetails(){
//        return studentDetails;
//    }

    public Long getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Long uniqueID) {
        this.uniqueID = uniqueID;
    }

//    public Student getStudent() {
//        return student;
//    }
//
//    public void setStudent(Student student) {
//        this.student = student;
//    }

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

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getGuardianName() {
        return GuardianName;
    }

    public void setGuardianName(String guardianName) {
        GuardianName = guardianName;
    }
}

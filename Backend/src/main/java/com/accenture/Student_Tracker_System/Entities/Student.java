package com.accenture.Student_Tracker_System.Entities;

import com.accenture.Student_Tracker_System.Enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regNo;

    private Integer rollNo;

    private String firstName;
    private String lastName;

    private Integer standard;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date admissionDate;

    private String address;
    private String mobileNo;
    private String emailId;
    private Status status;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransferCertificate transferCertificate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email address format");
        }
        this.emailId = emailId;
    }

    public void setTC(TransferCertificate transferCertificate){
        this.transferCertificate = transferCertificate;
    }

    public TransferCertificate getTC(){
        return transferCertificate;
    }

    public void setStatus(Status status){
        this.status=status;
    }

    public Status getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return "Student Details: " +
                "\nFirstname: "+firstName+" Lastname: "+lastName+
                "\nRoll.No: "+rollNo+" Class: "+standard+
                "\nMobile.No: "+mobileNo+" EmailId: "+emailId+
                "\nAdmissionDate: "+admissionDate+" Status: "+status+
                "\nAddress: "+address+"\n";
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }
}

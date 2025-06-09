package com.accenture.Student_Tracker_System.DTOs;
import com.accenture.Student_Tracker_System.Enums.ReasonOfLeaving;
import com.accenture.Student_Tracker_System.Enums.Remarks;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class TransferCertificateDTO {

    private Long uniqueID;
//    private Student student;
    private Remarks remark;
    private ReasonOfLeaving reasonOfLeaving;
    private LocalDate issuedDate;
    private Date DOB;
    private String GuardianName;
    private Integer studentId;

}

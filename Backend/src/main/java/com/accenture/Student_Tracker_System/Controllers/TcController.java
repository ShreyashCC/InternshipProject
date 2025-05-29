package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.StudentDTO;
import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Services.TCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/tc")
public class TcController {
    @Autowired
    private TCService tcService;

    @GetMapping("/{regNo}")
    public TransferCertificateDTO getTC(@PathVariable String regNo)  {
        try {
            TransferCertificate tc = tcService.getTC(Integer.parseInt(regNo));
            return TcToDTO(tc);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID REG NUMBER");
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
    }

    @PostMapping
    public TransferCertificate issueTC(@RequestBody TransferCertificateDTO tc) {
        TransferCertificate transferCertificate = DTOToTC(tc);
        tcService.issueTC(transferCertificate.getStudent());
        return tcService.saveTC(transferCertificate);
    }

    private TransferCertificate DTOToTC(TransferCertificateDTO transferCertificateDTO){
        TransferCertificate transferCertificate = new TransferCertificate();
        transferCertificate.setUniqueID(transferCertificateDTO.getUniqueID());
        transferCertificate.setStudent(transferCertificateDTO.getStudent());
        transferCertificate.setRemark(transferCertificateDTO.getRemark());
        transferCertificate.setReasonOfLeaving(transferCertificateDTO.getReasonOfLeaving());
        transferCertificate.setIssuedDate(transferCertificateDTO.getIssuedDate());
        transferCertificate.setDOB(transferCertificateDTO.getDOB());
        transferCertificate.setGuardianName(transferCertificateDTO.getGuardianName());
        return transferCertificate;
    }

    private TransferCertificateDTO TcToDTO(TransferCertificate transferCertificate){
        TransferCertificateDTO transferCertificateDTO = new TransferCertificateDTO();

        transferCertificateDTO.setUniqueID(transferCertificate.getUniqueID());
        transferCertificateDTO.setStudent(transferCertificate.getStudent());
        transferCertificateDTO.setRemark(transferCertificate.getRemark());
        transferCertificateDTO.setReasonOfLeaving(transferCertificate.getReasonOfLeaving());
        transferCertificateDTO.setIssuedDate(transferCertificate.getIssuedDate());
        transferCertificateDTO.setDOB(transferCertificate.getDOB());
        transferCertificateDTO.setGuardianName(transferCertificate.getGuardianName());
        return transferCertificateDTO;
    }
}

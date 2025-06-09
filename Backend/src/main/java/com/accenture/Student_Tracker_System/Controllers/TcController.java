package com.accenture.Student_Tracker_System.Controllers;
import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
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
    public TransferCertificate createTC(@RequestBody TransferCertificateDTO tcDTO) {
        return tcService.issueTC(tcDTO.getStudentId(), tcDTO);
    }

    private TransferCertificateDTO TcToDTO(TransferCertificate transferCertificate){
        TransferCertificateDTO transferCertificateDTO = new TransferCertificateDTO();
        transferCertificateDTO.setUniqueID(transferCertificate.getUniqueID());
//        transferCertificateDTO.setStudent(transferCertificate.getStudent());
        transferCertificateDTO.setRemark(transferCertificate.getRemark());
        transferCertificateDTO.setReasonOfLeaving(transferCertificate.getReasonOfLeaving());
        transferCertificateDTO.setIssuedDate(transferCertificate.getIssuedDate());
        transferCertificateDTO.setDOB(transferCertificate.getDOB());
        transferCertificateDTO.setGuardianName(transferCertificate.getGuardianName());
        transferCertificateDTO.setStudentId(transferCertificate.getStudent().getRegNo());
        return transferCertificateDTO;
    }
}

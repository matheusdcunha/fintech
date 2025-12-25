package cloud.matheusdcunha.fintech.controller;

import cloud.matheusdcunha.fintech.controller.dto.TransferMoneyDto;
import cloud.matheusdcunha.fintech.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDto dto){

        this.transferService.transferMoney(dto);

        return ResponseEntity.ok().build();

    }



}

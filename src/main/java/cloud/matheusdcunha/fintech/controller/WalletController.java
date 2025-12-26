package cloud.matheusdcunha.fintech.controller;

import cloud.matheusdcunha.fintech.controller.dto.CreateWalletDto;
import cloud.matheusdcunha.fintech.controller.dto.DepositMoneyDto;
import cloud.matheusdcunha.fintech.controller.dto.StatementDto;
import cloud.matheusdcunha.fintech.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto){

        var wallet = this.walletService.createWallet(dto);

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId())).build();
    }

    @DeleteMapping(path = "/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable UUID walletId){


        var deleted = this.walletService.deleteWallet(walletId);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(@PathVariable UUID walletId,
                                             @RequestBody @Valid DepositMoneyDto dto,
                                             HttpServletRequest servletRequest){

        this.walletService.depositMoney(
                walletId,
                dto,
                servletRequest.getAttribute("x-user-ip").toString()
        );

        return ResponseEntity.ok().build();

    }

    @GetMapping(path = "/{walletId}/statement")
    public ResponseEntity<StatementDto> getStatement(@PathVariable UUID walletId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        var statement = this.walletService.getStatements(walletId,page,pageSize);

        return ResponseEntity.ok(statement);
    }

}

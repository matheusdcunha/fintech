package cloud.matheusdcunha.fintech.service;


import cloud.matheusdcunha.fintech.controller.dto.CreateWalletDto;
import cloud.matheusdcunha.fintech.controller.dto.DepositMoneyDto;
import cloud.matheusdcunha.fintech.entities.Deposit;
import cloud.matheusdcunha.fintech.entities.Wallet;
import cloud.matheusdcunha.fintech.exceptions.DeleteWalletException;
import cloud.matheusdcunha.fintech.exceptions.WalletDataAlreadyExistsException;
import cloud.matheusdcunha.fintech.exceptions.WalletNotFoundException;
import cloud.matheusdcunha.fintech.repository.DepositRepository;
import cloud.matheusdcunha.fintech.repository.WalletRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var wallet = this.walletRepository.findByCpfOrEmail(dto.cpf(),dto.email());

        if(wallet.isPresent()){
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }

        Wallet walletEntity = new Wallet();

        walletEntity.setCpf(dto.cpf());
        walletEntity.setEmail(dto.email());
        walletEntity.setName(dto.name());
        walletEntity.setBalance(BigDecimal.ZERO);

        return this.walletRepository.save(walletEntity);
    }

    public boolean deleteWallet(UUID walletId) {

        var wallet = this.walletRepository.findById(walletId);

        if(wallet.isPresent()){

            if(wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0 ){
                throw new DeleteWalletException("the balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }

        return wallet.isPresent();

    }

    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDto dto, String ipAddress) {

        var wallet = this.walletRepository.findById(walletId).orElseThrow(()-> new WalletNotFoundException("there is no wallet with this id"));

        Deposit deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        this.depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        this.walletRepository.save(wallet);
    }
}

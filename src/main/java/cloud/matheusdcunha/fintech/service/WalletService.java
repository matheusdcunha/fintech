package cloud.matheusdcunha.fintech.service;


import cloud.matheusdcunha.fintech.controller.dto.*;
import cloud.matheusdcunha.fintech.entities.Deposit;
import cloud.matheusdcunha.fintech.entities.Wallet;
import cloud.matheusdcunha.fintech.exceptions.DeleteWalletException;
import cloud.matheusdcunha.fintech.exceptions.StatementException;
import cloud.matheusdcunha.fintech.exceptions.WalletDataAlreadyExistsException;
import cloud.matheusdcunha.fintech.exceptions.WalletNotFoundException;
import cloud.matheusdcunha.fintech.repository.DepositRepository;
import cloud.matheusdcunha.fintech.repository.WalletRepository;
import cloud.matheusdcunha.fintech.repository.dto.StatementView;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDto getStatements(UUID walletId, Integer page, Integer pageSize) {

        var wallet = this.walletRepository.findById(walletId)
                .orElseThrow(()-> new WalletNotFoundException("there is no wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

       var statements = this.walletRepository.findStatements(walletId.toString(), pageRequest)
               .map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(wallet.getWalletId(), wallet.getCpf(), wallet.getName(), wallet.getEmail(), wallet.getBalance()),
                statements.getContent(),
                new PaginationDto(statements.getNumber(), statements.getSize(), statements.getTotalElements(), statements.getTotalPages())

        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {

        if(view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
            && view.getWalletSender().equalsIgnoreCase(walletId.toString())
        ) {
            return mapWhenTransferSent(walletId, view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())
        ) {
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("invalid type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceived(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapWhenTransferSent(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private  StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}

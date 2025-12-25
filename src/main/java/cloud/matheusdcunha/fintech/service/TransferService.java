package cloud.matheusdcunha.fintech.service;

import cloud.matheusdcunha.fintech.controller.dto.TransferMoneyDto;
import cloud.matheusdcunha.fintech.entities.Transfer;
import cloud.matheusdcunha.fintech.entities.Wallet;
import cloud.matheusdcunha.fintech.exceptions.TransferException;
import cloud.matheusdcunha.fintech.exceptions.WalletNotFoundException;
import cloud.matheusdcunha.fintech.repository.TransferRepository;
import cloud.matheusdcunha.fintech.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository,
                           WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(TransferMoneyDto dto) {
        var sender = this.walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exist"));

        var receiver = this.walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exist"));

        if(sender.getBalance().compareTo(dto.value()) < 0){

            throw new TransferException("insufficiente balance. you current balance is $" + sender.getBalance());

        }

        updateWallets(dto, sender, receiver);
        persistTransfer(dto, sender, receiver);
    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        this.walletRepository.save(sender);
        this.walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        var transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());

        this.transferRepository.save(transfer);
    }
}

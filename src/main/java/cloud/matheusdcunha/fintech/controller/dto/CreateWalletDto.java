package cloud.matheusdcunha.fintech.controller.dto;

public record CreateWalletDto(
        String cpf,
        String email,
        String name
) {
}

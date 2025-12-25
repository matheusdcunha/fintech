package cloud.matheusdcunha.fintech.exceptions.dto;

public record InvalidParamDto(
        String field,
        String reason
) {
}

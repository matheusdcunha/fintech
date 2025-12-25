package cloud.matheusdcunha.fintech.exceptions;

import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;

public class WalletDataAlreadyExistsException extends FintechException{

    private final String detail;

    public WalletDataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("Wallet data already exists");
        pd.setDetail(this.detail);
        pd.setProperty("timestamp", LocalDateTime.now());

        return pd;
    }
}

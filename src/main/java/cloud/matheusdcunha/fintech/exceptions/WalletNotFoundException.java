package cloud.matheusdcunha.fintech.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;

public class WalletNotFoundException extends FintechException{

    private final String detail;

    public WalletNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pd.setTitle("Wallet not exists");
        pd.setDetail(this.detail);
        pd.setProperty("timestamp", LocalDateTime.now());

        return pd;
    }
}

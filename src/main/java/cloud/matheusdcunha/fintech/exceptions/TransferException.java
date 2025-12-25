package cloud.matheusdcunha.fintech.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;

public class TransferException extends FintechException{

    private final String detail;

    public TransferException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pd.setTitle("Transfer not allowed");
        pd.setDetail(this.detail);
        pd.setProperty("timestamp", LocalDateTime.now());

        return pd;
    }
}

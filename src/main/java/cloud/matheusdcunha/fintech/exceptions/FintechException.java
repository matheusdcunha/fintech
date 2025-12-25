package cloud.matheusdcunha.fintech.exceptions;

import org.springframework.http.ProblemDetail;

public abstract class   FintechException extends RuntimeException{
    public FintechException() {
    }

    public FintechException(String message) {
        super(message);
    }

    public FintechException(String message, Throwable cause) {
        super(message, cause);
    }

    public FintechException(Throwable cause) {
        super(cause);
    }

    public FintechException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProblemDetail toProblemDetail(){
        var pd = ProblemDetail.forStatus(500);
        pd.setTitle("Fintech Internal Server Erro");
        pd.setDetail("Contact Fintech support");

        return pd;
    }
}

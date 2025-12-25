package cloud.matheusdcunha.fintech.exceptions;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FintechException.class)
    public ProblemDetail handleFintechException(FintechException exception){
        return exception.toProblemDetail();
    }
}

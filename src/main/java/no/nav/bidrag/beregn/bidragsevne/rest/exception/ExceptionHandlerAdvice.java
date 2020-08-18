package no.nav.bidrag.beregn.bidragsevne.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler
  public ResponseEntity<?> handleUgyldigInputException(UgyldigInputException ugyldigInputException) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .header(HttpHeaders.WARNING, warningFrom(ugyldigInputException))
        .build();
  }

  @ExceptionHandler
  public ResponseEntity<?> handleSjablonConsumerException(SjablonConsumerException sjablonConsumerException) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .header(HttpHeaders.WARNING, warningFrom(sjablonConsumerException))
        .build();
  }

  private String warningFrom(RuntimeException runtimeException) {
    return String.format("%s: %s", runtimeException.getClass().getSimpleName(), runtimeException.getMessage());
  }
}

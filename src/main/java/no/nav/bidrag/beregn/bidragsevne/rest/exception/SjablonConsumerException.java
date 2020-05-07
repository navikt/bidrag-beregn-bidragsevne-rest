package no.nav.bidrag.beregn.bidragsevne.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SjablonConsumerException extends RuntimeException {

  public SjablonConsumerException(String melding) {
    super(melding);
  }
}
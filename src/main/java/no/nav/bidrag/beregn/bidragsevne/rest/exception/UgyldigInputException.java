package no.nav.bidrag.beregn.bidragsevne.rest.exception;

public class UgyldigInputException extends RuntimeException {

  public UgyldigInputException(String melding) {
    super(melding);
  }
}
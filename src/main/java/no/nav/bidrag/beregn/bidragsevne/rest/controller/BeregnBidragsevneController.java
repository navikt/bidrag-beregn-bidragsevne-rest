package no.nav.bidrag.beregn.bidragsevne.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneGrunnlag;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneResultat;
import no.nav.bidrag.beregn.bidragsevne.rest.service.BeregnBidragsevneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beregn")
public class BeregnBidragsevneController {

  private final BeregnBidragsevneService beregnBidragsevneService;

  public BeregnBidragsevneController(BeregnBidragsevneService beregnBidragsevneService) {
    this.beregnBidragsevneService = beregnBidragsevneService;
  }

  @PostMapping(path = "/bidragsevne", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<BeregnBidragsevneResultat> beregnBidragsevne(@RequestBody BeregnBidragsevneGrunnlag beregnBidragsevneGrunnlag) {
    var resultat = beregnBidragsevneService.beregn(beregnBidragsevneGrunnlag.tilCore());
    return new ResponseEntity<>(resultat.getResponseEntity().getBody(), resultat.getResponseEntity().getStatusCode());
  }
}

package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneResultat;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.felles.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonPeriodeCore;
import no.nav.bidrag.commons.web.HttpStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BeregnBidragsevneService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeregnBidragsevneService.class);

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;

  public BeregnBidragsevneService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
  }

  public HttpStatusResponse<BeregnBidragsevneResultat> beregn(BeregnBidragsevneGrunnlagAltCore grunnlagTilCore) {

    var sjablonResponse = sjablonConsumer.hentSjablontall();

    if (sjablonResponse == null) {
      LOGGER.error("Feil ved kall av bidrag-sjablon. Ingen respons");
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon. Ingen respons");
    }

    if (!(sjablonResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon. Status: {}", sjablonResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon. Status: " + sjablonResponse.getHttpStatus().toString() + " Melding: " +
          sjablonResponse.getBody());
    }

//    grunnlagTilCore.setSjablonPeriodeListe(mapSjablonVerdier(sjablonResponse.getBody()));
    grunnlagTilCore.setSjablonPeriodeListe(mapSjablonVerdierTemp());

    LOGGER.debug("Bidragsevne - grunnlag for beregning: {}", grunnlagTilCore);
    var resultatFraCore = bidragsevneCore.beregnBidragsevne(grunnlagTilCore);

    if (!resultatFraCore.getAvviksListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av bidragsevne" + System.lineSeparator()
          + "Bidragsevne - grunnlag for beregning: " + grunnlagTilCore + System.lineSeparator()
          + "Bidragsevne - avvik: " + resultatFraCore.getAvviksListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
      throw new UgyldigInputException(resultatFraCore.getAvviksListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Bidragsevne - resultat av beregning: {}", resultatFraCore.getResultatPeriodeListe());
    return new HttpStatusResponse(HttpStatus.OK, new BeregnBidragsevneResultat(resultatFraCore));
  }

  //Plukker ut aktuelle sjabloner og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonVerdier(List<Sjablontall> sjablontallListe) {
    return sjablontallListe
        .stream()
        .map(sTL -> new SjablonPeriodeCore(new PeriodeCore(sTL.getDatoFom(), sTL.getDatoTom()), sTL.getTypeSjablon(), sTL.getVerdi().doubleValue(),
            1d))
        .collect(toList());
  }

  private List<SjablonPeriodeCore> mapSjablonVerdierTemp() {

    var sjablonPeriodeListe = new ArrayList<SjablonPeriodeCore>();
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2003-01-01"), LocalDate.parse("2003-12-31")),
        "FordelSkatteklasse2", Double.valueOf(8848), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2013-01-01"), null),
        "FordelSkatteklasse2", Double.valueOf(0), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2003-01-01"), LocalDate.parse("2013-12-31")),
        "SatsTrygdeavgift", Double.valueOf(7.8), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2014-01-01"), null),
        "SatsTrygdeavgift", Double.valueOf(8.2), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "belopUnderholdEgneBarnIHusstand", Double.valueOf(3417), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "belopUnderholdEgneBarnIHusstand", Double.valueOf(3487), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2005-01-01"), LocalDate.parse("2005-05-31")),
        "MinstefradragBelop", Double.valueOf(57400), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-07-01"), LocalDate.parse("2017-12-31")),
        "MinstefradragBelop", Double.valueOf(75000), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-06-30")),
        "MinstefradragBelop", Double.valueOf(75000), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "MinstefradragBelop", Double.valueOf(83000), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "MinstefradragBelop", Double.valueOf(85050), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("9999-12-31")),
        "MinstefradragProsentInntekt", Double.valueOf(31), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "PersonfradragKlasse1", Double.valueOf(54750), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "PersonfradragKlasse1", Double.valueOf(56550), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "PersonfradragKlasse2", Double.valueOf(54750), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "PersonfradragKlasse2", Double.valueOf(56550), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")),
        "FordelSaerfradrag", Double.valueOf(13132), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), null),
        "FordelSaerfradrag", Double.valueOf(12977), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")), "Skattesats",
        Double.valueOf(23), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), null), "Skattesats",
        Double.valueOf(22), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")), "skattetrinn1",
        Double.valueOf(169000), Double.valueOf(1.4)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")), "skattetrinn2",
        Double.valueOf(237900), Double.valueOf(3.3)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")), "skattetrinn3",
        Double.valueOf(598050), Double.valueOf(12.4)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-01-01"), LocalDate.parse("2018-12-31")), "skattetrinn4",
        Double.valueOf(962050), Double.valueOf(15.4)));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31")), "skattetrinn1",
        Double.valueOf(174500), Double.valueOf(1.9)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31")), "skattetrinn2",
        Double.valueOf(245650), Double.valueOf(4.2)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31")), "skattetrinn3",
        Double.valueOf(617500), Double.valueOf(13.2)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-12-31")), "skattetrinn4",
        Double.valueOf(964800), Double.valueOf(16.2)));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-01-01"), null), "skattetrinn1",
        Double.valueOf(180800), Double.valueOf(1.9)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-01-01"), null), "skattetrinn2",
        Double.valueOf(254500), Double.valueOf(4.2)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-01-01"), null), "skattetrinn3",
        Double.valueOf(639750), Double.valueOf(13.2)));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2020-01-01"), null), "skattetrinn4",
        Double.valueOf(999550), Double.valueOf(16.2)));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "belopBoutgiftEn", Double.valueOf(9303), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "belopUnderholdEgetEn", Double.valueOf(8657), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "belopBoutgiftGs", Double.valueOf(5698), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2018-07-01"), LocalDate.parse("2019-06-30")),
        "belopUnderholdEgetGs", Double.valueOf(7330), null));

    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "belopBoutgiftEn", Double.valueOf(9591), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "belopUnderholdEgetEn", Double.valueOf(8925), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "belopBoutgiftGs", Double.valueOf(5875), null));
    sjablonPeriodeListe.add(new SjablonPeriodeCore(
        new PeriodeCore(LocalDate.parse("2019-07-01"), null),
        "belopUnderholdEgetGs", Double.valueOf(7557), null));

    return sjablonPeriodeListe;
  }
}

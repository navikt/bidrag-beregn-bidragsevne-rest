package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.TrinnvisSkattesats;
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

  private final static String ENSLIG = "EN";
  private final static String GIFT_SAMBOER = "GS";

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;

  private final Map<String, String> sjablontallMap = new HashMap<>() {{
    put("0001", "OrdinaerBarnetrygdBelop");
    put("0002", "OrdinaerSmabarnstilleggBelop");
    put("0003", "BoutgifterBidragsbarnBelop");
    put("0004", "FordelSkatteklasse2Belop");
    put("0005", "ForskuddssatsBelop");
    put("0006", "InnslagKapitalinntektBelop");
    put("0007", "InntektsintervallTilleggsbidragBelop");
    put("0008", "MaksInntektBPProsent");
    put("0009", "HoyInntektBPMultiplikator");
    put("0010", "InntektBBMultiplikator");
    put("0011", "MaksBidragMultiplikator");
    put("0012", "MaksInntektBBMultiplikator");
    put("0013", "MaksInntektForskuddMottakerMultiplikator");
    put("0014", "NedreInntektsgrenseGebyrBelop");
    put("0015", "SkattAlminneligInntektProsent");
    put("0016", "TilleggsbidragProsent");
    put("0017", "TrygdeavgiftProsent");
    put("0018", "BarnetilleggSkattProsent");
    put("0019", "UnderholdEgneBarnIHusstandBelop");
    put("0020", "EndringBidragGrenseProsent");
    put("0021", "BarnetilleggForsvaretForsteBarnBelop");
    put("0022", "BarnetilleggForsvaretOvrigeBarnBelop");
    put("0023", "MinstefradragInntektBelop");
    put("0024", "GjennomsnittVirkedagerPrManedAntall");
    put("0025", "MinstefradragInntektProsent");
    put("0026", "DagligSatsBarnetilleggBelop");
    put("0027", "PersonfradragKlasse1Belop");
    put("0028", "PersonfradragKlasse2Belop");
    put("0029", "KontantstotteBelop");
    put("0030", "OvreInntektsgrenseIkkeISkatteposisjonBelop");
    put("0031", "NedreInntektsgrenseFullSkatteposisjonBelop");
    put("0032", "EkstraSmabarnstilleggBelop");
    put("0033", "OvreInntektsgrenseFulltForskuddBelop");
    put("0034", "OvreInntektsgrense75ProsentForskuddEnBelop");
    put("0035", "OvreInntektsgrense75ProsentForskuddGSBelop");
    put("0036", "InntektsintervallForskuddBelop");
    put("0037", "OvreGrenseSaertilskuddBelop");
    put("0038", "Forskuddssats75ProsentBelop");
    put("0039", "FordelSaerfradragBelop");
    put("0040", "SkattesatsAlminneligInntektProsent");
    put("0100", "FastsettelsesgebyrBelop");
  }};

  public BeregnBidragsevneService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
  }

  public HttpStatusResponse<BeregnBidragsevneResultat> beregn(BeregnBidragsevneGrunnlagAltCore grunnlagTilCore) {

    // Henter sjabloner for sjablontall
    var sjablonTallResponse = sjablonConsumer.hentSjablonSjablontall();

    if (!(sjablonTallResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (sjablontall). Status: {}", sjablonTallResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (sjablontall). Status: " + sjablonTallResponse.getHttpStatus().toString()
          + " Melding: " + sjablonTallResponse.getBody());
    }

    // Henter sjabloner for bidragsevne
    var sjablonBidragsevneResponse = sjablonConsumer.hentSjablonBidragsevne();

    if (!(sjablonBidragsevneResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (bidragsevne). Status: {}", sjablonBidragsevneResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (bidragsevne). Status: "
          + sjablonBidragsevneResponse.getHttpStatus().toString() + " Melding: " + sjablonBidragsevneResponse.getBody());
    }

    // Henter sjabloner for trinnvis skattesats
    var sjablonTrinnvisSkattesatsResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    if (!(sjablonTrinnvisSkattesatsResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (trinnvis skattesats). Status: {}", sjablonTrinnvisSkattesatsResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (trinnvis skattesats). Status: "
          + sjablonTrinnvisSkattesatsResponse.getHttpStatus().toString() + " Melding: " + sjablonTrinnvisSkattesatsResponse.getBody());
    }

    // Populerer liste over aktuelle sjabloner til core basert på sjablonene som er hentet
    var sjablonPeriodeListe = new ArrayList<SjablonPeriodeCore>();
    sjablonPeriodeListe.addAll(mapSjablonSjablontall(sjablonTallResponse.getBody()));
    sjablonPeriodeListe.addAll(mapSjablonBidragsevne(sjablonBidragsevneResponse.getBody()));
    var sortertSjablonTrinnvisSkattesatsListe = sjablonTrinnvisSkattesatsResponse.getBody().stream().sorted(comparing(TrinnvisSkattesats::getDatoFom)
        .thenComparing(TrinnvisSkattesats::getDatoTom).thenComparing(TrinnvisSkattesats::getInntektgrense)).collect(toList());
    sjablonPeriodeListe.addAll(mapSjablonTrinnvisSkattesats(sortertSjablonTrinnvisSkattesatsListe));
    grunnlagTilCore.setSjablonPeriodeListe(sjablonPeriodeListe);

    // Kaller core-modulen for beregning av bidragsevne
    LOGGER.debug("Bidragsevne - grunnlag for beregning: {}", grunnlagTilCore);
    var resultatFraCore = bidragsevneCore.beregnBidragsevne(grunnlagTilCore);

    if (!resultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av bidragsevne" + System.lineSeparator()
          + "Bidragsevne - grunnlag for beregning: " + grunnlagTilCore + System.lineSeparator()
          + "Bidragsevne - avvik: " + resultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
      throw new UgyldigInputException(resultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Bidragsevne - resultat av beregning: {}", resultatFraCore.getResultatPeriodeListe());
    return new HttpStatusResponse(HttpStatus.OK, new BeregnBidragsevneResultat(resultatFraCore));
  }

  // Mapper sjabloner av typen sjablontall og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonSjablontall(List<Sjablontall> sjablonSjablontallListe) {
    return sjablonSjablontallListe
        .stream()
        .map(sTL -> new SjablonPeriodeCore(new PeriodeCore(sTL.getDatoFom(), sTL.getDatoTom()), sjablontallMap.get(sTL.getTypeSjablon()),
            sTL.getVerdi().doubleValue(), null))
        .collect(toList());
  }

  // Mapper sjabloner av typen bidragsevne og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonBidragsevne(List<Bidragsevne> sjablonBidragsevneListe) {

    var sjablonPeriodeCoreListe = new ArrayList<SjablonPeriodeCore>();
    for (Bidragsevne sjablonBidragsevne : sjablonBidragsevneListe) {
      if (sjablonBidragsevne.getBostatus().equals(ENSLIG)) {
        sjablonPeriodeCoreListe.add(new SjablonPeriodeCore(new PeriodeCore(sjablonBidragsevne.getDatoFom(), sjablonBidragsevne.getDatoTom()),
            "BoutgiftEnBelop", sjablonBidragsevne.getBelopBoutgift().doubleValue(), null));
        sjablonPeriodeCoreListe.add(new SjablonPeriodeCore(new PeriodeCore(sjablonBidragsevne.getDatoFom(), sjablonBidragsevne.getDatoTom()),
            "UnderholdEgetEnBelop", sjablonBidragsevne.getBelopUnderhold().doubleValue(), null));
      }
      if (sjablonBidragsevne.getBostatus().equals(GIFT_SAMBOER)) {
        sjablonPeriodeCoreListe.add(new SjablonPeriodeCore(new PeriodeCore(sjablonBidragsevne.getDatoFom(), sjablonBidragsevne.getDatoTom()),
            "BoutgiftGsBelop", sjablonBidragsevne.getBelopBoutgift().doubleValue(), null));
        sjablonPeriodeCoreListe.add(new SjablonPeriodeCore(new PeriodeCore(sjablonBidragsevne.getDatoFom(), sjablonBidragsevne.getDatoTom()),
            "UnderholdEgetGsBelop", sjablonBidragsevne.getBelopUnderhold().doubleValue(), null));
      }
    }
    return sjablonPeriodeCoreListe;
  }

  // Mapper sjabloner av typen trinnvis skattesats og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonTrinnvisSkattesats(List<TrinnvisSkattesats> sjablonTrinnvisSkattesatsListe) {

    var sjablonPeriodeCoreListe = new ArrayList<SjablonPeriodeCore>();
    var datoFom = LocalDate.MIN;
    var datoTom = LocalDate.MIN;
    var indeks = 0;
    for (TrinnvisSkattesats sjablonTrinnvisSkattesats : sjablonTrinnvisSkattesatsListe) {
      if (!(sjablonTrinnvisSkattesats.getDatoFom().equals(datoFom) && sjablonTrinnvisSkattesats.getDatoTom().equals(datoTom))) {
        indeks = 0;
        datoFom = sjablonTrinnvisSkattesats.getDatoFom();
        datoTom = sjablonTrinnvisSkattesats.getDatoTom();
      }
      indeks++;
      sjablonPeriodeCoreListe
          .add(new SjablonPeriodeCore(new PeriodeCore(sjablonTrinnvisSkattesats.getDatoFom(), sjablonTrinnvisSkattesats.getDatoTom()),
              "Skattetrinn" + indeks, sjablonTrinnvisSkattesats.getInntektgrense().doubleValue(), sjablonTrinnvisSkattesats.getSats().doubleValue()));
    }
    return sjablonPeriodeCoreListe;
  }
}

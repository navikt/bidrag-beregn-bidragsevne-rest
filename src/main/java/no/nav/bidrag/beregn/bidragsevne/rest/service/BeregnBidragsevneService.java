package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonInnholdCoreNy;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonNokkelCoreNy;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonPeriodeCoreNy;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNoekkelNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
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
    put("0001", SjablonTallNavn.ORDINAER_BARNETRYGD_BELOEP.getNavn());
    put("0002", SjablonTallNavn.ORDINAER_SMAABARNSTILLEGG_BELOP.getNavn());
    put("0003", SjablonTallNavn.BOUTGIFTER_BIDRAGSBARN_BELOEP.getNavn());
    put("0004", SjablonTallNavn.FORDEL_SKATTEKLASSE2_BELOEP.getNavn());
    put("0005", SjablonTallNavn.FORSKUDDSSATS_BELOEP.getNavn());
    put("0006", SjablonTallNavn.INNSLAG_KAPITALINNTEKT_BELOEP.getNavn());
    put("0007", SjablonTallNavn.INNTEKTSINTERVALL_TILLEGGSBIDRAG_BELOEP.getNavn());
    put("0008", SjablonTallNavn.MAKS_INNTEKT_BP_PROSENT.getNavn());
    put("0009", SjablonTallNavn.HOEY_INNTEKT_BP_MULTIPLIKATOR.getNavn());
    put("0010", SjablonTallNavn.INNTEKT_BB_MULTIPLIKATOR.getNavn());
    put("0011", SjablonTallNavn.MAKS_BIDRAG_MULTIPLIKATOR.getNavn());
    put("0012", SjablonTallNavn.MAKS_INNTEKT_BB_MULTIPLIKATOR.getNavn());
    put("0013", SjablonTallNavn.MAKS_INNTEKT_FORSKUDD_MOTTAKER_MULTIPLIKATOR.getNavn());
    put("0014", SjablonTallNavn.NEDRE_INNTEKTSGRENSE_GEBYR_BELOP.getNavn());
    put("0015", SjablonTallNavn.SKATT_ALMINNELIG_INNTEKT_PROSENT.getNavn());
    put("0016", SjablonTallNavn.TILLEGGSBIDRAG_PROSENT.getNavn());
    put("0017", SjablonTallNavn.TRYGDEAVGIFT_PROSENT.getNavn());
    put("0018", SjablonTallNavn.BARNETILLEGG_SKATT_PROSENT.getNavn());
    put("0019", SjablonTallNavn.UNDERHOLD_EGNE_BARN_I_HUSSTAND_BELOEP.getNavn());
    put("0020", SjablonTallNavn.ENDRING_BIDRAG_GRENSE_PROSENT.getNavn());
    put("0021", SjablonTallNavn.BARNETILLEGG_FORSVARET_FOERSTE_BARN_BELOEP.getNavn());
    put("0022", SjablonTallNavn.BARNETILLEGG_FORSVARET_OEVRIGE_BARN_BELOEP.getNavn());
    put("0023", SjablonTallNavn.MINSTEFRADRAG_INNTEKT_BELOEP.getNavn());
    put("0024", SjablonTallNavn.GJENNOMSNITT_VIRKEDAGER_PR_MAANED_ANTALL.getNavn());
    put("0025", SjablonTallNavn.MINSTEFRADRAG_INNTEKT_PROSENT.getNavn());
    put("0026", SjablonTallNavn.DAGLIG_SATS_BARNETILLEGG_BELOEP.getNavn());
    put("0027", SjablonTallNavn.PERSONFRADRAG_KLASSE1_BELOEP.getNavn());
    put("0028", SjablonTallNavn.PERSONFRADRAG_KLASSE2_BELOEP.getNavn());
    put("0029", SjablonTallNavn.KONTANTSTOETTE_BELOEP.getNavn());
    put("0030", SjablonTallNavn.OEVRE_INNTEKTSGRENSE_IKKE_I_SKATTEPOSISJON_BELOEP.getNavn());
    put("0031", SjablonTallNavn.NEDRE_INNTEKTSGRENSE_FULL_SKATTEPOSISJON_BELOEP.getNavn());
    put("0032", SjablonTallNavn.EKSTRA_SMAABARNSTILLEGG_BELOEP.getNavn());
    put("0033", SjablonTallNavn.OEVRE_INNTEKTSGRENSE_FULLT_FORSKUDD_BELOEP.getNavn());
    put("0034", SjablonTallNavn.OEVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_EN_BELOEP.getNavn());
    put("0035", SjablonTallNavn.OEVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_GS_BELOEP.getNavn());
    put("0036", SjablonTallNavn.INNTEKTSINTERVALL_FORSKUDD_BELOEP.getNavn());
    put("0037", SjablonTallNavn.OEVRE_GRENSE_SAERTILSKUDD_BELOEP.getNavn());
    put("0038", SjablonTallNavn.FORSKUDDSSATS_75PROSENT_BELOEP.getNavn());
    put("0039", SjablonTallNavn.FORDEL_SAERFRADRAG_BELOEP.getNavn());
    put("0040", SjablonTallNavn.SKATTESATS_ALMINNELIG_INNTEKT_PROSENT.getNavn());
    put("0100", SjablonTallNavn.FASTSETTELSESGEBYR_BELOEP.getNavn());
  }};

  public BeregnBidragsevneService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
  }

  public HttpStatusResponse<BeregnBidragsevneResultat> beregn(BeregnBidragsevneGrunnlagAltCore grunnlagTilCore) {

    // Henter sjabloner for sjablontall
    var sjablonSjablontallResponse = sjablonConsumer.hentSjablonSjablontall();

    if (!(sjablonSjablontallResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (sjablontall). Status: {}", sjablonSjablontallResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (sjablontall). Status: " + sjablonSjablontallResponse.getHttpStatus().toString()
          + " Melding: " + sjablonSjablontallResponse.getBody());
    } else {
      LOGGER.debug("Antall sjabloner hentet av type Sjablontall: {}", sjablonSjablontallResponse.getBody().size());
    }

    // Henter sjabloner for bidragsevne
    var sjablonBidragsevneResponse = sjablonConsumer.hentSjablonBidragsevne();

    if (!(sjablonBidragsevneResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (bidragsevne). Status: {}", sjablonBidragsevneResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (bidragsevne). Status: "
          + sjablonBidragsevneResponse.getHttpStatus().toString() + " Melding: " + sjablonBidragsevneResponse.getBody());
    } else {
      LOGGER.debug("Antall sjabloner hentet av type Bidragsevne: {}", sjablonBidragsevneResponse.getBody().size());
    }

    // Henter sjabloner for trinnvis skattesats
    var sjablonTrinnvisSkattesatsResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    if (!(sjablonTrinnvisSkattesatsResponse.getHttpStatus().is2xxSuccessful())) {
      LOGGER.error("Feil ved kall av bidrag-sjablon (trinnvis skattesats). Status: {}", sjablonTrinnvisSkattesatsResponse.getHttpStatus().toString());
      throw new SjablonConsumerException("Feil ved kall av bidrag-sjablon (trinnvis skattesats). Status: "
          + sjablonTrinnvisSkattesatsResponse.getHttpStatus().toString() + " Melding: " + sjablonTrinnvisSkattesatsResponse.getBody());
    } else {
      LOGGER.debug("Antall sjabloner hentet av type TrinnvisSkattesats: {}", sjablonTrinnvisSkattesatsResponse.getBody().size());
    }

    // Populerer liste over aktuelle sjabloner til core basert på sjablonene som er hentet
    var sjablonPeriodeListe = new ArrayList<SjablonPeriodeCore>();
    sjablonPeriodeListe.addAll(mapSjablonSjablontall(sjablonSjablontallResponse.getBody()));
    sjablonPeriodeListe.addAll(mapSjablonBidragsevne(sjablonBidragsevneResponse.getBody()));
    var sortertSjablonTrinnvisSkattesatsListe = sjablonTrinnvisSkattesatsResponse.getBody().stream().sorted(comparing(TrinnvisSkattesats::getDatoFom)
        .thenComparing(TrinnvisSkattesats::getDatoTom).thenComparing(TrinnvisSkattesats::getInntektgrense)).collect(toList());
    sjablonPeriodeListe.addAll(mapSjablonTrinnvisSkattesats(sortertSjablonTrinnvisSkattesatsListe));
    grunnlagTilCore.setSjablonPeriodeListe(sjablonPeriodeListe);

    // Populerer liste over aktuelle sjabloner til core basert på sjablonene som er hentet (ny struktur)
    var sjablonPeriodeListeNy = new ArrayList<SjablonPeriodeCoreNy>();
    sjablonPeriodeListeNy.addAll(mapSjablonSjablontallNy(sjablonSjablontallResponse.getBody()));
    sjablonPeriodeListeNy.addAll(mapSjablonBidragsevneNy(sjablonBidragsevneResponse.getBody()));
    sjablonPeriodeListeNy.addAll(mapSjablonTrinnvisSkattesatsNy(sjablonTrinnvisSkattesatsResponse.getBody()));
    grunnlagTilCore.setSjablonPeriodeListeNy(sjablonPeriodeListeNy);

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

  // Mapper sjabloner av typen sjablontall og flytter inn i inputen til core-modulen (ny struktur)
  private List<SjablonPeriodeCoreNy> mapSjablonSjablontallNy(List<Sjablontall> sjablonSjablontallListe) {
    return sjablonSjablontallListe
        .stream()
        .map(sTL -> new SjablonPeriodeCoreNy(
            new PeriodeCore(sTL.getDatoFom(), sTL.getDatoTom()),
            sjablontallMap.get(sTL.getTypeSjablon()),
            emptyList(),
            Arrays.asList(new SjablonInnholdCoreNy(SjablonInnholdNavn.SJABLON_VERDI.getNavn(), sTL.getVerdi().doubleValue()))))
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

  // Mapper sjabloner av typen bidragsevne og flytter inn i inputen til core-modulen (ny struktur)
  private List<SjablonPeriodeCoreNy> mapSjablonBidragsevneNy(List<Bidragsevne> sjablonBidragsevneListe) {

    return sjablonBidragsevneListe
        .stream()
        .map(sBL -> new SjablonPeriodeCoreNy(
            new PeriodeCore(sBL.getDatoFom(), sBL.getDatoTom()),
            SjablonNavn.BIDRAGSEVNE.getNavn(),
            Arrays.asList(new SjablonNokkelCoreNy(SjablonNoekkelNavn.BOSTATUS.getNavn(), sBL.getBostatus())),
            Arrays.asList(new SjablonInnholdCoreNy(SjablonInnholdNavn.BOUTGIFT_BELOEP.getNavn(), sBL.getBelopBoutgift().doubleValue()),
                new SjablonInnholdCoreNy(SjablonInnholdNavn.UNDERHOLD_BELOEP.getNavn(), sBL.getBelopUnderhold().doubleValue()))))
        .collect(toList());
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

  // Mapper sjabloner av typen trinnvis skattesats og flytter inn i inputen til core-modulen (ny struktur)
  private List<SjablonPeriodeCoreNy> mapSjablonTrinnvisSkattesatsNy(List<TrinnvisSkattesats> sjablonTrinnvisSkattesatsListe) {

    return sjablonTrinnvisSkattesatsListe
        .stream()
        .map(sTSL -> new SjablonPeriodeCoreNy(
            new PeriodeCore(sTSL.getDatoFom(), sTSL.getDatoTom()),
            SjablonNavn.TRINNVIS_SKATTESATS.getNavn(),
            emptyList(),
            Arrays.asList(new SjablonInnholdCoreNy(SjablonInnholdNavn.INNTEKTSGRENSE_BELOEP.getNavn(), sTSL.getInntektgrense().doubleValue()),
                new SjablonInnholdCoreNy(SjablonInnholdNavn.SKATTESATS_PROSENT.getNavn(), sTSL.getSats().doubleValue()))))
        .collect(toList());
  }
}

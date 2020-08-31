package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

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
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.felles.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
import no.nav.bidrag.beregn.felles.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BeregnBidragsevneService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeregnBidragsevneService.class);

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;

  private final Map<String, String> sjablontallMap = new HashMap<>() {{
    put("0001", SjablonTallNavn.ORDINAER_BARNETRYGD_BELOP.getNavn());
    put("0002", SjablonTallNavn.ORDINAER_SMAABARNSTILLEGG_BELOP.getNavn());
    put("0003", SjablonTallNavn.BOUTGIFTER_BIDRAGSBARN_BELOP.getNavn());
    put("0004", SjablonTallNavn.FORDEL_SKATTEKLASSE2_BELOP.getNavn());
    put("0005", SjablonTallNavn.FORSKUDDSSATS_BELOP.getNavn());
    put("0006", SjablonTallNavn.INNSLAG_KAPITALINNTEKT_BELOP.getNavn());
    put("0007", SjablonTallNavn.INNTEKTSINTERVALL_TILLEGGSBIDRAG_BELOP.getNavn());
    put("0008", SjablonTallNavn.MAKS_INNTEKT_BP_PROSENT.getNavn());
    put("0009", SjablonTallNavn.HOY_INNTEKT_BP_MULTIPLIKATOR.getNavn());
    put("0010", SjablonTallNavn.INNTEKT_BB_MULTIPLIKATOR.getNavn());
    put("0011", SjablonTallNavn.MAKS_BIDRAG_MULTIPLIKATOR.getNavn());
    put("0012", SjablonTallNavn.MAKS_INNTEKT_BB_MULTIPLIKATOR.getNavn());
    put("0013", SjablonTallNavn.MAKS_INNTEKT_FORSKUDD_MOTTAKER_MULTIPLIKATOR.getNavn());
    put("0014", SjablonTallNavn.NEDRE_INNTEKTSGRENSE_GEBYR_BELOP.getNavn());
    put("0015", SjablonTallNavn.SKATT_ALMINNELIG_INNTEKT_PROSENT.getNavn());
    put("0016", SjablonTallNavn.TILLEGGSBIDRAG_PROSENT.getNavn());
    put("0017", SjablonTallNavn.TRYGDEAVGIFT_PROSENT.getNavn());
    put("0018", SjablonTallNavn.BARNETILLEGG_SKATT_PROSENT.getNavn());
    put("0019", SjablonTallNavn.UNDERHOLD_EGNE_BARN_I_HUSSTAND_BELOP.getNavn());
    put("0020", SjablonTallNavn.ENDRING_BIDRAG_GRENSE_PROSENT.getNavn());
    put("0021", SjablonTallNavn.BARNETILLEGG_FORSVARET_FORSTE_BARN_BELOP.getNavn());
    put("0022", SjablonTallNavn.BARNETILLEGG_FORSVARET_OVRIGE_BARN_BELOP.getNavn());
    put("0023", SjablonTallNavn.MINSTEFRADRAG_INNTEKT_BELOP.getNavn());
    put("0024", SjablonTallNavn.GJENNOMSNITT_VIRKEDAGER_PR_MAANED_ANTALL.getNavn());
    put("0025", SjablonTallNavn.MINSTEFRADRAG_INNTEKT_PROSENT.getNavn());
    put("0026", SjablonTallNavn.DAGLIG_SATS_BARNETILLEGG_BELOP.getNavn());
    put("0027", SjablonTallNavn.PERSONFRADRAG_KLASSE1_BELOP.getNavn());
    put("0028", SjablonTallNavn.PERSONFRADRAG_KLASSE2_BELOP.getNavn());
    put("0029", SjablonTallNavn.KONTANTSTOTTE_BELOP.getNavn());
    put("0030", SjablonTallNavn.OVRE_INNTEKTSGRENSE_IKKE_I_SKATTEPOSISJON_BELOP.getNavn());
    put("0031", SjablonTallNavn.NEDRE_INNTEKTSGRENSE_FULL_SKATTEPOSISJON_BELOP.getNavn());
    put("0032", SjablonTallNavn.EKSTRA_SMAABARNSTILLEGG_BELOP.getNavn());
    put("0033", SjablonTallNavn.OVRE_INNTEKTSGRENSE_FULLT_FORSKUDD_BELOP.getNavn());
    put("0034", SjablonTallNavn.OVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_EN_BELOP.getNavn());
    put("0035", SjablonTallNavn.OVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_GS_BELOP.getNavn());
    put("0036", SjablonTallNavn.INNTEKTSINTERVALL_FORSKUDD_BELOP.getNavn());
    put("0037", SjablonTallNavn.OVRE_GRENSE_SAERTILSKUDD_BELOP.getNavn());
    put("0038", SjablonTallNavn.FORSKUDDSSATS_75PROSENT_BELOP.getNavn());
    put("0039", SjablonTallNavn.FORDEL_SAERFRADRAG_BELOP.getNavn());
    put("0040", SjablonTallNavn.SKATTESATS_ALMINNELIG_INNTEKT_PROSENT.getNavn());
    put("0041", SjablonTallNavn.FORHOYET_BARNETRYGD_BELOP.getNavn());
    put("0100", SjablonTallNavn.FASTSETTELSESGEBYR_BELOP.getNavn());
  }};

  public BeregnBidragsevneService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
  }

  public HttpResponse<BeregnBidragsevneResultat> beregn(BeregnBidragsevneGrunnlagAltCore grunnlagTilCore) {

    // Henter sjabloner for sjablontall
    var sjablonSjablontallResponse = sjablonConsumer.hentSjablonSjablontall();
    LOGGER.debug("Antall sjabloner hentet av type Sjablontall: {}", sjablonSjablontallResponse.getResponseEntity().getBody().size());

    // Henter sjabloner for bidragsevne
    var sjablonBidragsevneResponse = sjablonConsumer.hentSjablonBidragsevne();
    LOGGER.debug("Antall sjabloner hentet av type Bidragsevne: {}", sjablonBidragsevneResponse.getResponseEntity().getBody().size());

    // Henter sjabloner for trinnvis skattesats
    var sjablonTrinnvisSkattesatsResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();
    LOGGER.debug("Antall sjabloner hentet av type TrinnvisSkattesats: {}", sjablonTrinnvisSkattesatsResponse.getResponseEntity().getBody().size());

    // Populerer liste over aktuelle sjabloner til core basert på sjablonene som er hentet
    var sjablonPeriodeListe = new ArrayList<SjablonPeriodeCore>();
    sjablonPeriodeListe.addAll(mapSjablonSjablontall(sjablonSjablontallResponse.getResponseEntity().getBody()));
    sjablonPeriodeListe.addAll(mapSjablonBidragsevne(sjablonBidragsevneResponse.getResponseEntity().getBody()));
    sjablonPeriodeListe.addAll(mapSjablonTrinnvisSkattesats(sjablonTrinnvisSkattesatsResponse.getResponseEntity().getBody()));
    grunnlagTilCore.setSjablonPeriodeListe(sjablonPeriodeListe);

    // Kaller core-modulen for beregning av bidragsevne
    LOGGER.debug("Bidragsevne - grunnlag for beregning: {}", grunnlagTilCore);
    var resultatFraCore = bidragsevneCore.beregnBidragsevne(grunnlagTilCore);

    if (!resultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: " + System.lineSeparator()
          + resultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("Bidragsevne - grunnlag for beregning:" + System.lineSeparator()
          + "beregnDatoFra= " + grunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + grunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "antallBarnIEgetHusholdPeriodeListe= " + grunnlagTilCore.getAntallBarnIEgetHusholdPeriodeListe() + System.lineSeparator()
          + "bostatusPeriodeListe= " + grunnlagTilCore.getBostatusPeriodeListe() + System.lineSeparator()
          + "inntektPeriodeListe= " + grunnlagTilCore.getInntektPeriodeListe() + System.lineSeparator()
          + "særfradragPeriodeListe= " + grunnlagTilCore.getSaerfradragPeriodeListe() + System.lineSeparator()
          + "skatteklassePeriodeListe= " + grunnlagTilCore.getSkatteklassePeriodeListe());
      throw new UgyldigInputException("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: "
          + resultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Bidragsevne - resultat av beregning: {}", resultatFraCore.getResultatPeriodeListe());
    return HttpResponse.from(HttpStatus.OK, new BeregnBidragsevneResultat(resultatFraCore));
  }

  // Mapper sjabloner av typen sjablontall og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonSjablontall(List<Sjablontall> sjablonSjablontallListe) {
    return sjablonSjablontallListe
        .stream()
        .map(sTL -> new SjablonPeriodeCore(
            new PeriodeCore(sTL.getDatoFom(), sTL.getDatoTom()),
            sjablontallMap.getOrDefault(sTL.getTypeSjablon(), sTL.getTypeSjablon()),
            emptyList(),
            singletonList(new SjablonInnholdCore(SjablonInnholdNavn.SJABLON_VERDI.getNavn(), sTL.getVerdi().doubleValue()))))
        .collect(toList());
  }

  // Mapper sjabloner av typen bidragsevne og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonBidragsevne(List<Bidragsevne> sjablonBidragsevneListe) {

    return sjablonBidragsevneListe
        .stream()
        .map(sBL -> new SjablonPeriodeCore(
            new PeriodeCore(sBL.getDatoFom(), sBL.getDatoTom()),
            SjablonNavn.BIDRAGSEVNE.getNavn(),
            singletonList(new SjablonNokkelCore(SjablonNokkelNavn.BOSTATUS.getNavn(), sBL.getBostatus())),
            Arrays.asList(new SjablonInnholdCore(SjablonInnholdNavn.BOUTGIFT_BELOP.getNavn(), sBL.getBelopBoutgift().doubleValue()),
                new SjablonInnholdCore(SjablonInnholdNavn.UNDERHOLD_BELOP.getNavn(), sBL.getBelopUnderhold().doubleValue()))))
        .collect(toList());
  }

  // Mapper sjabloner av typen trinnvis skattesats og flytter inn i inputen til core-modulen
  private List<SjablonPeriodeCore> mapSjablonTrinnvisSkattesats(List<TrinnvisSkattesats> sjablonTrinnvisSkattesatsListe) {

    return sjablonTrinnvisSkattesatsListe
        .stream()
        .map(sTSL -> new SjablonPeriodeCore(
            new PeriodeCore(sTSL.getDatoFom(), sTSL.getDatoTom()),
            SjablonNavn.TRINNVIS_SKATTESATS.getNavn(),
            emptyList(),
            Arrays.asList(new SjablonInnholdCore(SjablonInnholdNavn.INNTEKTSGRENSE_BELOP.getNavn(), sTSL.getInntektgrense().doubleValue()),
                new SjablonInnholdCore(SjablonInnholdNavn.SKATTESATS_PROSENT.getNavn(), sTSL.getSats().doubleValue()))))
        .collect(toList());
  }
}

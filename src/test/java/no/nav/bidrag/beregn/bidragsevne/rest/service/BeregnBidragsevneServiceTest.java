package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.felles.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.commons.web.HttpStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

@DisplayName("BeregnBidragsevneServiceTest")
class BeregnBidragsevneServiceTest {

  @InjectMocks
  private BeregnBidragsevneService beregnBidragsevneService;

  @Mock
  private SjablonConsumer sjablonConsumerMock;
  @Mock
  private BidragsevneCore bidragsevneCoreMock;

  @BeforeEach
  void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("Skal beregne bidragsevne når retur fra SjablonConsumer og BidragsevneCore er OK")
  void skalBeregneBidragsevneNårReturFraSjablonConsumerOgBidragsevneCoreErOk() {
    when(sjablonConsumerMock.hentSjablonSjablontall()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()));
    when(sjablonConsumerMock.hentSjablonBidragsevne()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonBidragsevneListe()));
    when(sjablonConsumerMock.hentSjablonTrinnvisSkattesats())
        .thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonTrinnvisSkattesatsListe()));
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());

    var beregnBidragsevneResultat = beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore());

    assertAll(
        () -> assertThat(beregnBidragsevneResultat.getHttpStatus()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnBidragsevneResultat.getBody()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe().size()).isEqualTo(1)
    );
  }

  @Test
  @DisplayName("Skal ha korrekt sjablon-grunnlag når beregningsmodulen kalles")
  void skalHaKorrektSjablonGrunnlagNårBeregningsmodulenKalles() {
    var grunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnBidragsevneGrunnlagAltCore.class);
    when(sjablonConsumerMock.hentSjablonSjablontall()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()));
    when(sjablonConsumerMock.hentSjablonBidragsevne()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonBidragsevneListe()));
    when(sjablonConsumerMock.hentSjablonTrinnvisSkattesats())
        .thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonTrinnvisSkattesatsListe()));
    when(bidragsevneCoreMock.beregnBidragsevne(grunnlagTilCoreCaptor.capture())).thenReturn(TestUtil.dummyBidragsevneResultatCore());

    var beregnBidragsevneResultat = beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore());
    var grunnlagTilCore = grunnlagTilCoreCaptor.getValue();

    var forventetAntallSjablonElementer = TestUtil.dummySjablonSjablontallListe().size() + TestUtil.dummySjablonBidragsevneListe().size()
        + TestUtil.dummySjablonTrinnvisSkattesatsListe().size();

    assertAll(
        () -> assertThat(beregnBidragsevneResultat.getHttpStatus()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnBidragsevneResultat.getBody()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().size()).isEqualTo(forventetAntallSjablonElementer),

        // Sjekk at det mappes ut riktig antall for en gitt sjablon av type Sjablontall
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getSjablonNavn().equals(SjablonTallNavn.MINSTEFRADRAG_INNTEKT_PROSENT.getNavn())).count())
            .isEqualTo(TestUtil.dummySjablonSjablontallListe().stream()
                .filter(sjablontall -> sjablontall.getTypeSjablon().equals("0025")).count()),

        // Sjekk at det mappes ut riktig antall sjabloner av type Bidragsevne
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getSjablonNavn().equals(SjablonNavn.BIDRAGSEVNE.getNavn())).count())
            .isEqualTo(TestUtil.dummySjablonBidragsevneListe().size()),

        // Sjekk at det mappes ut riktig antall sjabloner av type Trinnvis Skattesats
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getSjablonNavn().equals(SjablonNavn.TRINNVIS_SKATTESATS.getNavn())).count())
            .isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().size()),

        // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type Sjablontall
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> (sjablonPeriodeCore.getSjablonNavn().equals(SjablonTallNavn.PERSONFRADRAG_KLASSE2_BELOP.getNavn())) &&
                (sjablonPeriodeCore.getSjablonPeriodeDatoFraTil().getPeriodeDatoFra().equals(LocalDate.parse("2017-07-01"))))
            .map(SjablonPeriodeCore::getSjablonInnholdListe)
            .flatMap(Collection::stream)
            .findFirst()
            .map(SjablonInnholdCore::getSjablonInnholdVerdi)
            .orElse(0d))
            .isEqualTo(57000d),

        // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type Bidragsevne
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> (sjablonPeriodeCore.getSjablonNavn().equals(SjablonNavn.BIDRAGSEVNE.getNavn())) &&
                (sjablonPeriodeCore.getSjablonPeriodeDatoFraTil().getPeriodeDatoFra().equals(LocalDate.parse("2017-07-01"))))
            .map(SjablonPeriodeCore::getSjablonInnholdListe)
            .flatMap(Collection::stream)
            .findFirst()
            .map(SjablonInnholdCore::getSjablonInnholdVerdi)
            .orElse(0d))
            .isEqualTo(10000d),

        // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type TrinnvisSkattesats
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> (sjablonPeriodeCore.getSjablonNavn().equals(SjablonNavn.TRINNVIS_SKATTESATS.getNavn())) &&
                (sjablonPeriodeCore.getSjablonPeriodeDatoFraTil().getPeriodeDatoFra().equals(LocalDate.parse("2017-07-01"))))
            .map(SjablonPeriodeCore::getSjablonInnholdListe)
            .flatMap(Collection::stream)
            .findFirst()
            .map(SjablonInnholdCore::getSjablonInnholdVerdi)
            .orElse(0d))
            .isEqualTo(180800d));
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException ved feil retur fra SjablonConsumer")
  void skalKasteSjablonConsumerExceptionVedFeilReturFraSjablonConsumer() {
    Map<String, String> body = new HashMap<>();
    body.put("error code", "503");
    body.put("error msg", "SERVICE_UNAVAILABLE");
    body.put("error text", "Service utilgjengelig");
    when(sjablonConsumerMock.hentSjablonSjablontall()).thenReturn(new HttpStatusResponse(HttpStatus.SERVICE_UNAVAILABLE, body.toString()));

    assertThatExceptionOfType(SjablonConsumerException.class)
        .isThrownBy(() -> beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore()))
        .withMessageContaining("Feil ved kall av bidrag-sjablon (sjablontall). Status: " + HttpStatus.SERVICE_UNAVAILABLE + " Melding: ");
  }


  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BidragsevneCore")
  void skalKasteUgyldigInputExceptionVedFeilReturFraBidragsevneCore() {
    when(sjablonConsumerMock.hentSjablonSjablontall()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()));
    when(sjablonConsumerMock.hentSjablonBidragsevne()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonBidragsevneListe()));
    when(sjablonConsumerMock.hentSjablonTrinnvisSkattesats())
        .thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonTrinnvisSkattesatsListe()));
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore()))
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }
}

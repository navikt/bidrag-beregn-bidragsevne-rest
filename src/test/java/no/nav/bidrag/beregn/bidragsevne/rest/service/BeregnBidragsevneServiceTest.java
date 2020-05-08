package no.nav.bidrag.beregn.bidragsevne.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.felles.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
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
    var grunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnBidragsevneGrunnlagAltCore.class);
    when(sjablonConsumerMock.hentSjablontall()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonListe()));
    when(bidragsevneCoreMock.beregnBidragsevne(grunnlagTilCoreCaptor.capture())).thenReturn(TestUtil.dummyBidragsevneResultatCore());

    var beregnBidragsevneResultat = beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore());
    var grunnlagTilCore = grunnlagTilCoreCaptor.getValue();

    assertAll(
        () -> assertThat(beregnBidragsevneResultat.getHttpStatus()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnBidragsevneResultat.getBody()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(beregnBidragsevneResultat.getBody().getResultatPeriodeListe().size()).isEqualTo(1),
//        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().size()).isEqualTo(TestUtil.dummySjablonListe().size())
        () -> assertThat(grunnlagTilCore.getSjablonPeriodeListe().size()).isEqualTo(40)
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException ved feil retur fra SjablonConsumer")
  void SkalKasteSjablonConsumerExceptionVedFeilReturFraSjablonConsumer() {
    Map<String, String> body = new HashMap<>();
    body.put("error code", "503");
    body.put("error msg", "SERVICE_UNAVAILABLE");
    body.put("error text", "Service utilgjengelig");
    when(sjablonConsumerMock.hentSjablontall()).thenReturn(new HttpStatusResponse(HttpStatus.SERVICE_UNAVAILABLE, body.toString()));

    assertThatExceptionOfType(SjablonConsumerException.class)
        .isThrownBy(() -> beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore()))
        .withMessageContaining("Feil ved kall av bidrag-sjablon. Status: " + HttpStatus.SERVICE_UNAVAILABLE + " Melding: ");
  }


  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BidragsevneCore")
  void SkalKasteUgyldigInputExceptionVedFeilReturFraBidragsevneCore() {
    when(sjablonConsumerMock.hentSjablontall()).thenReturn(new HttpStatusResponse<>(HttpStatus.OK, TestUtil.dummySjablonListe()));
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnBidragsevneService.beregn(TestUtil.dummyBidragsevneGrunnlagCore()))
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }
}
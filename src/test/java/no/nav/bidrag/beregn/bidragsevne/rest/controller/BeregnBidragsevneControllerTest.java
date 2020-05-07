package no.nav.bidrag.beregn.bidragsevne.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDate;
import no.nav.bidrag.beregn.bidragsevne.rest.BidragBeregnBidragsevneLocal;
import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneResultat;
import no.nav.bidrag.beregn.bidragsevne.rest.service.BeregnBidragsevneService;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
import no.nav.bidrag.commons.web.HttpStatusResponse;
import no.nav.bidrag.commons.web.test.HttpHeaderTestRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@DisplayName("BeregnBidragsevneControllerTest")
@SpringBootTest(classes = BidragBeregnBidragsevneLocal.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class BeregnBidragsevneControllerTest {

  @Autowired
  private HttpHeaderTestRestTemplate httpHeaderTestRestTemplate;
  @LocalServerPort
  private int port;
  @MockBean
  private BeregnBidragsevneService beregnBidragsevneServiceMock;

  @BeforeEach
  void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName("Skal returnere bidragsevne resultat")
  void skalReturnereBidragsevneResultat() {

    when(beregnBidragsevneServiceMock.beregn(any(BeregnBidragsevneGrunnlagAltCore.class)))
        .thenReturn(new HttpStatusResponse(OK, TestUtil.dummyBidragsevneResultat()));

    var url = "http://localhost:" + port + "/bidrag-beregn-bidragsevne-rest/beregn/bidragsevne";
    var request = initHttpEntity(TestUtil.byggBidragsevneGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnBidragsevneResultat.class);
    var bidragsevneResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(bidragsevneResultat).isNotNull(),
        () -> assertThat(bidragsevneResultat.getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(bidragsevneResultat.getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(bidragsevneResultat.getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoFra())
            .isEqualTo(LocalDate.parse("2017-01-01")),
        () -> assertThat(bidragsevneResultat.getResultatPeriodeListe().get(0).getResultatDatoFraTil().getPeriodeDatoTil())
            .isEqualTo(LocalDate.parse("2019-01-01")),
        () -> assertThat(bidragsevneResultat.getResultatPeriodeListe().get(0).getResultatBeregning().getResultatEvne()).isEqualTo(100d)
    );
  }

  @Test
  @DisplayName("Skal returnere 400 Bad Request")
  void skalReturnere400BadRequest() {

    var url = "http://localhost:" + port + "/bidrag-beregn-bidragsevne-rest/beregn/bidragsevne";
    var request = initHttpEntity(TestUtil.byggBidragsevneGrunnlagUtenBostatusKode());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnBidragsevneResultat.class);

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST)
    );
  }

  @Test
  @DisplayName("Skal returnere 500 Internal Server Error")
  void skalReturnere500InternalServerError() {

    when(beregnBidragsevneServiceMock.beregn(any(BeregnBidragsevneGrunnlagAltCore.class)))
        .thenReturn(new HttpStatusResponse(INTERNAL_SERVER_ERROR, null));

    var url = "http://localhost:" + port + "/bidrag-beregn-bidragsevne-rest/beregn/bidragsevne";
    var request = initHttpEntity(TestUtil.byggBidragsevneGrunnlag());
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnBidragsevneResultat.class);
    var bidragsevneResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR),
        () -> assertThat(bidragsevneResultat).isNull()
    );
  }

  private <T> HttpEntity<T> initHttpEntity(T body) {
    var httpHeaders = new HttpHeaders();
    return new HttpEntity<>(body, httpHeaders);
  }
}

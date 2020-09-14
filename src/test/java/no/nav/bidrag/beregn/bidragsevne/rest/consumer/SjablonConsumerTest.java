package no.nav.bidrag.beregn.bidragsevne.rest.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.rest.BidragBeregnBidragsevne;
import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("unchecked")
@SpringBootTest(classes = BidragBeregnBidragsevne.class, webEnvironment = WebEnvironment.NONE)
@TestPropertySource(properties = {"NAV_API_KEY=test.key"})
@DisplayName("SjablonConsumerTest")
class SjablonConsumerTest {

  @Autowired
  private SjablonConsumer sjablonConsumer;

  @MockBean
  private RestTemplate restTemplateMock;
  @MockBean
  private TestRestTemplate testRestTemplate;

  @Test
  @DisplayName("Skal hente liste av Sjablontall når respons fra tjenesten er OK")
  void skalHenteListeAvSjablontallNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonSjablontallListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonSjablontall();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonSjablontallListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getTypeSjablon())
            .isEqualTo(TestUtil.dummySjablonSjablontallListe().get(0).getTypeSjablon())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for Sjablontall")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForSjablontall() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(SjablonConsumerException.class).isThrownBy(() -> sjablonConsumer.hentSjablonSjablontall());
  }

  @Test
  @DisplayName("Skal hente liste av Bidragsevne-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvBidragsevneSjablonerNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonBidragsevneListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonBidragsevne();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonBidragsevneListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getBelopBoutgift())
            .isEqualTo(TestUtil.dummySjablonBidragsevneListe().get(0).getBelopBoutgift())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for Bidragsevne")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForBidragsevne() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(SjablonConsumerException.class).isThrownBy(() -> sjablonConsumer.hentSjablonBidragsevne());
  }

  @Test
  @DisplayName("Skal hente liste av TrinnvisSkattesats-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvTrinnvisSkattesatsSjablonerNaarResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonTrinnvisSkattesatsListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().size()).isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().size()),
        () -> assertThat(sjablonResponse.getResponseEntity().getBody().get(0).getInntektgrense())
            .isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().get(0).getInntektgrense())
    );
  }

  @Test
  @DisplayName("Skal kaste SjablonConsumerException når respons fra tjenesten ikke er OK for TrinnvisSkattesats")
  void skalKasteRestClientExceptionNaarResponsFraTjenestenIkkeErOkForTrinnvisSkattesats() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), any(), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatExceptionOfType(SjablonConsumerException.class).isThrownBy(() -> sjablonConsumer.hentSjablonTrinnvisSkattesats());
  }

  @Test
  @DisplayName("skal sende nav-apikey header for å snakke med applikasjon i dev-fss")
  void skalSendeNavApiKeyForKommunikasjonMedDevFss() {
    ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
    };

    sjablonConsumer.getSjablonWithApiKey("/some/sjablon", type);

    var entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
    verify(restTemplateMock).exchange(anyString(), eq(HttpMethod.GET), entityCaptor.capture(), eq(type));

    var sjablonEntity = entityCaptor.getValue();
    List<String> navApiKey = sjablonEntity.getHeaders().get(SjablonConsumer.X_NAV_API_KEY);

    assertThat(navApiKey).isEqualTo(List.of("test.key"));
  }
}

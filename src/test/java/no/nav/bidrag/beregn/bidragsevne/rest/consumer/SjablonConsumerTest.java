package no.nav.bidrag.beregn.bidragsevne.rest.consumer;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.commons.web.HttpStatusResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
@DisplayName("SjablonConsumerTest")
class SjablonConsumerTest {

  @InjectMocks
  private SjablonConsumer sjablonConsumer;

  @Mock
  private RestTemplate restTemplateMock;

  @Test
  @DisplayName("Skal hente liste av Sjablontall når respons fra tjenesten er OK")
  void skalHenteListeAvSjablontallNårResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonSjablontallListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonSjablontall();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getBody().size()).isEqualTo(TestUtil.dummySjablonSjablontallListe().size()),
        () -> assertThat(sjablonResponse.getBody().get(0).getTypeSjablon()).isEqualTo(TestUtil.dummySjablonSjablontallListe().get(0).getTypeSjablon())
    );
  }

  @Test
  @DisplayName("Skal returnere mottatt status for Sjablontall når respons fra tjenesten ikke er OK")
  void skalReturnereMottattStatusForSjablontallNårResponsFraTjenestenIkkeErOk() {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.put("error code", singletonList("503"));
    body.put("error msg", singletonList("SERVICE_UNAVAILABLE"));
    body.put("error text", singletonList("Service utilgjengelig"));

    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenReturn(new ResponseEntity(body, HttpStatus.SERVICE_UNAVAILABLE));
    var sjablonResponse = sjablonConsumer.hentSjablonSjablontall();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.SERVICE_UNAVAILABLE,
            "[error code:\"503\", error msg:\"SERVICE_UNAVAILABLE\", error text:\"Service utilgjengelig\"]"));
  }

  @Test
  @DisplayName("Skal returnere InternalServerError for Sjablontall når respons fra tjenesten er null")
  void skalReturnereInternalServerErrorForSjablontallNårResponsFraTjenestenErNull() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Sjablontall>>) any()))
        .thenReturn(null);
    var sjablonResponse = sjablonConsumer.hentSjablonSjablontall();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ukjent feil ved kall av bidrag-sjablon"));
  }

  @Test
  @DisplayName("Skal hente liste av Bidragsevne-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvBidragsevneSjablonerNårResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonBidragsevneListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonBidragsevne();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getBody().size()).isEqualTo(TestUtil.dummySjablonBidragsevneListe().size()),
        () -> assertThat(sjablonResponse.getBody().get(0).getBelopBoutgift())
            .isEqualTo(TestUtil.dummySjablonBidragsevneListe().get(0).getBelopBoutgift())
    );
  }

  @Test
  @DisplayName("Skal returnere mottatt status for Bidragsevne-sjabloner når respons fra tjenesten ikke er OK")
  void skalReturnereMottattStatusForBidragsevneSjablonerNårResponsFraTjenestenIkkeErOk() {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.put("error code", singletonList("503"));
    body.put("error msg", singletonList("SERVICE_UNAVAILABLE"));
    body.put("error text", singletonList("Service utilgjengelig"));

    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenReturn(new ResponseEntity(body, HttpStatus.SERVICE_UNAVAILABLE));
    var sjablonResponse = sjablonConsumer.hentSjablonBidragsevne();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.SERVICE_UNAVAILABLE,
            "[error code:\"503\", error msg:\"SERVICE_UNAVAILABLE\", error text:\"Service utilgjengelig\"]"));
  }

  @Test
  @DisplayName("Skal returnere InternalServerError for Bidragsevne-sjabloner når respons fra tjenesten er null")
  void skalReturnereInternalServerErrorForBidragsevneSjablonerNårResponsFraTjenestenErNull() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<Bidragsevne>>) any()))
        .thenReturn(null);
    var sjablonResponse = sjablonConsumer.hentSjablonBidragsevne();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ukjent feil ved kall av bidrag-sjablon"));
  }

  @Test
  @DisplayName("Skal hente liste av TrinnvisSkattesats-sjabloner når respons fra tjenesten er OK")
  void skalHenteListeAvTrinnvisSkattesatsSjablonerNårResponsFraTjenestenErOk() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenReturn(new ResponseEntity<>(TestUtil.dummySjablonTrinnvisSkattesatsListe(), HttpStatus.OK));
    var sjablonResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    assertAll(
        () -> assertThat(sjablonResponse).isNotNull(),
        () -> assertThat(sjablonResponse.getBody().size()).isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().size()),
        () -> assertThat(sjablonResponse.getBody().get(0).getInntektgrense())
            .isEqualTo(TestUtil.dummySjablonTrinnvisSkattesatsListe().get(0).getInntektgrense())
    );
  }

  @Test
  @DisplayName("Skal returnere mottatt status for TrinnvisSkattesats-sjabloner når respons fra tjenesten ikke er OK")
  void skalReturnereMottattStatusForTrinnvisSkattesatsSjablonerNårResponsFraTjenestenIkkeErOk() {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.put("error code", singletonList("503"));
    body.put("error msg", singletonList("SERVICE_UNAVAILABLE"));
    body.put("error text", singletonList("Service utilgjengelig"));

    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenReturn(new ResponseEntity(body, HttpStatus.SERVICE_UNAVAILABLE));
    var sjablonResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.SERVICE_UNAVAILABLE,
            "[error code:\"503\", error msg:\"SERVICE_UNAVAILABLE\", error text:\"Service utilgjengelig\"]"));
  }

  @Test
  @DisplayName("Skal returnere InternalServerError for TrinnvisSkattesats-sjabloner når respons fra tjenesten er null")
  void skalReturnereInternalServerErrorForTrinnvisSkattesatsSjablonerNårResponsFraTjenestenErNull() {
    when(restTemplateMock.exchange(anyString(), eq(HttpMethod.GET), eq(null), (ParameterizedTypeReference<List<TrinnvisSkattesats>>) any()))
        .thenReturn(null);
    var sjablonResponse = sjablonConsumer.hentSjablonTrinnvisSkattesats();

    assertThat(sjablonResponse)
        .isEqualToComparingFieldByField(new HttpStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ukjent feil ved kall av bidrag-sjablon"));
  }
}

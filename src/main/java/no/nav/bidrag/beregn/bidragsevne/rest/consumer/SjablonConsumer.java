package no.nav.bidrag.beregn.bidragsevne.rest.consumer;

import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class SjablonConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SjablonConsumer.class);
  private static final ParameterizedTypeReference<List<Sjablontall>> SJABLON_SJABLONTALL_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<Bidragsevne>> SJABLON_BIDRAGSEVNE_LISTE = new ParameterizedTypeReference<>() {
  };
  private static final ParameterizedTypeReference<List<TrinnvisSkattesats>> SJABLON_TRINNVIS_SKATTESATS_LISTE = new ParameterizedTypeReference<>() {
  };

  private final RestTemplate restTemplate;
  private final String sjablonSjablontallUrl;
  private final String sjablonBidragsevneUrl;
  private final String sjablonTrinnvisSkattesatsUrl;

  public SjablonConsumer(RestTemplate restTemplate, String sjablonBaseUrl) {
    this.restTemplate = restTemplate;
    this.sjablonSjablontallUrl = sjablonBaseUrl + "/sjablontall/all";
    this.sjablonBidragsevneUrl = sjablonBaseUrl + "/bidragsevner/all";
    this.sjablonTrinnvisSkattesatsUrl = sjablonBaseUrl + "/trinnvisskattesats/all";
  }

  public HttpResponse<List<Sjablontall>> hentSjablonSjablontall() {

    try {
      var sjablonResponse = restTemplate.exchange(sjablonSjablontallUrl, HttpMethod.GET, null, SJABLON_SJABLONTALL_LISTE);
      LOGGER.info("hentSjablonSjablontall fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
      return new HttpResponse<>(sjablonResponse);
    } catch (RestClientResponseException exception) {
      LOGGER.error("hentSjablonSjablontall fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
          exception.getMessage());
      throw new SjablonConsumerException(exception);
    }
  }

  public HttpResponse<List<Bidragsevne>> hentSjablonBidragsevne() {

    try {
      var sjablonResponse = restTemplate.exchange(sjablonBidragsevneUrl, HttpMethod.GET, null, SJABLON_BIDRAGSEVNE_LISTE);
      LOGGER.info("hentSjablonBidragsevne fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
      return new HttpResponse<>(sjablonResponse);
    } catch (RestClientResponseException exception) {
      LOGGER.error("hentSjablonBidragsevne fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
          exception.getMessage());
      throw new SjablonConsumerException(exception);
    }
  }

  public HttpResponse<List<TrinnvisSkattesats>> hentSjablonTrinnvisSkattesats() {

    try {
      var sjablonResponse = restTemplate.exchange(sjablonTrinnvisSkattesatsUrl, HttpMethod.GET, null, SJABLON_TRINNVIS_SKATTESATS_LISTE);
      LOGGER.info("hentSjablonTrinnvisSkattesats fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
      return new HttpResponse<>(sjablonResponse);
    } catch (RestClientResponseException exception) {
      LOGGER.error("hentSjablonTrinnvisSkattesats fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
          exception.getMessage());
      throw new SjablonConsumerException(exception);
    }
  }
}
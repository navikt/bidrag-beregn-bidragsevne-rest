package no.nav.bidrag.beregn.bidragsevne.rest.consumer;

import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.SjablonConsumerException;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class SjablonConsumer {

  static final String X_NAV_API_KEY = "X-Nav-ApiKey";
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
  private final String navApiKey;

  public SjablonConsumer(RestTemplate restTemplate, String sjablonBaseUrl, String navApiKey) {
    this.restTemplate = restTemplate;
    this.sjablonSjablontallUrl = sjablonBaseUrl + "/sjablontall/all";
    this.sjablonBidragsevneUrl = sjablonBaseUrl + "/bidragsevner/all";
    this.sjablonTrinnvisSkattesatsUrl = sjablonBaseUrl + "/trinnvisskattesats/all";
    this.navApiKey = navApiKey;
  }

  public HttpResponse<List<Sjablontall>> hentSjablonSjablontall() {

    try {
      var sjablonResponse = getSjablonWithApiKey(sjablonSjablontallUrl, SJABLON_SJABLONTALL_LISTE);
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
      var sjablonResponse = getSjablonWithApiKey(sjablonBidragsevneUrl, SJABLON_BIDRAGSEVNE_LISTE);
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
      var sjablonResponse = getSjablonWithApiKey(sjablonTrinnvisSkattesatsUrl, SJABLON_TRINNVIS_SKATTESATS_LISTE);
      LOGGER.info("hentSjablonTrinnvisSkattesats fikk http status {} fra bidrag-sjablon", sjablonResponse.getStatusCode());
      return new HttpResponse<>(sjablonResponse);
    } catch (RestClientResponseException exception) {
      LOGGER.error("hentSjablonTrinnvisSkattesats fikk følgende feilkode fra bidrag-sjablon: {}, med melding {}", exception.getStatusText(),
          exception.getMessage());
      throw new SjablonConsumerException(exception);
    }
  }

  <T> ResponseEntity<T> getSjablonWithApiKey(String url, ParameterizedTypeReference<T> type) {
    var headers = new HttpHeaders();
    headers.put(X_NAV_API_KEY, List.of(navApiKey));
    HttpEntity<T> requestWithNavApiKeyHeader = new HttpEntity<>(headers);
    return restTemplate.exchange(url, HttpMethod.GET, requestWithNavApiKeyHeader, type);
  }
}
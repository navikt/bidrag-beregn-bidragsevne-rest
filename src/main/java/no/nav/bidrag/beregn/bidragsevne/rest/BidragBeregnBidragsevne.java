package no.nav.bidrag.beregn.bidragsevne.rest;

import no.nav.bidrag.beregn.bidragsevne.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.felles.bidragsevne.BidragsevneCore;
import no.nav.bidrag.commons.ExceptionLogger;
import no.nav.bidrag.commons.web.CorrelationIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BidragBeregnBidragsevne {

  @Bean
  public BidragsevneCore bidragsevneCore() {
    return BidragsevneCore.getInstance();
  }

  @Bean
  public SjablonConsumer sjablonConsumer(
      @Value("${SJABLON_URL}") String sjablonBaseUrl,
      RestTemplate restTemplate,
      @Value("${NAV_API_KEY}") String navApiKey
  ) {
    return new SjablonConsumer(restTemplate, sjablonBaseUrl, navApiKey);
  }

  @Bean
  public ExceptionLogger exceptionLogger() {
    return new ExceptionLogger(BidragBeregnBidragsevne.class.getSimpleName());
  }

  @Bean
  public CorrelationIdFilter correlationIdFilter() {
    return new CorrelationIdFilter();
  }

  public static void main(String[] args) {
    SpringApplication.run(BidragBeregnBidragsevne.class, args);
  }
}

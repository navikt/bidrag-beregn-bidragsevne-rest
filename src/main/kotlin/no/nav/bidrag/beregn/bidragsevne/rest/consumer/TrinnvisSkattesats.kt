package no.nav.bidrag.beregn.bidragsevne.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class TrinnvisSkattesats (
  var datoFom: LocalDate? = null,
  var datoTom: LocalDate? = null,
  var inntektgrense: BigDecimal? = null,
  var sats: BigDecimal? = null
)

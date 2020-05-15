package no.nav.bidrag.beregn.bidragsevne.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
class TrinnvisSkattesats {
  var datoFom: LocalDate? = null
  var datoTom: LocalDate? = null
  var inntektgrense: BigDecimal? = null
  var sats: BigDecimal? = null

  constructor() {}

  constructor(datoFom: LocalDate?, datoTom: LocalDate?, inntektgrense: BigDecimal?, sats: BigDecimal?) {
    this.datoFom = datoFom
    this.datoTom = datoTom
    this.inntektgrense = inntektgrense
    this.sats = sats
  }
}
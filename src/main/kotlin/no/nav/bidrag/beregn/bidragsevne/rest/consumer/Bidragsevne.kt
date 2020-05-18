package no.nav.bidrag.beregn.bidragsevne.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
class Bidragsevne {
  var bostatus: String? = null
  var datoFom: LocalDate? = null
  var datoTom: LocalDate? = null
  var belopBoutgift: BigDecimal? = null
  var belopUnderhold: BigDecimal? = null

  constructor() {}

  constructor(bostatus: String?, datoFom: LocalDate?, datoTom: LocalDate?, belopBoutgift: BigDecimal?, belopUnderhold: BigDecimal?) {
    this.bostatus = bostatus
    this.datoFom = datoFom
    this.datoTom = datoTom
    this.belopBoutgift = belopBoutgift
    this.belopUnderhold = belopUnderhold
  }
}

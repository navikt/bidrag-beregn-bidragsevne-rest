package no.nav.bidrag.beregn.bidragsevne.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
class Sjablontall {
  var typeSjablon: String? = null
  var datoFom: LocalDate? = null
  var datoTom: LocalDate? = null
  var verdi: BigDecimal? = null

  protected constructor() {}
  constructor(typeSjablon: String?, datoFom: LocalDate?, datoTom: LocalDate?, verdi: BigDecimal?) {
    this.typeSjablon = typeSjablon
    this.datoFom = datoFom
    this.datoTom = datoTom
    this.verdi = verdi
  }
}
package no.nav.bidrag.beregn.bidragsevne.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.felles.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneResultatCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BostatusPeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.InntektCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.InntektPeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.PeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SaerfradragPeriodeCore
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonCore
import java.time.LocalDate


// Grunnlag
@ApiModel(value = "Grunnlaget for en bidragsevnesberegning")
data class BeregnBidragsevneGrunnlag(
    @ApiModelProperty(value = "Beregn bidragsevne fra-dato") var beregnDatoFra: LocalDate? = null,
    @ApiModelProperty(value = "Beregn bidragsevne til-dato") var beregnDatoTil: LocalDate? = null,
    @ApiModelProperty(value = "Periodisert liste over bidragmottakers inntekter") val inntektPeriodeListe: List<InntektPeriode>? = null,
    @ApiModelProperty(value = "Periodisert liste over søknadsbarnets bostatus") val bostatusPeriodeListe: List<BostatusPeriode>? = null,
    @ApiModelProperty(
        value = "Periodisert liste over antall barn i eget hushold") val antallBarnIEgetHusholdPeriodeListe: List<AntallBarnIEgetHusholdPeriode>? = null,
    @ApiModelProperty(value = "Periodisert liste over særfradrag") val saerfradragPeriodeListe: List<SaerfradragPeriode>? = null
) {
  fun tilCore() = BeregnBidragsevneGrunnlagAltCore(
      beregnDatoFra = if (beregnDatoFra != null) beregnDatoFra!! else throw UgyldigInputException("beregnDatoFra kan ikke være null"),
      beregnDatoTil = if (beregnDatoTil != null) beregnDatoTil!! else throw UgyldigInputException("beregnDatoTil kan ikke være null"),

      sjablonPeriodeListe = emptyList(),

      inntektPeriodeListe = if (inntektPeriodeListe != null) inntektPeriodeListe.map { it.tilCore() }
      else throw UgyldigInputException("inntektPeriodeListe kan ikke være null"),

      bostatusPeriodeListe = if (bostatusPeriodeListe != null) bostatusPeriodeListe.map { it.tilCore() }
      else throw UgyldigInputException("bostatusPeriodeListe kan ikke være null"),

      antallBarnIEgetHusholdPeriodeListe = if (antallBarnIEgetHusholdPeriodeListe != null) antallBarnIEgetHusholdPeriodeListe.map { it.tilCore() }
      else throw UgyldigInputException("antallBarnIEgetHusholdPeriodeListe kan ikke være null"),

      saerfradragPeriodeListe = if (saerfradragPeriodeListe != null) saerfradragPeriodeListe.map { it.tilCore() }
      else throw UgyldigInputException("saerfradragPeriodeListe kan ikke være null")
  )
}

@ApiModel(value = "Bidragsmottakers inntekt")
data class InntektPeriode(
    @ApiModelProperty(value = "Bidragsmottakers inntekt fra-til-dato") var inntektDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Bidragsmottakers inntekt type") var inntektType: String? = null,
    @ApiModelProperty(value = "Bidragsmottakers skatteklasse") var skatteklasse: Int? = null,
    @ApiModelProperty(value = "Bidragsmottakers inntekt beløp") var inntektBelop: Double? = null
) {
  fun tilCore() = InntektPeriodeCore(
      inntektPeriodeDatoFraTil = if (inntektDatoFraTil != null) inntektDatoFraTil!!.tilCore() else throw UgyldigInputException(
          "inntektDatoFraTil kan ikke være null"),
      inntektType = if (inntektType != null) inntektType!! else throw UgyldigInputException("inntektType kan ikke være null"),
      skatteklasse = if (skatteklasse != null) skatteklasse!! else throw UgyldigInputException("skatteklasse kan ikke være null"),
      inntektBelop = if (inntektBelop != null) inntektBelop!! else throw UgyldigInputException("inntektBelop kan ikke være null")
  )
}

@ApiModel(value = "Søknadsbarnets bostatus")
data class BostatusPeriode(
    @ApiModelProperty(value = "Søknadsbarnets bostatus fra-til-dato") var bostatusDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Søknadsbarnets bostatuskode") var bostatusKode: String? = null
) {
  fun tilCore() = BostatusPeriodeCore(
      bostatusPeriodeDatoFraTil = if (bostatusDatoFraTil != null) bostatusDatoFraTil!!.tilCore() else throw UgyldigInputException(
          "bostatusDatoFraTil kan ikke være null"),
      bostatusKode = if (bostatusKode != null) bostatusKode!! else throw UgyldigInputException("bostatusKode kan ikke være null")
  )
}

@ApiModel(value = "Antall barn i eget hushold")
data class AntallBarnIEgetHusholdPeriode(
    @ApiModelProperty(value = "Antall barn i eget hushold fra-til-dato") var antallBarnIEgetHusholdDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Antall barn i eget husholde") var antallBarn: Int? = null
) {
  fun tilCore() = AntallBarnIEgetHusholdPeriodeCore(
      antallBarnIEgetHusholdPeriodeDatoFraTil = if (antallBarnIEgetHusholdDatoFraTil != null) antallBarnIEgetHusholdDatoFraTil!!.tilCore()
      else throw UgyldigInputException("antallBarnIEgetHusholdDatoFraTil kan ikke være null"),

      antallBarn = if (antallBarn != null) antallBarn!! else throw UgyldigInputException("antallBarn kan ikke være null")
  )
}

@ApiModel(value = "Særfradrag")
data class SaerfradragPeriode(
    @ApiModelProperty(value = "Særfradrag fra-til-dato") var saerfradragDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Særfradrag kode") var saerfradragKode: String? = null
) {
  fun tilCore() = SaerfradragPeriodeCore(
      saerfradragPeriodeDatoFraTil = if (saerfradragDatoFraTil != null) saerfradragDatoFraTil!!.tilCore() else throw UgyldigInputException(
          "saerfradragDatoFraTil kan ikke være null"),
      saerfradragKode = if (saerfradragKode != null) saerfradragKode!! else throw UgyldigInputException("saerfradragKode kan ikke være null")
  )
}


// Resultat
@ApiModel(value = "Resultatet av en bidragsevnesberegning")
data class BeregnBidragsevneResultat(
    @ApiModelProperty(
        value = "Periodisert liste over resultat av bidragsevnesberegning") var resultatPeriodeListe: List<ResultatPeriode> = emptyList()
) {
  constructor(beregnBidragsevneResultat: BeregnBidragsevneResultatCore) : this(
      resultatPeriodeListe = beregnBidragsevneResultat.resultatPeriodeListe.map { ResultatPeriode(it) }
  )
}

@ApiModel(value = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriode(
    @ApiModelProperty(value = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode,
    @ApiModelProperty(value = "Beregning resultat innhold") var resultatBeregning: ResultatBeregning,
    @ApiModelProperty(value = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlag
) {
  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregning = ResultatBeregning(resultatPeriode.resultatBeregning),
      resultatGrunnlag = ResultatGrunnlag(resultatPeriode.resultatGrunnlag)
  )
}

@ApiModel(value = "Resultatet av en beregning")
data class ResultatBeregning(
    @ApiModelProperty(value = "Resultatevne") var resultatEvne: Double = 0.0
) {
  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatEvne = resultatBeregning.resultatEvne
  )
}

@ApiModel(value = "Grunnlaget for en beregning")
data class ResultatGrunnlag(
    @ApiModelProperty(value = "Liste over bidragsmottakers inntekter") var inntektListe: List<Inntekt> = emptyList(),
    @ApiModelProperty(value = "Skatteklasse") var skatteklasse: Int,
    @ApiModelProperty(value = "Bostatuskode") var bostatusKode: String,
    @ApiModelProperty(value = "Antall egne barn i husstand") var antallEgneBarnIHusstand: Int,
    @ApiModelProperty(value = "Særfradragkode") var saerfradragKode: String,
    @ApiModelProperty(value = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {
  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      inntektListe = resultatGrunnlag.inntektListe.map { Inntekt(it) },
      skatteklasse = resultatGrunnlag.skatteklasse,
      bostatusKode = resultatGrunnlag.bostatusKode,
      antallEgneBarnIHusstand = resultatGrunnlag.antallEgneBarnIHusstand,
      saerfradragKode = resultatGrunnlag.saerfradragkode,
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}

@ApiModel(value = "Inntekttype og -beløp")
data class Inntekt(
    @ApiModelProperty(value = "Inntekt type") var inntektType: String,
    @ApiModelProperty(value = "Inntekt beløp") var inntektBelop: Double
) {
  constructor(inntekt: InntektCore) : this(
      inntektType = inntekt.inntektType,
      inntektBelop = inntekt.inntektBelop
  )
}

@ApiModel(value = "Sjablonverdier")
data class Sjablon(
    @ApiModelProperty(value = "Sjablon navn") var sjablonNavn: String,
    @ApiModelProperty(value = "Sjablon verdi 1") var sjablonVerdi1: Double,
    @ApiModelProperty(value = "Sjablon verdi 2") var sjablonVerdi2: Double? = null
) {
  constructor(sjablon: SjablonCore) : this(
      sjablonNavn = sjablon.sjablonnavn,
      sjablonVerdi1 = sjablon.sjablonVerdi1,
      sjablonVerdi2 = sjablon.sjablonVerdi2
  )
}


// Felles
@ApiModel(value = "Periode (fra-til dato)")
data class Periode(
    @ApiModelProperty(value = "Fra-dato") var periodeDatoFra: LocalDate? = null,
    @ApiModelProperty(value = "Til-dato") var periodeDatoTil: LocalDate? = null
) {
  constructor(periode: PeriodeCore) : this(
      periodeDatoFra = periode.periodeDatoFra,
      periodeDatoTil = periode.periodeDatoTil
  )

  fun tilCore() = PeriodeCore(
      periodeDatoFra = if (periodeDatoFra != null) periodeDatoFra!! else throw UgyldigInputException("periodeDatoFra kan ikke være null"),
      periodeDatoTil = periodeDatoTil
  )
}

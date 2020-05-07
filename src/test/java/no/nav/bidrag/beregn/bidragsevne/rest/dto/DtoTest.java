package no.nav.bidrag.beregn.bidragsevne.rest.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import no.nav.bidrag.beregn.bidragsevne.rest.TestUtil;
import no.nav.bidrag.beregn.bidragsevne.rest.exception.UgyldigInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DtoTest")
class DtoTest {

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBeregnDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("beregnDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når beregnDatoTil er null")
  void skalKasteIllegalArgumentExceptionNaarBeregnDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBeregnDatoTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("beregnDatoTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarInntektPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("inntektPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarInntektDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektDatoFraTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("inntektDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarInntektDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("periodeDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når inntektDatoTil er null")
  void skalIkkeKasteExceptionNaarInntektDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektDatoTil();
    assertThatCode(grunnlag::tilCore).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektType er null")
  void skalKasteIllegalArgumentExceptionNaarInntektTypeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektType();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("inntektType kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når skatteklasse er null")
  void skalKasteIllegalArgumentExceptionNaarSkatteklasseErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSkatteklasse();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("skatteklasse kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når inntektBelop er null")
  void skalKasteIllegalArgumentExceptionNaarInntektBelopErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenInntektBelop();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("inntektBelop kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarBostatusPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("bostatusPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarBostatusDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoFraTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("bostatusDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarBostatusDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("periodeDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når bostatusDatoTil er null")
  void skalIkkeKasteExceptionNaarBostatusDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusDatoTil();
    assertThatCode(grunnlag::tilCore).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når bostatusKode er null")
  void skalKasteIllegalArgumentExceptionNaarBostatusKodeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenBostatusKode();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("bostatusKode kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarAntallBarnIEgetHusholdPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("antallBarnIEgetHusholdPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarAntallBarnIEgetHusholdDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFraTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("antallBarnIEgetHusholdDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarnIEgetHusholdDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarAntallBarnIEgetHusholdDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("periodeDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når antallBarnIEgetHusholdDatoTil er null")
  void skalIkkeKasteExceptionNaarAntallBarnIEgetHusholdDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoTil();
    assertThatCode(grunnlag::tilCore).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når antallBarn er null")
  void skalKasteIllegalArgumentExceptionNaarAntallBarnErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenAntallBarn();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("antallBarn kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragPeriodeListe er null")
  void skalKasteIllegalArgumentExceptionNaarSaerfradragPeriodeListeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragPeriodeListe();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("saerfradragPeriodeListe kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragDatoFraTil er null")
  void skalKasteIllegalArgumentExceptionNaarSaerfradragDatoFraTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoFraTil();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("saerfradragDatoFraTil kan ikke være null");
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragDatoFra er null")
  void skalKasteIllegalArgumentExceptionNaarSaerfradragDatoFraErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoFra();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("periodeDatoFra kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception når saerfradragDatoTil er null")
  void skalIkkeKasteExceptionNaarSaerfradragDatoTilErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragDatoTil();
    assertThatCode(grunnlag::tilCore).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Skal kaste IllegalArgumentException når saerfradragKode er null")
  void skalKasteIllegalArgumentExceptionNaarSaerfradragKodeErNull() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlagUtenSaerfradragKode();
    assertThatExceptionOfType(UgyldigInputException.class).isThrownBy(grunnlag::tilCore)
        .withMessage("saerfradragKode kan ikke være null");
  }

  @Test
  @DisplayName("Skal ikke kaste exception")
  void skalIkkeKasteException() {
    var grunnlag = TestUtil.byggBidragsevneGrunnlag();
    assertThatCode(grunnlag::tilCore).doesNotThrowAnyException();
  }
}

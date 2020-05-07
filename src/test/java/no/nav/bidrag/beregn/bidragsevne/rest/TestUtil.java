package no.nav.bidrag.beregn.bidragsevne.rest;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import no.nav.bidrag.beregn.bidragsevne.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.AntallBarnIEgetHusholdPeriode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneGrunnlag;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BeregnBidragsevneResultat;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.BostatusPeriode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.Inntekt;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.InntektPeriode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.Periode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.ResultatBeregning;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.ResultatGrunnlag;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.ResultatPeriode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.SaerfradragPeriode;
import no.nav.bidrag.beregn.bidragsevne.rest.dto.http.Sjablon;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneGrunnlagAltCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.BostatusPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.InntektCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.InntektPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatBeregningCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatGrunnlagCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.ResultatPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SaerfradragPeriodeCore;
import no.nav.bidrag.beregn.felles.bidragsevne.dto.SjablonCore;

public class TestUtil {

  // Bygger opp liste av sjablonverdier
  public static List<Sjablontall> dummySjablonListe() {
    var sjablontallListe = new ArrayList<Sjablontall>();
    sjablontallListe.add(new Sjablontall("0005", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(1600)));
    sjablontallListe.add(new Sjablontall("0013", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(320)));
    sjablontallListe.add(new Sjablontall("0033", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(270200)));
    sjablontallListe.add(new Sjablontall("0034", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(419700)));
    sjablontallListe.add(new Sjablontall("0035", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(336500)));
    sjablontallListe.add(new Sjablontall("0036", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(61700)));
    sjablontallListe.add(new Sjablontall("XXXX", LocalDate.MIN, LocalDate.MAX, BigDecimal.valueOf(0)));
    return sjablontallListe;
  }

  // Bygger opp BeregnBidragsevneGrunnlagCore
  public static BeregnBidragsevneGrunnlagAltCore dummyBidragsevneGrunnlagCore() {

    var inntektPeriode = new InntektPeriodeCore(new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01")), "LØNNSINNTEKT", 0, 0d);
    var inntektPeriodeListe = new ArrayList<InntektPeriodeCore>();
    inntektPeriodeListe.add(inntektPeriode);

    var bostatusPeriode = new BostatusPeriodeCore(new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01")), "MED_ANDRE");
    var bostatusPeriodeListe = new ArrayList<BostatusPeriodeCore>();
    bostatusPeriodeListe.add(bostatusPeriode);

    var antallBarnIEgetHusholdPeriode = new AntallBarnIEgetHusholdPeriodeCore(
        new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01")), 1);
    var antallBarnIEgetHusholdPeriodeListe = new ArrayList<AntallBarnIEgetHusholdPeriodeCore>();
    antallBarnIEgetHusholdPeriodeListe.add(antallBarnIEgetHusholdPeriode);

    var saerfradragPeriode = new SaerfradragPeriodeCore(new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01")), "HELT");
    var saerfradragPeriodeListe = new ArrayList<SaerfradragPeriodeCore>();
    saerfradragPeriodeListe.add(saerfradragPeriode);

    return new BeregnBidragsevneGrunnlagAltCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01"), emptyList(),
        inntektPeriodeListe, bostatusPeriodeListe, antallBarnIEgetHusholdPeriodeListe, saerfradragPeriodeListe);
  }

  // Bygger opp BeregnBidragsevneResultatCore
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCore() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriodeCore>();
    bidragPeriodeResultatListe.add(new ResultatPeriodeCore(new PeriodeCore(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new ResultatBeregningCore(100d),
        new ResultatGrunnlagCore(singletonList(new InntektCore("LØNNSINNTEKT", 500000d)), 1, "MED_ANDRE", 1,
            "HELT", singletonList(new SjablonCore("SatsTrygdeavgift", 0d, 0d)))));
    return new BeregnBidragsevneResultatCore(bidragPeriodeResultatListe, emptyList());
  }

  // Bygger opp BeregnBidragsevneResultatCore med avvik
  public static BeregnBidragsevneResultatCore dummyBidragsevneResultatCoreMedAvvik() {
    var avvikListe = new ArrayList<AvvikCore>();
    avvikListe.add(new AvvikCore("beregnDatoFra kan ikke være null", "NULL_VERDI_I_DATO"));
    avvikListe.add(new AvvikCore(
        "periodeDatoTil må være etter periodeDatoFra i inntektPeriodeListe: periodeDatoFra=2018-04-01, periodeDatoTil=2018-03-01",
        "DATO_FRA_ETTER_DATO_TIL"));
    return new BeregnBidragsevneResultatCore(emptyList(), avvikListe);
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlag() {
    return byggBidragsevneGrunnlag("");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBeregnDatoFra() {
    return byggBidragsevneGrunnlag("beregnDatoFra");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBeregnDatoTil() {
    return byggBidragsevneGrunnlag("beregnDatoTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektPeriodeListe() {
    return byggBidragsevneGrunnlag("inntektPeriodeListe");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusPeriodeListe() {
    return byggBidragsevneGrunnlag("bostatusPeriodeListe");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdPeriodeListe() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdPeriodeListe");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragPeriodeListe() {
    return byggBidragsevneGrunnlag("saerfradragPeriodeListe");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektDatoFraTil() {
    return byggBidragsevneGrunnlag("inntektDatoFraTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektDatoFra() {
    return byggBidragsevneGrunnlag("inntektDatoFra");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektDatoTil() {
    return byggBidragsevneGrunnlag("inntektDatoTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektType() {
    return byggBidragsevneGrunnlag("inntektType");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSkatteklasse() {
    return byggBidragsevneGrunnlag("skatteklasse");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenInntektBelop() {
    return byggBidragsevneGrunnlag("inntektBelop");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoFraTil() {
    return byggBidragsevneGrunnlag("bostatusDatoFraTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoFra() {
    return byggBidragsevneGrunnlag("bostatusDatoFra");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusDatoTil() {
    return byggBidragsevneGrunnlag("bostatusDatoTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenBostatusKode() {
    return byggBidragsevneGrunnlag("bostatusKode");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFraTil() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoFraTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoFra() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoFra");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarnIEgetHusholdDatoTil() {
    return byggBidragsevneGrunnlag("antallBarnIEgetHusholdDatoTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenAntallBarn() {
    return byggBidragsevneGrunnlag("antallBarn");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoFraTil() {
    return byggBidragsevneGrunnlag("saerfradragDatoFraTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoFra() {
    return byggBidragsevneGrunnlag("saerfradragDatoFra");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragDatoTil() {
    return byggBidragsevneGrunnlag("saerfradragDatoTil");
  }

  public static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlagUtenSaerfradragKode() {
    return byggBidragsevneGrunnlag("saerfradragKode");
  }

  // Bygger opp BeregnBidragsevneGrunnlag
  private static BeregnBidragsevneGrunnlag byggBidragsevneGrunnlag(String nullVerdi) {
    var beregnDatoFra = (nullVerdi.equals("beregnDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var beregnDatoTil = (nullVerdi.equals("beregnDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var inntektDatoFra = (nullVerdi.equals("inntektDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var inntektDatoTil = (nullVerdi.equals("inntektDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var inntektType = (nullVerdi.equals("inntektType") ? null : "LØNNSINNTEKT");
    var skatteklasse = (nullVerdi.equals("skatteklasse") ? null : 1);
    var inntektBelop = (nullVerdi.equals("inntektBelop") ? null : 100000d);
    var bostatusDatoFra = (nullVerdi.equals("bostatusDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var bostatusDatoTil = (nullVerdi.equals("bostatusDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var bostatusKode = (nullVerdi.equals("bostatusKode") ? null : "MED_ANDRE");
    var antallBarnIEgetHusholdDatoFra = (nullVerdi.equals("antallBarnIEgetHusholdDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var antallBarnIEgetHusholdDatoTil = (nullVerdi.equals("antallBarnIEgetHusholdDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var antallBarn = (nullVerdi.equals("antallBarn") ? null : 1);
    var saerfradragDatoFra = (nullVerdi.equals("saerfradragDatoFra") ? null : LocalDate.parse("2017-01-01"));
    var saerfradragDatoTil = (nullVerdi.equals("saerfradragDatoTil") ? null : LocalDate.parse("2020-01-01"));
    var saerfradragKode = (nullVerdi.equals("saerfradragKode") ? null : "HELT");

    List<InntektPeriode> inntektPeriodeListe;
    if (nullVerdi.equals("inntektPeriodeListe")) {
      inntektPeriodeListe = null;
    } else {
      InntektPeriode inntektPeriode;
      if (nullVerdi.equals("inntektDatoFraTil")) {
        inntektPeriode = new InntektPeriode(null, inntektType, skatteklasse, inntektBelop);
      } else {
        inntektPeriode = new InntektPeriode(new Periode(inntektDatoFra, inntektDatoTil), inntektType, skatteklasse, inntektBelop);
      }
      inntektPeriodeListe = new ArrayList<>();
      inntektPeriodeListe.add(inntektPeriode);
    }

    List<BostatusPeriode> bostatusPeriodeListe;
    if (nullVerdi.equals("bostatusPeriodeListe")) {
      bostatusPeriodeListe = null;
    } else {
      BostatusPeriode bostatusPeriode;
      if (nullVerdi.equals("bostatusDatoFraTil")) {
        bostatusPeriode = new BostatusPeriode(null, bostatusKode);
      } else {
        bostatusPeriode = new BostatusPeriode(new Periode(bostatusDatoFra, bostatusDatoTil), bostatusKode);
      }
      bostatusPeriodeListe = new ArrayList<>();
      bostatusPeriodeListe.add(bostatusPeriode);
    }

    List<AntallBarnIEgetHusholdPeriode> antallBarnIEgetHusholdPeriodeListe;
    if (nullVerdi.equals("antallBarnIEgetHusholdPeriodeListe")) {
      antallBarnIEgetHusholdPeriodeListe = null;
    } else {
      AntallBarnIEgetHusholdPeriode antallBarnIEgetHusholdPeriode;
      if (nullVerdi.equals("antallBarnIEgetHusholdDatoFraTil")) {
        antallBarnIEgetHusholdPeriode = new AntallBarnIEgetHusholdPeriode(null, antallBarn);
      } else {
        antallBarnIEgetHusholdPeriode = new AntallBarnIEgetHusholdPeriode(new Periode(antallBarnIEgetHusholdDatoFra, antallBarnIEgetHusholdDatoTil), antallBarn);
      }
      antallBarnIEgetHusholdPeriodeListe = new ArrayList<>();
      antallBarnIEgetHusholdPeriodeListe.add(antallBarnIEgetHusholdPeriode);
    }

    List<SaerfradragPeriode> saerfradragPeriodeListe;
    if (nullVerdi.equals("saerfradragPeriodeListe")) {
      saerfradragPeriodeListe = null;
    } else {
      SaerfradragPeriode saerfradragPeriode;
      if (nullVerdi.equals("saerfradragDatoFraTil")) {
        saerfradragPeriode = new SaerfradragPeriode(null, saerfradragKode);
      } else {
        saerfradragPeriode = new SaerfradragPeriode(new Periode(saerfradragDatoFra, saerfradragDatoTil), saerfradragKode);
      }
      saerfradragPeriodeListe = new ArrayList<>();
      saerfradragPeriodeListe.add(saerfradragPeriode);
    }

    return new BeregnBidragsevneGrunnlag(beregnDatoFra, beregnDatoTil, inntektPeriodeListe, bostatusPeriodeListe, antallBarnIEgetHusholdPeriodeListe,
        saerfradragPeriodeListe);
  }

  // Bygger opp BeregnBidragsevneResultat
  public static BeregnBidragsevneResultat dummyBidragsevneResultat() {
    var bidragPeriodeResultatListe = new ArrayList<ResultatPeriode>();
    bidragPeriodeResultatListe.add(new ResultatPeriode(new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-01-01")),
        new ResultatBeregning(100d),
        new ResultatGrunnlag(singletonList(new Inntekt("LØNNSINNTEKT", 500000d)), 1, "MED_ANDRE", 1,
            "HELT", singletonList(new Sjablon("SatsTrygdeavgift", 0d, 0d)))));
    return new BeregnBidragsevneResultat(bidragPeriodeResultatListe);
  }
}

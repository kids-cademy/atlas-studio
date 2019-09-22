package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.HDate.Era;
import com.kidscademy.atlas.studio.model.HDate.Format;
import com.kidscademy.atlas.studio.model.HDate.Period;

public class HDateTest
{
  @Test
  public void defaultConstructor()
  {
    HDate date = new HDate();

    assertThat(date.getValue(), nullValue());
    assertThat(date.getFormat(), nullValue());
    assertThat(date.getPeriod(), nullValue());
    assertThat(date.getEra(), nullValue());
  }

  @Test
  public void utilDate()
  {
    HDate date = new HDate(new java.util.Date(0L));

    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(HDate.Format.DATE));
    assertThat(date.getPeriod(), equalTo(HDate.Period.FULL));
    assertThat(date.getEra(), equalTo(HDate.Era.CE));
  }

  @Test
  public void sqlDate()
  {
    HDate date = new HDate(new java.sql.Date(0L));

    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(HDate.Format.DATE));
    assertThat(date.getPeriod(), equalTo(HDate.Period.FULL));
    assertThat(date.getEra(), equalTo(HDate.Era.CE));
  }

  @Test
  public void sqlTime()
  {
    HDate date = new HDate(new java.sql.Time(0L));

    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(HDate.Format.DATE));
    assertThat(date.getPeriod(), equalTo(HDate.Period.FULL));
    assertThat(date.getEra(), equalTo(HDate.Era.CE));
  }

  @Test
  public void systemTime()
  {
    HDate date = new HDate(0L);

    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(HDate.Format.DATE));
    assertThat(date.getPeriod(), equalTo(HDate.Period.FULL));
    assertThat(date.getEra(), equalTo(HDate.Era.CE));
  }

  @Test
  public void format_DATE()
  {
    HDate date = new HDate(0L, Format.DATE);
    assertFormat(date, HDate.Format.DATE);
  }

  @Test
  public void format_YEAR()
  {
    HDate date = new HDate(0L, Format.YEAR);
    assertFormat(date, HDate.Format.YEAR);
  }

  @Test
  public void format_DECADE()
  {
    HDate date = new HDate(0L, Format.DECADE);
    assertFormat(date, HDate.Format.DECADE);
  }

  @Test
  public void format_CENTURY()
  {
    HDate date = new HDate(0L, Format.CENTURY);
    assertFormat(date, HDate.Format.CENTURY);
  }

  @Test
  public void format_MILLENNIA()
  {
    HDate date = new HDate(0L, Format.MILLENNIA);
    assertFormat(date, HDate.Format.MILLENNIA);
  }

  @Test
  public void format_KYA()
  {
    HDate date = new HDate(0L, Format.KYA);
    assertFormat(date, HDate.Format.KYA);
  }

  @Test
  public void format_MYA()
  {
    HDate date = new HDate(0L, Format.MYA);
    assertFormat(date, HDate.Format.MYA);
  }

  @Test
  public void format_BYA()
  {
    HDate date = new HDate(0L, Format.BYA);
    assertFormat(date, HDate.Format.BYA);
  }

  @Test
  public void period_FULL()
  {
    HDate date = new HDate(0L, Format.CENTURY, Period.FULL);
    assertPeriod(date, Period.FULL);
  }

  @Test
  public void period_BEGINNING()
  {
    HDate date = new HDate(0L, Format.CENTURY, Period.BEGINNING);
    assertPeriod(date, Period.BEGINNING);
  }

  @Test
  public void period_MIDDLE()
  {
    HDate date = new HDate(0L, Format.CENTURY, Period.MIDDLE);
    assertPeriod(date, Period.MIDDLE);
  }

  @Test
  public void period_END()
  {
    HDate date = new HDate(0L, Format.CENTURY, Period.END);
    assertPeriod(date, Period.END);
  }

  @Test
  public void era_CE()
  {
    HDate date = new HDate(0L, Format.CENTURY, Era.CE);
    assertEra(date, Era.CE);
  }

  @Test
  public void era_BCE()
  {
    HDate date = new HDate(0L, Format.CENTURY, Era.BCE);
    assertEra(date, Era.BCE);
  }

  // ----------------------------------------------------------------------------------------------
  // UTILITY METHODS

  private static void assertFormat(HDate date, Format format)
  {
    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(format));
    assertThat(date.getPeriod(), equalTo(Period.FULL));
    assertThat(date.getEra(), equalTo(Era.CE));
  }

  private static void assertPeriod(HDate date, Period period)
  {
    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(Format.CENTURY));
    assertThat(date.getPeriod(), equalTo(period));
    assertThat(date.getEra(), equalTo(Era.CE));
  }

  private static void assertEra(HDate date, Era era)
  {
    assertThat(date.getValue(), equalTo(0.0));
    assertThat(date.getFormat(), equalTo(Format.CENTURY));
    assertThat(date.getPeriod(), equalTo(Period.FULL));
    assertThat(date.getEra(), equalTo(era));
  }
}

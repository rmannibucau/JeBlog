package com.github.rmannibucau.blog.domain.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;
import java.util.Date;

public class DateAdaptor extends XmlAdapter<String, Date> {
    private static final Calendar CALENDAR = Calendar.getInstance();

    @Override
    public Date unmarshal(final String v) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized String marshal(final Date date) throws Exception {
        CALENDAR.setTime(date);
        return CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.DAY_OF_MONTH) + "/" + CALENDAR.get(Calendar.YEAR)
            + " " +  CALENDAR.get(Calendar.HOUR) + ":" + CALENDAR.get(Calendar.MINUTE) + ":" + CALENDAR.get(Calendar.SECOND)
            + (CALENDAR.get(Calendar.AM_PM) == Calendar.AM? "AM" : "PM");
    }
}

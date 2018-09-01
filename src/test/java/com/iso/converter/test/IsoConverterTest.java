package com.iso.converter.test;

import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class IsoConverterTest {

	@Test
	public void testConvertDateToXML() throws DatatypeConfigurationException, ParseException {
		//setup data
		DateFormat df = new SimpleDateFormat("yyMMdd");
		String isoDate = df.format(new Date());
		//run code
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new SimpleDateFormat("yyMMdd").parse(isoDate));
		XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		//output to check
		System.out.println(xmlDate.toString());
	}
}

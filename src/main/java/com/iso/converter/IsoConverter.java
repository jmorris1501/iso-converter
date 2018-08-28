package com.iso.converter;

import iso.std.iso._20022.tech.xsd.pain_001_001.CustomerCreditTransferInitiationV08;
import iso.std.iso._20022.tech.xsd.pain_002_001.CustomerPaymentStatusReportV09;
import org.bitbucket.openisoj.Iso8583;
import org.bitbucket.openisoj.Iso8583Rev93;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@SuppressWarnings("unused")
public class IsoConverter {
	/**
	 * create an Iso8583 message from a byte array
	 *
	 * @param bytes the byte array
	 * @return the Iso8583 message
	 * @throws Exception in case of error unpacking the array
	 */
	public static Iso8583 getIso8583(byte[] bytes) throws Exception {
		Iso8583 msg = new Iso8583();
		msg.unpack(bytes, 0);
		return msg;
	}

	/**
	 * create a customer credit transfer initiation message from the string xml data
	 *
	 * @param data string xml data
	 * @return customer credit transfer initiation object
	 * @throws JAXBException in case of error parsing xml
	 */
	public static CustomerCreditTransferInitiationV08 getCustomerCreditTransferInitiation(String data) throws JAXBException {
		return ((iso.std.iso._20022.tech.xsd.pain_001_001.Document) unMarshal(data)).getCstmrCdtTrfInitn();
	}

	/**
	 * create a customer payment status report message from the string xml data
	 *
	 * @param data string xml data
	 * @return customer payment status report object
	 * @throws JAXBException in case of error parsing xml
	 */
	public static CustomerPaymentStatusReportV09 getCustomerPaymentStatusReport(String data) throws JAXBException {
		return ((iso.std.iso._20022.tech.xsd.pain_002_001.Document) unMarshal(data)).getCstmrPmtStsRpt();
	}

	/**
	 * return an object representing an Iso20022 message
	 *
	 * @param data the string xml data
	 * @return the message object
	 * @throws JAXBException in case of error while parsing the xml string
	 */
	private static Object unMarshal(String data) throws JAXBException {
		//TODO object types, see if possible to pass in type or if reading return is safe
		JAXBContext jaxbContext = JAXBContext.newInstance(Object.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(data);
		return jaxbUnMarshaller.unmarshal(reader);
	}
}

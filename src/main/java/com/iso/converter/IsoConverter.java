package com.iso.converter;

import iso.std.iso._20022.tech.xsd.pain_001_001.*;
import iso.std.iso._20022.tech.xsd.pain_002_001.CustomerPaymentStatusReportV09;
import org.bitbucket.openisoj.Iso8583;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
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
	 * convert an Iso8583 0100 request message to a CustomerCreditTransferInitiationV08 message
	 *
	 * @param iso8583 iso8583 0100 request message
	 * @return CustomerCreditTransferInitiationV08 message
	 */
	public static CustomerCreditTransferInitiationV08 convert85830100to20022(
			Iso8583 iso8583,
			PartyIdentification43 initParty,
			BranchAndFinancialInstitutionIdentification5 forwardingAgent,
			String entityID,
			PartyIdentification43 debtorInfo,
			BranchAndFinancialInstitutionIdentification5 debtorAgent,
			CashAccount24 debtorAccount,
			PartyIdentification43 ultimateDebtor,
			CashAccount24 chargeAccount,
			BranchAndFinancialInstitutionIdentification5 chargeAccountAgent) throws ParseException, DatatypeConfigurationException {
		CustomerCreditTransferInitiationV08 iso20022 = new CustomerCreditTransferInitiationV08();

		BigDecimal controlSum = getControlSum(iso8583);

		// create the group header for the message
		GroupHeader48 grpHdr = new GroupHeader48();
		String isoDate = iso8583.get(Iso8583.Bit._007_TRAN_DATE_TIME);
		grpHdr.setMsgId(entityID + "/" + isoDate + "/CCT001/0100");
		grpHdr.setCreDtTm(convertDateToXML(isoDate, YYMMDD_FORMAT));
		Authorisation1Choice authChoice = new Authorisation1Choice();
		authChoice.setCd(Authorisation1Code.AUTH);
		authChoice.setPrtry(AUTH_CHC_PR_HIGH);
		grpHdr.getAuthstn().add(authChoice);
		grpHdr.setNbOfTxs("1");
		grpHdr.setCtrlSum(controlSum);
		grpHdr.setInitgPty(initParty);
		grpHdr.setFwdgAgt(forwardingAgent);
		//destination and address (issuer?)
		//branch and fin inst info
		iso20022.setGrpHdr(grpHdr);

		//actual payment
		PaymentInstruction22 payment = new PaymentInstruction22();
		payment.setPmtInfId(entityID + iso8583.get(Iso8583.Bit._011_SYS_TRACE_AUDIT_NUM));
		payment.setPmtMtd(PaymentMethod3Code.TRA);
		payment.setNbOfTxs("1");
		payment.setCtrlSum(controlSum);
		DateAndDateTimeChoice ddtc = new DateAndDateTimeChoice();
		ddtc.setDt(convertDateToXML(iso8583.get(Iso8583.Bit._013_LOCAL_TRAN_DATE) + iso8583.get(Iso8583.Bit._012_LOCAL_TRAN_TIME), MMDD_FORMAT + HHMMSS_FORMAT));
		payment.setReqdExctnDt(ddtc);
		payment.setDbtr(debtorInfo);
		payment.setDbtrAcct(debtorAccount);
		payment.setDbtrAgt(debtorAgent);
		payment.setUltmtDbtr(ultimateDebtor);
		payment.setChrgsAcct(chargeAccount);
		payment.setChrgsAcctAgt(chargeAccountAgent);

		CreditTransferTransaction26 ctt26 = new CreditTransferTransaction26();
		//TODO
		payment.getCdtTrfTxInf().add(ctt26);
		iso20022.getPmtInf().add(payment);

		return iso20022;
	}

	/**
	 * convert an Iso8583 0110 response message to a CustomerPaymentStatusReportV09 message
	 *
	 * @param iso8583 iso 8583 0110 response message
	 * @return CustomerPaymentStatusReportV09 message
	 */
	public static CustomerPaymentStatusReportV09 convert85830110to20022(Iso8583 iso8583) {
		CustomerPaymentStatusReportV09 iso20022 = new CustomerPaymentStatusReportV09();
		//TODO map fields
		return iso20022;
	}

	/**
	 * convert a CustomerCreditTransferInitiationV08 message to an Iso8583 0100 request message
	 *
	 * @param iso20022 CustomerCreditTransferInitiationV08 message
	 * @return iso8583 0100 request message
	 */
	public static Iso8583 convert20022CCTIto85830100(CustomerCreditTransferInitiationV08 iso20022) {
		Iso8583 iso8583 = new Iso8583();
		//TODO map fields
		return iso8583;
	}

	/**
	 * convert a CustomerPaymentStatusReportV09 message to an Iso8583 0110 response message
	 *
	 * @param iso20022 CustomerPaymentStatusReportV09 message
	 * @return iso 8583 0110 response message
	 */
	public static Iso8583 convert20022CPSRto85830100(CustomerCreditTransferInitiationV08 iso20022) {
		Iso8583 iso8583 = new Iso8583();
		//TODO map fields
		return iso8583;
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

	private static XMLGregorianCalendar convertDateToXML(String isoDate, String format) throws ParseException, DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new SimpleDateFormat(format).parse(isoDate));
		XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return xmlDate;
	}

	private static BigDecimal getControlSum(Iso8583 iso8583) {
		// 4 5 6 8 28 29 30 31
		BigDecimal controlSum = new BigDecimal(0);
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._004_TRAN_AMOUNT)));
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._005_SETTLE_AMOUNT)));
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._028_TRAN_FEE_AMOUNT)));
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._029_SETTLEMENT_FEE_AMOUNT)));
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._030_TRAN_PROC_FEE_AMOUNT)));
		controlSum = controlSum.add(new BigDecimal(iso8583.get(Iso8583.Bit._031_SETTLEMENT_PROC_FEE_AMOUNT)));
		return controlSum;
	}

	private static final String YYMMDD_FORMAT = "yyMMdd";
	private static final String MMDD_FORMAT = "MMdd";
	private static final String HHMMSS_FORMAT = "hhmmss";
	private static final String AUTH_CHC_PR_HIGH = "HIGH_PRIORITY";
}

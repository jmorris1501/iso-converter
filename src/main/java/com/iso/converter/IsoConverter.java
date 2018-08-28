package com.iso.converter;

import org.bitbucket.openisoj.Iso8583;
import org.bitbucket.openisoj.Iso8583Rev93;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;

@SuppressWarnings("unused")
public class IsoConverter
{
	 /**
	  * create an Iso8583 message from a byte array
	  *
	  * @param bytes the byte array
	  * @return the Iso8583 message
	  * @throws Exception in case of error unpacking the array
	  */
	 public static Iso8583 getIso8583(byte[] bytes) throws Exception
	 {
		  Iso8583 msg = new Iso8583();
		  msg.unpack(bytes, 0);
		  return msg;
	 }

	 /**
	  * create an Iso8583Rev93 message from a byte array
	  *
	  * @param bytes the byte array
	  * @return the Iso8583Rev93 message
	  * @throws Exception in case of error unpacking the array
	  */
	 public static Iso8583Rev93 getIso8583Rev93(byte[] bytes) throws Exception
	 {
		  Iso8583Rev93 msg = new Iso8583Rev93();
		  msg.unpack(bytes, 0);
		  return msg;
	 }

	/**
	 * return an object representing an Iso20022 message
	 *
	 * @param data the string xml data
	 * @return the message object
	 * @throws JAXBException in case of error while parsing the xml string
	 */
	public static Object unMarshal(String data) throws JAXBException {
		//TODO object types, see if possible to pass in type or if reading return is safe
		JAXBContext jaxbContext = JAXBContext.newInstance(Object.class);
		Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(data);
		return jaxbUnMarshaller.unmarshal(reader);
	}
}

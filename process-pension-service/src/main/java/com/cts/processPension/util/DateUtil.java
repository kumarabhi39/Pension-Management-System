package com.cts.processPension.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Utility class is for converting string to date format
 * 
 * @author Abhishek Kumar
 */

public class DateUtil {

	private DateUtil() {
	}

	public static Date parseDate(String date) throws ParseException {
		return new SimpleDateFormat("dd-MM-yyyy").parse(date);
	}

}
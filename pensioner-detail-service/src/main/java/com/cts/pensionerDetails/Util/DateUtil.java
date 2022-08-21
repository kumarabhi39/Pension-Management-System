package com.cts.pensionerDetails.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the DateUtil class which consists parseDate method which is used to
 * convert the string into the Date Type
 * 
 * @author Abhishek Kumar
 * 
 */

public class DateUtil {

	private DateUtil() {
	}

	public static Date parseDate(String date) throws ParseException {
		return new SimpleDateFormat("dd-MM-yyyy").parse(date);
	}

}

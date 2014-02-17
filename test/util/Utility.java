package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	
	
	public static String DateTimeToStringConvert(Date date){
		DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
		return dateFormat.format(date);
	}
	
	public static String getUniqueNumber(final String mask) {

		char[] buf = new char[mask.length()];
		char[] charsArray = mask.toCharArray();

		for (int i = 0; i < charsArray.length; i++) {
			if (charsArray[i] == '9') {
				buf[i] = Constant.NUMS.charAt(Constant.RANDOM.nextInt(Constant.NUMS.length()));
			}
		}
		return new String(buf);
	}
	
}

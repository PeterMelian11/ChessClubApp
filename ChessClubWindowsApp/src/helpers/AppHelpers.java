package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AppHelpers {
	
	public static String isStringEmpty(String keyName, String stringVal) throws Exception {
		if (stringVal.isEmpty()) {
			throw new Exception(keyName + " is Empty. Expecting a NON_EMPTY value.");
		}
		return stringVal.trim();
	}

	
	public static String isValidEmailAddress(String keyName,String email) throws Exception {
		isStringEmpty(keyName,email);
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		
		if(!email.matches(regex)) {
			throw new Exception(keyName + " malformed email address.");
		}
		return email;
	}
	
	public static String validateAndParseBirthDate(String keyName,String birthDate) throws Exception {
		isStringEmpty(keyName,birthDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
				format.parse(birthDate);
		}catch(ParseException er) {
			throw new Exception(keyName + " expected date format as 'yyyy-MM-dd'.");
		}
		
		return birthDate;
	}
}

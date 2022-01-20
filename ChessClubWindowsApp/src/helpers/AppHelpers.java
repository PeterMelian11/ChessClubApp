package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	
	public static int isInteger(String keyName,String sId) throws Exception {
		isStringEmpty(keyName,sId);
	    boolean isNumberic =true;
		int iC = 0;
	    for(int i = 0; i < sId.length(); i++) {
	        if(i == 0 && sId.charAt(i) == '-') {
	            if(sId.length() == 1) isNumberic =false;
	            else continue;
	        }
	        if(Character.digit(sId.charAt(i),10) < 0) isNumberic =false;
	    }
	    if(!isNumberic) {
	    	throw new Exception(keyName + " string value is NOT numeric'.");
	    }
	    try{
	    	iC=  Integer.parseInt(sId);
	    }catch(NumberFormatException vv) {
	    	throw new Exception(keyName + " exception parsing string," + vv.getMessage());
	    }
		return iC;
	}
	
	public static TreeMap <Integer,Integer>deleteAndReorderMap(TreeMap <Integer,Integer> tInput,int sID){
		TreeMap <Integer,Integer>tReturn 	= new TreeMap<Integer,Integer>();
		int iRank = 0;		
		for (Entry<Integer, Integer> eSet : tInput.entrySet()) {
			if(eSet.getValue().equals(sID)) {
				iRank = eSet.getKey();
				break;
			}
		}
		if(tInput.lastKey() == iRank){
			tInput.remove(iRank);
			return tInput;
		}else {
			int nRankCounter =1;
			for (Entry<Integer, Integer> eSet : tInput.entrySet()) {
				if(eSet.getKey() != iRank) {
					tReturn.put(nRankCounter, eSet.getValue());
					nRankCounter++;
				}
			}
			return tReturn;
		}
	}
}

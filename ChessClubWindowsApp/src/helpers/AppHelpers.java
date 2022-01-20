package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
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
	
	public static TreeMap <Integer,Integer> onDrawNonAdjacentReorderMap(int iLowerRankId , int iLowerRankcurrentVal ,TreeMap <Integer,Integer>tRankIdMap ) {
		TreeMap <Integer,Integer>tReturn 	= new TreeMap<Integer,Integer>();
		for (Entry<Integer, Integer> mRes : tRankIdMap.entrySet()) {
			if(mRes.getValue().equals(iLowerRankId)) {
				Map.Entry<Integer, Integer> prevRecord = ((TreeMap<Integer, Integer>) tRankIdMap).lowerEntry(iLowerRankcurrentVal); // previous
				if(prevRecord != null) {
					tReturn.put(prevRecord.getKey()+1, prevRecord.getValue());
					tReturn.put(mRes.getKey()-1, mRes.getValue());
				}
			}else {
				tReturn.put(mRes.getKey(), mRes.getValue());
			}
		}
		return tReturn;
	}
	
	public static int getCurrentRankFromId(TreeMap <Integer,Integer>sortedIdRankMap,int iComaprePlayerId) {
		// Returns the key from the value
		for (Entry<Integer, Integer> iterable_element : sortedIdRankMap.entrySet()) {
			if(iterable_element.getValue().equals(iComaprePlayerId)) {
				return iterable_element.getKey();
			}
		}
		return 0;
	}
	
	public static TreeMap<Integer,Integer> popIntoMapAndIncrement(TreeMap<Integer,Integer> iMp, Integer newRank, Integer newRankId,Integer oldRank,Integer higherRankId,Integer iHigherRankCurrentVal) {
		if(newRank == oldRank) {
			// These players are adjastent so just swap higher and lower ranks
			TreeMap <Integer,Integer>tmapRankIdMap =  null;
			tmapRankIdMap =  new TreeMap<Integer,Integer>();
			for (Entry<Integer, Integer> mRes : iMp.entrySet()) {
				tmapRankIdMap.put(mRes.getKey(), mRes.getValue());
			}
			tmapRankIdMap.put(newRank-1,newRankId);
			tmapRankIdMap.put(oldRank, higherRankId);
			return tmapRankIdMap;
		}else{
			TreeMap <Integer,Integer>tmapRankIdMap =  null;
			tmapRankIdMap = orderTree(iMp,  newRank,  newRankId, oldRank);
			
			//Move the higher rank player up one position(Higher ranked player moves down the rank by 1)
			Map.Entry<Integer, Integer> nextRecord = ((TreeMap<Integer, Integer>) tmapRankIdMap).higherEntry(iHigherRankCurrentVal); //-- next record
    		if (nextRecord != null) {
    			tmapRankIdMap.put(nextRecord.getKey() -1, nextRecord.getValue());
    			tmapRankIdMap.put(iHigherRankCurrentVal +1, higherRankId);
    		}
    		return tmapRankIdMap;
		}
	}
	
	private static TreeMap <Integer,Integer> orderTree(TreeMap <Integer,Integer>iMp,Integer newRank, Integer newRankId,Integer oldRank){
		TreeMap <Integer,Integer>tmapRankIdMap =  new TreeMap<Integer,Integer>(); 
		for (Entry<Integer, Integer> iT : iMp.entrySet()) {
			if(newRank == iT.getKey()) {
				tmapRankIdMap.put(newRank, newRankId); 				// insert new Rank and Id here into the new Tree map
				tmapRankIdMap.put(newRank+1, iT.getValue());       // the current record at this position, increment the rank(key) and keep the ID(value the same)
				if(iMp.size() == tmapRankIdMap.size()) {
					return tmapRankIdMap;
				}
				
				for (Entry<Integer, Integer> iTinner : iMp.tailMap( iT.getKey()+2).entrySet()) {
					Map.Entry<Integer, Integer> prevRecord = ((TreeMap<Integer, Integer>) iMp).lowerEntry(iTinner.getKey()); // previous
					boolean doesItExists           = doesValueExist(tmapRankIdMap,iTinner.getValue());
					boolean doesItExistsInPre = doesValueExist(tmapRankIdMap,prevRecord.getValue());
						
					if(doesItExists) {
						tmapRankIdMap.put(iTinner.getKey(), prevRecord.getValue());
					}
					if(!doesItExists && doesItExistsInPre) {
						tmapRankIdMap.put(iTinner.getKey(), iTinner.getValue());
					}
					if(!doesItExists && !doesItExistsInPre) {
						tmapRankIdMap.put(iTinner.getKey(), prevRecord.getValue());
					}
				}
				break;
			}else {
				tmapRankIdMap.put(iT.getKey(), iT.getValue());
			}
		
		}
		return tmapRankIdMap;
		
	}
	
	private static boolean doesValueExist(TreeMap <Integer,Integer>tMap, int idValue) {
		boolean doesExist = false;
		for (Entry<Integer, Integer> iT : tMap.entrySet()) {
			if(iT.getValue() == idValue) {
				doesExist =true;
				break;
			}
		}
		return doesExist;
	}
}

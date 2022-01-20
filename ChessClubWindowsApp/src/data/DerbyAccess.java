package data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import helpers.AppHelpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DerbyAccess {
	
	/** Derby JDBC Connection URL **/
	private static final String JDBC_URL = "jdbc:derby:ChessClub;create=true";
	
	private static Statement staListUser 	= null;
	private static ResultSet resListUser 	= null;
	
	/** Getters and Setters **/
	public static Statement getStaListUser() 				 { return staListUser;	}
	public static void setStaListUser(Statement staListUser) { DerbyAccess.staListUser = staListUser;	}
	public static ResultSet getResListUser() 				 {	return resListUser;	}
	public static void setResListUser(ResultSet resListUser) {	DerbyAccess.resListUser = resListUser;	}
	
	
	/**
	 * @author pmelian
	 * @return Connection Object
	 * @throws Exception
	 * @Comment Open Connection to embedded Derby Data Base.
	 * 
	 */
	public static Connection connectToDb() throws Exception {
		Connection conn				 = null;
		try {
			conn = DriverManager.getConnection(JDBC_URL);
		} catch (SQLException e) {
			throw new Exception("Failed to connect to embedded Derby Data Base");
		}
		return conn;
	}
	
	/**
	 * @author pmelian
	 * @param conn
	 * @throws Exception
	 * @Comment 
	 *          Creates table 'CHESSPLAYERS' if it does not exists
	 *          with generated 'id' as UNIQUE identity
	 *          Columns::
	 *                   id 			( int )
	 *                   FullName 		( varchar (256) )
	 *                   EmailAddress   ( varchar (125) )
	 *                   BirthDay       ( varchar (126) )
	 *                   GamesPlayed	( int )
	 *                   Rank			( int )
	 *          
	 */
	public static void createDbTable(Connection conn) throws Exception {
		DatabaseMetaData metaData	= null;
		ResultSet resMetaData		= null;
		boolean bTableExists		= false;
		Statement stCreateTable    	= null;	
		String cTableSql ="Create Table CHESSPLAYERS ("
				+ "id int not null generated always as identity,"
				+ "FullName varchar(256),"
				+ "EmailAddress varchar(125),"
				+ "BirthDay varchar (126),"
				+ "GamesPlayed int,"
				+ "Rank int"
				+ ")";
		try {
			metaData = conn.getMetaData();
			resMetaData = metaData.getTables(null, null, "CHESSPLAYERS",   new String[] {"TABLE"});
			while (resMetaData.next()) {	 bTableExists =true;	 }
			if(!bTableExists) {
				stCreateTable = conn.createStatement();
				stCreateTable.executeLargeUpdate(cTableSql);
			}
		} catch (SQLException e) {
			throw new Exception("Derby create table exception," + e.getMessage());
		}finally {
			if(stCreateTable != null) {
				try {
					stCreateTable.close();
					stCreateTable = null;
				} catch (Throwable ignore) {}
				
				if(resMetaData != null) {
					try {
						resMetaData.close();
						resMetaData = null;
					} catch (Throwable ignore) {}
				}
			}
		}
	}
	
	/**
	 * @author pmelian
	 * @param conn 
	 * @Comment Close Derby Connection
	 */
	public static void closeDb(Connection conn) {
		/** Clean up static variables that may be in use before closing the DB connection */
		cleanUpStaticVar();
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (Throwable ignore) {}
		}
	}
	
	/**
	 * @author pmelian
	 * @comment
	 *         clean up static variables that may be in use.
	 */
	private static void cleanUpStaticVar() {
		if(getStaListUser() != null) {
			try {
				getStaListUser().close();
				setStaListUser(null);
			} catch (Throwable ignore) {}
		}
		if(getResListUser() != null) {
			try {
				getResListUser().close();
				setResListUser(null);
			} catch (Throwable ignore) {}
		}
	}
	
	/**
	 * 
	 * @param conn
	 * @throws Exception
	 * @comment
	 *         truncate table 'CHESSPLAYERS' and reset UNIQUE identity 'ID' back to 1 (int)
	 */
	public static void truncateTable(Connection conn) throws Exception {
		cleanUpStaticVar();
		PreparedStatement  pStTruncate 					= null;
		String truncateTableSql 						= "TRUNCATE TABLE CHESSPLAYERS ";
		String altTableSql 								= "ALTER TABLE CHESSPLAYERS ALTER COLUMN ID RESTART WITH 1 ";
		try {
			pStTruncate = conn.prepareStatement(truncateTableSql);
			pStTruncate.execute();
			pStTruncate.close();
			pStTruncate = conn.prepareStatement(altTableSql);
			pStTruncate.execute();
		}catch (SQLException x) {
			throw new Exception("failed to truncate table 'CHESSPLAYERS'," + x.getMessage());
		}finally {
			if (pStTruncate != null) {
				try {
					pStTruncate.close();
					pStTruncate = null;
				}catch (Throwable ignore) {}
			}
		}
	}
	
	
	/***
	 * @author pmelian
	 * @param conn
	 * @param name
	 * @param email
	 * @param birthDate
	 * @throws Exception
	 * @comment 
	 *         Insert new user into 'CHESSPLAYERS' table.
	 *         No records in table insert with first rank =1;
	 *         Validate that email address is unique,if not throw exception.
	 *         Next user rank , previous user rank +1.
	 *         
	 */
	public static void insertNewUser(Connection conn,String name ,String email,String birthDate) throws Exception {
		ResultSet rs = null;
		boolean bNewEntry = true;
		String insertSql 							= "INSERT INTO CHESSPLAYERS (FullName,EmailAddress,BirthDay,GamesPlayed,Rank) values(?,?,?,?,?)";
		PreparedStatement  pSt 						= null;
		List<String> emailAddressList 				= new ArrayList<String>();
		SortedMap <Integer,Integer>sortedIdRankMap 	= new TreeMap<Integer,Integer>();
		TreeMap <Integer,Integer>tRankIdMap 		= new TreeMap<Integer,Integer>();
		try {
			getAllTableRecords(conn);
			rs = DerbyAccess.getResListUser();
			while (rs.next()) {
				bNewEntry =false;
				emailAddressList.add(rs.getString("EmailAddress"));
				sortedIdRankMap.put(rs.getInt("id"), rs.getInt("Rank"));
				tRankIdMap.put(rs.getInt("Rank"), rs.getInt("id"));
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if(rs != null) {
				try {
					rs.close();
					rs =null;
				} catch (Throwable ignore) {}
			}
		}
		try {
			pSt = conn.prepareStatement(insertSql);
			pSt.setString(1, name);
			pSt.setString(2, email);
			pSt.setString(3,birthDate);
			pSt.setInt(4,0);
			/**We have no records in the data base.**/
			if(bNewEntry) {
				pSt.setInt(5,1);
			}else{
				if(emailAddressList.contains(email)) {
					throw new Exception("this email address has already been captured.");
				}
				Integer rankV   = tRankIdMap.lastKey();
				pSt.setInt(5,rankV+1);
			}
			pSt.executeUpdate();
		}catch(SQLException ee) {
			throw new Exception("Cannot insert item  into table 'CHESSPLAYERS' ," + ee.getMessage());
		}finally {
			if(pSt != null) {
				try {
					pSt.close();
					pSt =null;
				} catch (Throwable ignore) {}
			}
		}
	}
	
	/**
	 * @author pmelian
	 * @param conn
	 * @throws Exception
	 * @comment
	 *         Select all records in table 'CHESSPLAYERS' in Ascending order.
	 *         Populate static Resultset with query results.
	 *         
	 */
	public static void getAllTableRecords(Connection conn) throws Exception {
		cleanUpStaticVar();
		try {
			staListUser = conn.createStatement();
			resListUser = staListUser.executeQuery("SELECT * FROM CHESSPLAYERS ORDER BY Rank ASC");
		} catch (SQLException e) {
			throw new Exception("Cannot select items from table 'CHESSPLAYERS' ," + e.getMessage());
			
		}
	}
	
	/**
	 * @author pmelian
	 * @param conn
	 * @param sID
	 * @throws Exception
	 * @comments
	 *          Delete the user from table 'CHESSPLAYERS' 
	 *          Re-order TreeMap, remove ID from new TreeMap.
	 *          Commit/persist changed TreeMap in correct order to table 'CHESSPLAYERS'
	 */
	public static void deleteUser(Connection conn,int sID) throws Exception {
		ResultSet rs 								= null;
		boolean bHasRecordSet 						= false;
		TreeMap <Integer,Integer>tRankIdMap 	    = new TreeMap<Integer,Integer>();
		List<Integer> idList 						= new ArrayList<Integer>();
		PreparedStatement  pStDel 					= null;
		PreparedStatement  pStUpdate 				= null;
		String deleteSql 							= "DELETE FROM CHESSPLAYERS WHERE id = ?";
		String updateSql 							= "UPDATE CHESSPLAYERS SET RANK = ? WHERE ID =?";
		
		try {
			getAllTableRecords(conn);
			rs = DerbyAccess.getResListUser();
			while (rs.next()) {	
				bHasRecordSet =true;
				idList.add(rs.getInt("id"));
				tRankIdMap.put(rs.getInt("Rank"), rs.getInt("id"));
			}
			
			if(!bHasRecordSet) 			{	throw new Exception("No records exist to delete.");								}
			if(!idList.contains(sID))	{	throw new Exception("Passed 'User System Id' does not exist in Data Base,");	}
			
			try {
				pStDel = conn.prepareStatement(deleteSql);
				pStDel.setInt(1,sID);
				pStDel.executeUpdate();
			}catch (SQLException x) {
				throw new Exception("failed to delete record from Data Base," + x.getMessage());
			}
			
			for (Entry<Integer, Integer> sE : AppHelpers.deleteAndReorderMap(tRankIdMap, sID).entrySet()) {
				if(pStUpdate != null) {
					try {
						pStUpdate.close();
						pStUpdate =null;
					} catch (SQLException e1) {
						throw new Exception("failed to close prepared statement," + e1.getMessage());
					}
				}
				try {
					pStUpdate = conn.prepareStatement(updateSql);
					pStUpdate.setInt(1,sE.getKey());
					pStUpdate.setInt(2,sE.getValue());
					pStUpdate.executeUpdate();
				}catch(SQLException e2) {
					throw new Exception("failed to update Data Base after delete record," + e2.getMessage());
				}
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if(rs != null) {
				try {
					rs.close();
					rs =null;
				} catch (Throwable ignore) {}
			}
			if(pStDel != null) {
				try {
					pStDel.close();
					pStDel =null;
				} catch (Throwable ignore) {}
			}
			if(pStUpdate != null) {
				try {
					pStUpdate.close();
					pStUpdate =null;
				} catch (Throwable ignore) {}
			}
		}
	}

}

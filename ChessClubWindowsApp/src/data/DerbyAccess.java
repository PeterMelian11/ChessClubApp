package data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

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
	
	public static void cleanUpStaticVar() {
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

}

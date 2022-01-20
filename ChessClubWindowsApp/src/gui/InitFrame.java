package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import data.DerbyAccess;
import helpers.AppHelpers;
import net.proteanit.sql.DbUtils;

import java.sql.Connection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InitFrame {

	private JFrame frame;
	private JLabel dbLabelMsg 	= null;
    private Connection conn 	= null;
    private JTable table;
    private JTextField txtAsdas;
    private JTextField txtXxgamilcom;
    private JTextField textField_BithDate;
    private JTextField textField_Delete;
    private JTextField textField_MatchPlayer_1_Id;
    private JTextField textField_MatchPlayer_2_Id;
    private JScrollPane scrollPaneListTable;
    private JComboBox comboBoxResult;
    private JButton btnButton_List ;
    private JLabel lblNewLabel;
    private JLabel lblEmailAddress;
    private JLabel lblBirthday;
    private JButton btnAddUser;
    private JButton btnButtonDelete;
    private JLabel labelDeleteUser;
    private JLabel lblNewLabel_1;
    private JLabel lbLabelPlayer1;
    private JLabel lbLabelPlayer2 ;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitFrame window = new InitFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InitFrame() {
		initialize();
		try {
			/***
			 * Connect to DB and Create Table if it does not exist. 
			 * If exception occurs throw message back to Message Dialog Window and close the application window. 
			 * **/
			conn = DerbyAccess.connectToDb();
			DerbyAccess.createDbTable(conn);
			dbLabelMsg.setText("Connected to embedded Derby Data Base. (Table CHESSPLAYERS)");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Derby exception," +e.getMessage() +",closing application.");
			DerbyAccess.closeDb(conn);
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		
		btnButton_List = new JButton("List Users");
		btnButton_List.setBounds(27, 11, 116, 23);
		frame.getContentPane().add(btnButton_List);
		
		scrollPaneListTable = new JScrollPane();
		scrollPaneListTable.setBounds(27, 40, 554, 335);
		frame.getContentPane().add(scrollPaneListTable);
		
		table = new JTable();
		scrollPaneListTable.setViewportView(table);
		
		txtAsdas = new JTextField();
		txtAsdas.setBounds(27, 386, 193, 20);
		frame.getContentPane().add(txtAsdas);
		txtAsdas.setColumns(10);
		
		txtXxgamilcom = new JTextField();
		txtXxgamilcom.setColumns(10);
		txtXxgamilcom.setBounds(27, 417, 193, 20);
		frame.getContentPane().add(txtXxgamilcom);
		
		textField_BithDate = new JTextField();
		textField_BithDate.setColumns(10);
		textField_BithDate.setBounds(27, 448, 193, 20);
		frame.getContentPane().add(textField_BithDate);
		
		lblNewLabel = new JLabel("Name And Surname");
		lblNewLabel.setBounds(230, 392, 143, 14);
		frame.getContentPane().add(lblNewLabel);
		
		lblEmailAddress = new JLabel("Email Address");
		lblEmailAddress.setBounds(230, 423, 143, 14);
		frame.getContentPane().add(lblEmailAddress);
		
		lblBirthday = new JLabel("Birthday");
		lblBirthday.setBounds(230, 451, 143, 14);
		frame.getContentPane().add(lblBirthday);
		
		btnAddUser = new JButton("Add User");
		btnAddUser.setBounds(27, 479, 89, 23);
		frame.getContentPane().add(btnAddUser);
		
		btnButtonDelete = new JButton("Delete User");
		btnButtonDelete.setBounds(611, 49, 127, 23);
		frame.getContentPane().add(btnButtonDelete);
		
		labelDeleteUser = new JLabel("User System Id");
		labelDeleteUser.setBounds(751, 86, 98, 14);
		frame.getContentPane().add(labelDeleteUser);
		
		textField_Delete = new JTextField();
		textField_Delete.setColumns(10);
		textField_Delete.setBounds(611, 83, 127, 20);
		frame.getContentPane().add(textField_Delete);
		
		lblNewLabel_1 = new JLabel("Match Play");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 35));
		lblNewLabel_1.setBounds(614, 140, 235, 43);
		frame.getContentPane().add(lblNewLabel_1);
		
		lbLabelPlayer1 = new JLabel("Lower Ranked Player Id");
		lbLabelPlayer1.setBounds(611, 194, 141, 14);
		frame.getContentPane().add(lbLabelPlayer1);
		
		lbLabelPlayer2 = new JLabel("Higher Ranked Player Id");
		lbLabelPlayer2.setBounds(777, 194, 148, 14);
		frame.getContentPane().add(lbLabelPlayer2);
		
		textField_MatchPlayer_1_Id = new JTextField();
		textField_MatchPlayer_1_Id.setBounds(611, 219, 116, 20);
		frame.getContentPane().add(textField_MatchPlayer_1_Id);
		textField_MatchPlayer_1_Id.setColumns(10);
		
		textField_MatchPlayer_2_Id = new JTextField();
		textField_MatchPlayer_2_Id.setBounds(777, 219, 116, 20);
		frame.getContentPane().add(textField_MatchPlayer_2_Id);
		textField_MatchPlayer_2_Id.setColumns(10);
		
		lblNewLabel_2 = new JLabel("vs");
		lblNewLabel_2.setBounds(740, 222, 27, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		String  [] comboBoxList = {"LOSE","DRAW","WIN"};
		comboBoxResult = new JComboBox(comboBoxList);
		comboBoxResult.setBounds(611, 275, 86, 23);
		frame.getContentPane().add(comboBoxResult);
		
		lblNewLabel_3 = new JLabel("Lower Ranked Player Outcome");
		lblNewLabel_3.setBounds(611, 250, 187, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		btnNewButton = new JButton("Truncate Table");
		btnNewButton.setBounds(611, 11, 127, 23);
		frame.getContentPane().add(btnNewButton);
		
		
		/** Windows Event handlers*/
		
		/*** Truncate table ***/
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					DerbyAccess.truncateTable(conn);
					JOptionPane.showMessageDialog(frame, "Table Truncated.");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
			}
		});
		
		/**Add User event handler**/
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name 		= null;
				String email 		= null;
				String birthDate 	= null;
				try {
					name  = AppHelpers.isStringEmpty("Text Box 'Name And Surname'", (txtAsdas.getText()).toUpperCase());
					email = AppHelpers.isValidEmailAddress("Text Box 'Email Address'", (txtXxgamilcom.getText()).toLowerCase());
					birthDate = AppHelpers.validateAndParseBirthDate("Text Box 'Birthday'",textField_BithDate.getText());
					DerbyAccess.insertNewUser(conn,name,email,birthDate);
					JOptionPane.showMessageDialog(frame, "User added.");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				} 
			}
		});
		
		/**List  Users event handler**/
		btnButton_List.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					DerbyAccess.getAllTableRecords(conn);
					table.setModel(DbUtils.resultSetToTableModel(DerbyAccess.getResListUser()));
				} catch (Exception e) {
					dbLabelMsg.setText(e.getMessage());
				}
			}
		});
		
		/**Delete User event handler**/
		btnButtonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int sID;
				try {
					sID  = AppHelpers.isInteger("Text Box 'User System Id'", (textField_Delete.getText()).toUpperCase());
					DerbyAccess.deleteUser(conn,sID);
					JOptionPane.showMessageDialog(frame, "User deleted.");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				} 
			}
		});
		
		/**Match Play **/
		comboBoxResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sID_Player1;
				int sID_Player2;
				String sPalyer1Result = "";
				String resultMsg      ="";
				try {
					sID_Player1  = AppHelpers.isInteger("Text Box 'Player 1 ID'", (textField_MatchPlayer_1_Id.getText()).toUpperCase());
					sID_Player2  = AppHelpers.isInteger("Text Box 'Player 2 ID'", (textField_MatchPlayer_2_Id.getText()).toUpperCase());
					if(sID_Player1 == sID_Player2) {	throw new Exception("'Player 1 ID' cannot be the same as 'Player 2 ID'"); }
					
					sPalyer1Result = (String) comboBoxResult.getSelectedItem();
					resultMsg = DerbyAccess.matchPlayEvaluate(conn,sID_Player1,sID_Player2,sPalyer1Result);
					JOptionPane.showMessageDialog(frame,resultMsg);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				} 
				
			}
		});
	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			/**Close db connection on Windows Closing event.***/
			@Override
			public void windowClosing(WindowEvent arg0) {
				DerbyAccess.closeDb(conn);
			}
		});
		frame.setBounds(100, 100, 959, 569);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		dbLabelMsg = new JLabel("..Initilizing DB");
		dbLabelMsg.setBounds(153, 15, 420, 14);
		frame.getContentPane().add(dbLabelMsg);
	}

}

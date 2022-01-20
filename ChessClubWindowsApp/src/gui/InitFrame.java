package gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import data.DerbyAccess;

import java.sql.Connection;

public class InitFrame {

	private JFrame frame;
	private JLabel dbLabelMsg 	= null;
	private Connection conn 	= null;

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
	public InitFrame() {
		initialize();
		
		try {
			conn = DerbyAccess.connectToDb();
			DerbyAccess.createDbTable(conn);
			dbLabelMsg.setText("Connected to embedded Derby Data Base. (Table CHESSPLAYERS)");
		} catch (Exception e) {
			dbLabelMsg.setText(e.getMessage());
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.out.println("Closing ..................................");
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

package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import connection.Sever;
import controller.LoginController;
import panel.LoginPane;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public Main() {
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		this.setContentPane(contentPane);
		
		LoginPane loginPane = new LoginPane();
		contentPane.add(loginPane, BorderLayout.CENTER);
		
		LoginController loginController = new LoginController(loginPane);
		loginController.setEvent(this, contentPane);
		
		setIconImage(Sever.icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Phần mềm quản lý nhà hàng - Restaurant Manager".toUpperCase());
		setSize(600, 550);
		setLocationRelativeTo(this);
		setVisible(true);
	}
	
	public static void main(String[] args) {	
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Sever sever = new Sever();
					sever.checkTime();
					
					Main main = new Main();
					main.addWindowListener(new WindowAdapter() {
						
						@Override
						public void windowClosing(WindowEvent e) {
							super.windowClosing(e);
							try {
								Sever.connection.close();
							} catch (SQLException ex) {
								ex.printStackTrace();
								System.exit(1);
							}
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}

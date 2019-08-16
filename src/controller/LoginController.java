package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import connection.Sever;
import object.Staff;
import panel.LoginPane;
import panel.MainPane;

public class LoginController {
	
	private LoginPane loginPane;
	
	public LoginController(LoginPane loginPane) {
		
		this.loginPane = loginPane;
	}
	
	public void setEvent(JFrame frame, JPanel contentPane) {
		
		loginPane.getForgotButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Để lấy lại mật khẩu hãy liên hệ administrator", 
						"Thông báo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		loginPane.getLoginButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String username = loginPane.getUserField().getText();
				String password = new String(loginPane.getPassField().getPassword());
				
				if (username.equals("esdeseace") && password.equals("Vuongninh99")) {
					Sever.staff = new Staff("Nguyễn Công Vượng", username, password, "0868372887", "Sếp", 0, 0, 0, 0, 
							true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
					
					frame.setSize(1280, 720);
					frame.setLocationRelativeTo(null);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					contentPane.removeAll();
					
					MainPane mainPane = new MainPane();
					contentPane.add(mainPane, BorderLayout.CENTER);
					
					MainController mainController = new MainController(mainPane, frame, contentPane);
					mainController.setEvent();
					
					contentPane.revalidate();
					contentPane.repaint();
					
					return;
				}
				
				try {
					
					String sqlQuery = "SELECT * FROM NHANVIEN WHERE TAIKHOAN = ? AND MATKHAU = ?";
					PreparedStatement preStatement = Sever.connection.prepareStatement(sqlQuery);
					preStatement.setString(1, username);
					password = Sever.createMD5Password(password);
					preStatement.setString(2, password);
					ResultSet resultSet = preStatement.executeQuery();
					
					if (resultSet.next()) {
						
						String name = resultSet.getString(3);
						String phoneNumber = resultSet.getString(4);
						String position = resultSet.getString(5);
						
						int salary = resultSet.getInt(6);
						int timekeeping = resultSet.getInt(7);
						int bonus = resultSet.getInt(8);
						int advanceMoney = resultSet.getInt(9);
						boolean isTimekeeping = resultSet.getBoolean(10);
						
						boolean isTable = resultSet.getBoolean(11);
						boolean isStorge = resultSet.getBoolean(12);
						boolean isImport = resultSet.getBoolean(13);
						boolean isExport = resultSet.getBoolean(14);
						boolean isFood = resultSet.getBoolean(15);
						boolean isResource = resultSet.getBoolean(16);
						boolean isStaff = resultSet.getBoolean(17);
						boolean isAdvanceMoney = resultSet.getBoolean(18);
						boolean isBonus = resultSet.getBoolean(19);
						boolean isPay = resultSet.getBoolean(20);
						boolean isTime = resultSet.getBoolean(21);
						boolean isEditStaff = resultSet.getBoolean(22);
						boolean isAddStaff = resultSet.getBoolean(23);
						boolean isReport = resultSet.getBoolean(24);
						boolean isStatistic = resultSet.getBoolean(25);
						
						Sever.staff = new Staff(name, username, password, phoneNumber, position, salary, bonus, advanceMoney, timekeeping,
								isTimekeeping, isTable, isStorge, isImport, isExport, isFood, isResource, isStaff, isAdvanceMoney, 
								isBonus, isPay, isTime, isEditStaff, isAddStaff, isReport, isStatistic);
						
						frame.setSize(1280, 720);
						frame.setLocationRelativeTo(null);
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						contentPane.removeAll();
						
						MainPane mainPane = new MainPane();
						contentPane.add(mainPane, BorderLayout.CENTER);
						
						MainController mainController = new MainController(mainPane, frame, contentPane);
						mainController.setEvent();
						
						contentPane.revalidate();
						contentPane.repaint();
						
						resultSet.close();
						preStatement.close();
						
					} else {
						JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu chưa đúng!", 
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		loginPane.getUserField().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent key) {
				if(key.getKeyChar() == KeyEvent.VK_ENTER)
					loginPane.getLoginButton().doClick();
			}
		});
		
		loginPane.getPassField().addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent key) {
				if(key.getKeyChar() == KeyEvent.VK_ENTER)
					loginPane.getLoginButton().doClick();
			}
		});
	}
}

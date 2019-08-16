package panel;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class LoginPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField userField;
	private JPasswordField passField;
	private JButton loginButton;
	private JButton forgotButton;
	
	public JTextField getUserField() {
		return this.userField;
	}
	
	public JPasswordField getPassField() {
		return this.passField;
	}
	
	public JButton getLoginButton() {
		return this.loginButton;
	}
	
	public JButton getForgotButton() {
		return this.forgotButton;
	}
	
	public LoginPane() {
		
		setBackground(new Color(54, 57, 63));
		setLayout(null);
		
		JLabel mainBanner = new JLabel("Chào mừng bạn!");
		mainBanner.setForeground(Color.WHITE);
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		add(mainBanner);
		
		JLabel extraBanner = new JLabel("Đăng nhập vào tài khoản ngay.");
		extraBanner.setHorizontalAlignment(SwingConstants.CENTER);
		extraBanner.setForeground(new Color(99, 103, 110));
		extraBanner.setFont(new Font("Calibri", Font.PLAIN, 20));
		extraBanner.setBounds(30, 74, 524, 26);
		add(extraBanner);
		
		JLabel userLabel = new JLabel("TÀI KHOẢN");
		userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		userLabel.setForeground(new Color(255, 255, 255));
		userLabel.setBounds(75, 155, 200, 19);
		add(userLabel);
		
		JLabel passLabel = new JLabel("MẬT KHẨU");
		passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		passLabel.setForeground(Color.WHITE);
		passLabel.setBounds(75, 255, 200, 19);
		add(passLabel);
		
		loginButton = new JButton("Đăng nhập");
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		loginButton.setBounds(75, 403, 449, 49);
		add(loginButton);
		
		forgotButton = new JButton("Quên mật khẩu?");
		forgotButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		forgotButton.setBorderPainted(false);
		forgotButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		forgotButton.setContentAreaFilled(false);
		forgotButton.setOpaque(false);
		forgotButton.setForeground(new Color(114, 137, 218));
		forgotButton.setBounds(75, 344, 163, 21);
		add(forgotButton);
		
		JPanel userPanel = new JPanel();
		userPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		userPanel.setBounds(75, 183, 449, 44);
		add(userPanel);
		
		userPanel.setLayout(new BorderLayout(0, 0));
		userField = new JTextField();
		userField.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e) {
				userPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(114, 137, 218)));
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				userPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
			}
		});
		userField.setFont(new Font("Calibri", Font.BOLD, 20));
		userField.setBorder(new EmptyBorder(0, 15, 0, 0));
		userField.setCaretColor(new Color(114, 137, 218));
		userField.setForeground(Color.WHITE);
		userField.setBackground(new Color(48, 51, 57));
		userPanel.add(userField);
		userField.setColumns(10);
		
		JPanel passPanel = new JPanel();
		passPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		passPanel.setBounds(75, 287, 449, 44);
		passPanel.setLayout(new BorderLayout(0, 0));
		add(passPanel);
		
		passField = new JPasswordField();
		passField.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e) {
				passPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(114, 137, 218)));
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				passPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
			}
		});
		passField.setFont(new Font("Calibri", Font.BOLD, 20));
		passField.setBorder(new EmptyBorder(0, 15, 0, 0));
		passField.setCaretColor(new Color(114, 137, 218));
		passField.setForeground(Color.WHITE);
		passField.setBackground(new Color(48, 51, 57));
		passPanel.add(passField);
	}
}

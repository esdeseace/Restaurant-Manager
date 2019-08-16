package dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import connection.Sever;
import model.StaffModel;

public class PasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JButton button;
	
	public PasswordDialog(StaffModel staffModel) {

		setIconImage(Sever.icon.getImage());
		JPanel contentPane = new JPanel();
		setSize(546, 456);
		setTitle("ĐỔI MẬT KHẨU");
		setLocationRelativeTo(this);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel mainBanner = new JLabel("ĐỔI MẬT KHẨU");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(12, 28, 504, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(mainBanner);

		JLabel oldPassLabel = new JLabel("Mật khẩu cũ:");
		oldPassLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		oldPassLabel.setBounds(53, 113, 108, 19);
		contentPane.add(oldPassLabel);
		
		JPasswordField oldPassField = new JPasswordField();
		oldPassField.setFont(new Font("Calibri", Font.BOLD, 20));
		oldPassField.setBorder(new EmptyBorder(0, 15, 0, 0));
		oldPassField.setBounds(53, 132, 426, 44);
		contentPane.add(oldPassField);
		
		JLabel newPassLabel = new JLabel("Mật khẩu mới:");
		newPassLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		newPassLabel.setBounds(53, 207, 108, 19);
		contentPane.add(newPassLabel);
		
		JPasswordField newPassField = new JPasswordField();
		newPassField.setFont(new Font("Calibri", Font.BOLD, 20));
		newPassField.setBorder(new EmptyBorder(0, 15, 0, 0));
		newPassField.setBounds(53, 226, 426, 44);
		contentPane.add(newPassField);
		
		button = new JButton("Đổi mật khẩu");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String oldPassword = new String(oldPassField.getPassword());
				String newPassword = new String(newPassField.getPassword());
				String md5OldPassword = Sever.createMD5Password(oldPassword);
				
				if ( md5OldPassword.equals( Sever.staff.getPassword() ) ) {
					if (newPassword.isEmpty())
						JOptionPane.showMessageDialog(null, "Mật khẩu mới không được trống!", 
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					else {
					
						int isOk = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc chắn không?!", 
								"Đổi mật khẩu", JOptionPane.YES_NO_OPTION);
						
						if (isOk == 0)
							staffModel.editStaff(Sever.staff, newPassword);
						
						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Mật khẩu cũ không khớp!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(53, 317, 426, 49);
		contentPane.add(button);	
	}
	
}

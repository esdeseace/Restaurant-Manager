package dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import connection.Sever;
import controller.StaffController;
import model.StaffModel;
import object.Staff;
import subComponent.MoneyTextField;

public class PayWageDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel mainBanner;
	private JButton button;
	
	private JTextField userField;
	private JTextField nameField;
	private MoneyTextField moneyField;
	private JComboBox<Integer> timeCombo;
	
	private Staff staff;
	private StaffController staffController;
	private StaffModel staffModel;

	public PayWageDialog(StaffController staffController) {
		
		this.staffController = staffController;
		this.staffModel = staffController.getStaffModel();
		this.staff = staffController.getSelectedStaff();
		
		setIconImage(Sever.icon.getImage());
		
		setView();
		setEvent();
		
		userField.setText(staff.getUsername());
		nameField.setText(staff.getName());
		moneyField.setText( Sever.numberFormat.format( staff.getRealSalary() ) );
	}

	private void setEvent() {
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int isOk = JOptionPane.showConfirmDialog(null, 
						"Việc này không thể hoàn tác. Bạn có chắc chắn không?!", 
						"Trả lương", JOptionPane.YES_NO_OPTION);
				
				if (isOk == 0) {
					int timekeeping = (int) timeCombo.getSelectedItem();
					staffModel.payWage(staff, timekeeping);
					staffController.updateMainPane();
				}
				dispose();
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(619, 488);
		setTitle("TRẢ LƯƠNG".toUpperCase());
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mainBanner = new JLabel("TRẢ LƯƠNG");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(mainBanner);
		
		JLabel usernameLabel = new JLabel("Tài khoản:");
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		usernameLabel.setBounds(75, 85, 108, 19);
		getContentPane().add(usernameLabel);
		
		userField = new JTextField();
		userField.setEditable(false);
		userField.setFont(new Font("Calibri", Font.BOLD, 20));
		userField.setBorder(new EmptyBorder(0, 15, 0, 0));
		userField.setBounds(75, 104, 449, 44);
		contentPane.add(userField);
		
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameField.setBounds(75, 190, 449, 44);
		contentPane.add(nameField);
		
		JLabel nameLabel = new JLabel("Họ tên:");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameLabel.setBounds(75, 171, 108, 19);
		contentPane.add(nameLabel);
		
		moneyField = new MoneyTextField();
		moneyField.setEditable(false);
		moneyField.setFont(new Font("Calibri", Font.BOLD, 20));
		moneyField.setBorder(new EmptyBorder(0, 15, 0, 0));
		moneyField.setBounds(75, 266, 264, 44);
		contentPane.add(moneyField);
		
		JLabel moneyLabel = new JLabel("Số tiền:");
		moneyLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		moneyLabel.setBounds(75, 247, 93, 19);
		contentPane.add(moneyLabel);
		
		JLabel note = new JLabel("Lưu ý: Số tiền = Lương + Tiền thưởng - Tiền ứng");
		note.setHorizontalAlignment(SwingConstants.CENTER);
		note.setFont(new Font("Tahoma", Font.BOLD, 15));
		note.setBounds(0, 323, 601, 32);
		contentPane.add(note);
		
		JLabel timeLabel = new JLabel("Số công:");
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		timeLabel.setBounds(416, 247, 108, 19);
		contentPane.add(timeLabel);
		
		timeCombo = new JComboBox<>();
		timeCombo.setFont(new Font("Calibri", Font.BOLD, 20));
		timeCombo.setBorder(new EmptyBorder(0, 15, 0, 0));
		timeCombo.setBounds(416, 266, 108, 44);
		contentPane.add(timeCombo);
		
		for (int index = 0; index < 100; index++)
			timeCombo.addItem(new Integer(index));
		
		button = new JButton("Trả lương");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 368, 449, 49);
		getContentPane().add(button);
	}
}

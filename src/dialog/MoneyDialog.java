package dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
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

public class MoneyDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel mainBanner;
	private JButton button;
	
	private JTextField userField;
	private JTextField nameField;
	private MoneyTextField moneyField;
	private JTextField giveField;
	
	private Staff staff;
	private StaffController staffController;
	private StaffModel staffModel;
	private boolean isbonus;

	public MoneyDialog(StaffController staffController, boolean isbonus) {
		
		this.staffController = staffController;
		this.isbonus = isbonus;
		this.staffModel = staffController.getStaffModel();
		this.staff = staffController.getSelectedStaff();
		setIconImage(Sever.icon.getImage());
		setView();
		setEvent();
		
		userField.setText(staff.getUsername());
		nameField.setText(staff.getName());
		
		if (isbonus) {
			button.setText("Thưởng tiền");
			mainBanner.setText("THƯỞNG TIỀN");
			setTitle("THƯỞNG TIỀN".toUpperCase());
		} else{	
			setTitle("ỨNG tiền".toUpperCase());
		}
	}

	private void setEvent() {
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String moneyString = moneyField.getText();
				String give = giveField.getText();
				
				if (moneyString.isEmpty())
					JOptionPane.showMessageDialog(null, "Số tiền không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (give.isEmpty())
					JOptionPane.showMessageDialog(null, "Họ tên người cho không được để trống !",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else {
					
					try {
						Number number = Sever.numberFormat.parse(moneyString);
						int money = number.intValue();
						
						int isOk = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc chắn không?!", 
								"Ứng/Thưởng tiền", JOptionPane.YES_NO_OPTION);
						
						if (isOk == 0) {
							if (isbonus)
								staffModel.addBonus(staff, money, give);
							else
								staffModel.addAdvanceMoney(staff, money, give);
							staffController.updateMainPane();
						}
						dispose();
					} catch (ParseException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Giá tiền không hợp lệ! Vui lòng xem lại!!",
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(619, 533);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mainBanner = new JLabel("ỨNG TIỀN");
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
		moneyField.setFont(new Font("Calibri", Font.BOLD, 20));
		moneyField.setBorder(new EmptyBorder(0, 15, 0, 0));
		moneyField.setBounds(75, 266, 449, 44);
		contentPane.add(moneyField);
		
		JLabel moneyLabel = new JLabel("Số tiền:");
		moneyLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		moneyLabel.setBounds(75, 247, 449, 19);
		contentPane.add(moneyLabel);
		
		button = new JButton("Ứng tiền");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 409, 449, 49);
		getContentPane().add(button);
		
		giveField = new JTextField();
		giveField.setFont(new Font("Calibri", Font.BOLD, 20));
		giveField.setBorder(new EmptyBorder(0, 15, 0, 0));
		giveField.setBounds(75, 342, 449, 44);
		contentPane.add(giveField);
		
		JLabel giveLabel = new JLabel("Nhân viên thực hiện:");
		giveLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		giveLabel.setBounds(75, 323, 246, 19);
		contentPane.add(giveLabel);
	}
}

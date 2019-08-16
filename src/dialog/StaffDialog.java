package dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import connection.Sever;
import controller.StaffController;
import model.StaffModel;
import object.Staff;
import subComponent.MoneyTextField;

public class StaffDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JButton button;
	private JLabel mainBanner;
	private JTextField userField;
	private JTextField phoneField;
	private JTextField nameField;
	private JTextField positionField;
	private JPasswordField passField;
	private MoneyTextField salaryField;
	
	private StaffModel staffModel;
	private StaffController staffController;
	
	private Staff staff;
	private boolean isEdit;

	private JCheckBox tableCheckBox;
	private JCheckBox storgeCheckBox;
	private JCheckBox importCheckBox;
	private JCheckBox exportCheckBox;
	private JCheckBox foodCheckBox;
	private JCheckBox resourceCheckBox;
	private JCheckBox reportCheckBox;
	private JCheckBox statisticCheckBox;
	private JCheckBox staffCheckBox;
	private JCheckBox advanceCheckBox;
	private JCheckBox bonusCheckBox;
	private JCheckBox payCheckBox;
	private JCheckBox timeCheckBox;
	private JCheckBox editCheckBox;
	private JCheckBox addCheckBox;
	
	public StaffDialog(StaffController staffController, boolean isEdit) {
		setIconImage(Sever.icon.getImage());
		this.staffModel = staffController.getStaffModel();
		this.staffController = staffController;
		this.isEdit = isEdit;
		
		setView();
		setEvent();
		
		if (isEdit) {
			
			staff = staffController.getSelectedStaff();
			
			button.setText("Sửa Tài Khoản");
			mainBanner.setText("SỬA TÀI KHOẢN");
			setTitle("Sửa tài khoản".toUpperCase());
			
			String username = staff.getUsername();
			String password = staff.getPassword();
			String phoneNumber = staff.getPhoneNumber();
			String name = staff.getName();
			String salary = Sever.numberFormat.format( staff.getSalary() );
			String position = staff.getPosition();
			
			nameField.setText(name);
			userField.setText(username);
			passField.setText(password);
			phoneField.setText(phoneNumber);
			salaryField.setText(salary);
			positionField.setText(position);
			
			tableCheckBox.setSelected( staff.isTable );
			storgeCheckBox.setSelected( staff.isStorge );
			importCheckBox.setSelected( staff.isImport );
			exportCheckBox.setSelected( staff.isExport );
			foodCheckBox.setSelected( staff.isFood );
			resourceCheckBox.setSelected( staff.isResource );
			reportCheckBox.setSelected( staff.isReport );
			statisticCheckBox.setSelected( staff.isStatistic );
			staffCheckBox.setSelected( staff.isStaff );
			advanceCheckBox.setSelected( staff.isAdvanceMoney );
			bonusCheckBox.setSelected( staff.isBonus );
			payCheckBox.setSelected( staff.isPay );
			timeCheckBox.setSelected( staff.isTime );
			editCheckBox.setSelected( staff.isEditStaff );
			addCheckBox.setSelected( staff.isAddStaff );
			
			if ( staff.isStorge ) {
				importCheckBox.setVisible(true);
				exportCheckBox.setVisible(true);
			}
			
			if ( staff.isStaff ) {
				advanceCheckBox.setVisible(true);
				bonusCheckBox.setVisible(true);
				payCheckBox.setVisible(true);
				timeCheckBox.setVisible(true);
				editCheckBox.setVisible(true);
				addCheckBox.setVisible(true);
			}
			
		} else {
			setTitle("Thêm tài khoản".toUpperCase());
		}
	}
	
	private void setEvent() {
		
		storgeCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (storgeCheckBox.isSelected()) {
					importCheckBox.setVisible(true);
					exportCheckBox.setVisible(true);
				} else {
					importCheckBox.setVisible(false);
					exportCheckBox.setVisible(false);
				}
			}
		});
		
		staffCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (staffCheckBox.isSelected()) {
					advanceCheckBox.setVisible(true);
					bonusCheckBox.setVisible(true);
					payCheckBox.setVisible(true);
					timeCheckBox.setVisible(true);
					editCheckBox.setVisible(true);
					addCheckBox.setVisible(true);
				} else {
					advanceCheckBox.setVisible(false);
					bonusCheckBox.setVisible(false);
					payCheckBox.setVisible(false);
					timeCheckBox.setVisible(false);
					editCheckBox.setVisible(false);
					addCheckBox.setVisible(false);
				}
			}
		});
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				String name = nameField.getText();
				String username = userField.getText();
				String password = new String(passField.getPassword());
				String phoneNumber = phoneField.getText();
				String position = positionField.getText();
				String salaryString = salaryField.getText();
				
				if (name.isEmpty())
					JOptionPane.showMessageDialog(null, "Họ và tên không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (username.isEmpty())
					JOptionPane.showMessageDialog(null, "Tài khoản không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (password.isEmpty())
					JOptionPane.showMessageDialog(null, "Mật khẩu không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (phoneNumber.isEmpty())
					JOptionPane.showMessageDialog(null, "Số điện thọai không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (!phoneNumber.matches("\\d+"))
					JOptionPane.showMessageDialog(null, "Số điện thọai không hợp lệ!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (position.isEmpty())
					JOptionPane.showMessageDialog(null, "Chức vụ không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (salaryString.isEmpty())
					JOptionPane.showMessageDialog(null, "Lương không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (!isEdit && staffModel.getStaffByUsername(username) != null)
					JOptionPane.showMessageDialog(null, "Tài khoản bị trùng!!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else {
					try {
						
						Number number = Sever.numberFormat.parse(salaryString);
						int salary = number.intValue();
						
						int isOk = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc chắn không?!", 
								"Thêm/Sửa tài khoản", JOptionPane.YES_NO_OPTION);
						
						if (isOk == 0) {
							
							boolean isTable = tableCheckBox.isSelected();
							boolean isStorge = storgeCheckBox.isSelected();
							boolean isImport = importCheckBox.isSelected();
							boolean isExport = exportCheckBox.isSelected();
							boolean isFood = foodCheckBox.isSelected();
							boolean isResource = resourceCheckBox.isSelected();
							boolean isReport = reportCheckBox.isSelected();
							boolean isStatistic = statisticCheckBox.isSelected();
							boolean isStaff = staffCheckBox.isSelected();
							boolean isAdvanceMoney = advanceCheckBox.isSelected();
							boolean isBonus = bonusCheckBox.isSelected();
							boolean isPay = payCheckBox.isSelected();
							boolean isTime = timeCheckBox.isSelected();
							boolean isEditStaff = editCheckBox.isSelected();
							boolean isAddStaff = addCheckBox.isSelected();
							
							if (isEdit)
								staffModel.editStaff(staff, name, password, phoneNumber, position, salary,
										 isTable, isStorge, isImport, isExport, isFood,
										 isResource, isStaff, isAdvanceMoney, isBonus, isPay,
										 isTime, isEditStaff, isAddStaff, isReport, isStatistic);
							else
								staffModel.addStaff(name, username, password, phoneNumber, position, salary,
										isTable, isStorge, isImport, isExport, isFood,
										isResource, isStaff, isAdvanceMoney, isBonus, isPay,
										isTime, isEditStaff, isAddStaff, isReport, isStatistic);
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
		setSize(643, 759);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mainBanner = new JLabel("THÊM TÀI KHOẢN");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(mainBanner);
		
		JLabel userLabel = new JLabel("Tài khoản:");
		userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		userLabel.setBounds(75, 85, 108, 19);
		getContentPane().add(userLabel);
		
		userField = new JTextField();
		userField.setFont(new Font("Calibri", Font.BOLD, 20));
		userField.setBorder(new EmptyBorder(0, 15, 0, 0));
		userField.setBounds(75, 104, 466, 44);
		contentPane.add(userField);
		
		JLabel passLabel = new JLabel("Mật khẩu:");
		passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		passLabel.setBounds(75, 178, 108, 19);
		contentPane.add(passLabel);
		
		passField = new JPasswordField();
		passField.setFont(new Font("Calibri", Font.BOLD, 20));
		passField.setBorder(new EmptyBorder(0, 15, 0, 0));
		passField.setBounds(75, 197, 466, 44);
		contentPane.add(passField);
		
		JLabel nameLabel = new JLabel("Họ và Tên:");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameLabel.setBounds(75, 263, 108, 19);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameField.setBounds(75, 285, 466, 44);
		contentPane.add(nameField);
		
		JLabel phoneLabel = new JLabel("Số điện thoại:");
		phoneLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		phoneLabel.setBounds(75, 358, 148, 19);
		getContentPane().add(phoneLabel);
		
		phoneField = new JTextField();
		phoneField.setFont(new Font("Calibri", Font.BOLD, 20));
		phoneField.setBorder(new EmptyBorder(0, 15, 0, 0));
		phoneField.setBounds(75, 379, 175, 44);
		contentPane.add(phoneField);

		button = new JButton("Thêm tài khoản");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 636, 466, 49);
		getContentPane().add(button);
		
		JLabel positionLabel = new JLabel("Chức vụ:");
		positionLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		positionLabel.setBounds(75, 545, 148, 19);
		contentPane.add(positionLabel);
		
		positionField = new JTextField();
		positionField.setFont(new Font("Calibri", Font.BOLD, 20));
		positionField.setBorder(new EmptyBorder(0, 15, 0, 0));
		positionField.setBounds(75, 566, 175, 44);
		contentPane.add(positionField);
		
		salaryField = new MoneyTextField();
		salaryField.setFont(new Font("Calibri", Font.BOLD, 20));
		salaryField.setBorder(new EmptyBorder(0, 15, 0, 0));
		salaryField.setBounds(75, 478, 175, 44);
		contentPane.add(salaryField);
		
		JLabel salaryLabel = new JLabel("Lương:");
		salaryLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		salaryLabel.setBounds(75, 456, 108, 19);
		contentPane.add(salaryLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(262, 379, 279, 231);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(8, 0, 0, 0));
		panel.add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(7, 0, 0, 0));
		rightPanel.setBorder(new MatteBorder(0, 2, 0, 0, (Color) new Color(0, 0, 0)));
		panel.add(rightPanel);
		
		staffCheckBox = new JCheckBox("Nhân viên");
		staffCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(staffCheckBox);
		
		advanceCheckBox = new JCheckBox("Ứng lương");
		advanceCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(advanceCheckBox);
		
		bonusCheckBox = new JCheckBox("Thưởng tiền");
		bonusCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(bonusCheckBox);
		
		payCheckBox = new JCheckBox("Trả lương");
		payCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(payCheckBox);
		
		timeCheckBox = new JCheckBox("Chấm công");
		timeCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(timeCheckBox);
		
		editCheckBox = new JCheckBox("Sửa nhân viên");
		editCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(editCheckBox);
		
		addCheckBox = new JCheckBox("Thêm nhân viên");
		addCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rightPanel.add(addCheckBox);
		
		tableCheckBox = new JCheckBox("Bàn ăn");
		tableCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(tableCheckBox);
		
		foodCheckBox = new JCheckBox("Món ăn");
		foodCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(foodCheckBox);
		
		resourceCheckBox = new JCheckBox("Thực phẩm");
		resourceCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(resourceCheckBox);
		
		reportCheckBox = new JCheckBox("Báo cáo");
		reportCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(reportCheckBox);
		
		statisticCheckBox = new JCheckBox("Thống kê");
		statisticCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(statisticCheckBox);
		
		storgeCheckBox = new JCheckBox("Kho");
		storgeCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(storgeCheckBox);
		
		importCheckBox = new JCheckBox("Nhập hàng");
		importCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(importCheckBox);
		
		exportCheckBox = new JCheckBox("Xuất hàng");
		exportCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		leftPanel.add(exportCheckBox);
		
		JLabel label = new JLabel("Phân quyền:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		label.setBounds(304, 360, 220, 19);
		contentPane.add(label);
		
		importCheckBox.setVisible(false);
		exportCheckBox.setVisible(false);
		advanceCheckBox.setVisible(false);
		bonusCheckBox.setVisible(false);
		payCheckBox.setVisible(false);
		timeCheckBox.setVisible(false);
		editCheckBox.setVisible(false);
		addCheckBox.setVisible(false);
	}
}

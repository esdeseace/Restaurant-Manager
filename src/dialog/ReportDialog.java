package dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.Font;
import javax.swing.border.LineBorder;

import connection.Sever;

import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ReportDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	public ReportDialog() {
		
		setIconImage(Sever.icon.getImage());
		contentPane = new JPanel();
		setSize(274, 456);
		setTitle("báo cáo pdf".toUpperCase());
		
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel banner = new JLabel("Chọn báo cáo để mở");
		banner.setHorizontalAlignment(SwingConstants.CENTER);
		banner.setFont(new Font("Calibri", Font.BOLD, 20));
		banner.setBounds(0, 0, 248, 50);
		contentPane.add(banner);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.GRAY));
		panel.setBounds(30, 50, 196, 287);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JCheckBox billCheckBox = new JCheckBox("Hóa đơn");
		billCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		billCheckBox.setBounds(10, 10, 145, 25);
		panel.add(billCheckBox);
		
		JCheckBox detailBillCheckBox = new JCheckBox("Chi tiết hóa đơn");
		detailBillCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		detailBillCheckBox.setBounds(10, 40, 145, 25);
		panel.add(detailBillCheckBox);
		
		JCheckBox importCheckBox = new JCheckBox("Chi tiết nhập");
		importCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		importCheckBox.setBounds(10, 70, 145, 25);
		panel.add(importCheckBox);
		
		JCheckBox exportCheckBox = new JCheckBox("Chi tiết xuất");
		exportCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		exportCheckBox.setBounds(10, 100, 145, 25);
		panel.add(exportCheckBox);
		
		JCheckBox salaryCheckBox = new JCheckBox("Báo cáo trả lương");
		salaryCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		salaryCheckBox.setBounds(10, 130, 158, 25);
		panel.add(salaryCheckBox);
		
		JCheckBox bonusCheckBox = new JCheckBox("Báo cáo thưởng tiền");
		bonusCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bonusCheckBox.setBounds(10, 160, 180, 25);
		panel.add(bonusCheckBox);
		
		JCheckBox advanceCheckBox = new JCheckBox("Báo cáo ứng tiền");
		advanceCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		advanceCheckBox.setBounds(10, 190, 158, 25);
		panel.add(advanceCheckBox);
		
		JCheckBox timeCheckBox = new JCheckBox("Báo cáo chấm công");
		timeCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		timeCheckBox.setBounds(10, 220, 158, 25);
		panel.add(timeCheckBox);
		
		JCheckBox allCheckBox = new JCheckBox("Báo cáo tổng hợp");
		allCheckBox.setSelected(true);
		allCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		allCheckBox.setBounds(10, 250, 158, 25);
		panel.add(allCheckBox);
		
		JButton opemButton = new JButton("Mở");
		opemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					if (detailBillCheckBox.isSelected()) { 
						File file = new File("pdf/Chitiethoadon.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (billCheckBox.isSelected()) { 
						File file = new File("pdf/Hoadon.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (importCheckBox.isSelected()) { 
						File file = new File("pdf/Nhap.pdf");
						Desktop.getDesktop().open(file);
					}

					if (exportCheckBox.isSelected()) { 
						File file = new File("pdf/Xuat.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (salaryCheckBox.isSelected()) {
						File file = new File("pdf/Luong.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (bonusCheckBox.isSelected()) {
						File file = new File("pdf/Tienthuong.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (timeCheckBox.isSelected()) {
						File file = new File("pdf/Chamcong.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (advanceCheckBox.isSelected()) {
						File file = new File("pdf/Tienung.pdf");
						Desktop.getDesktop().open(file);
					}
					
					if (allCheckBox.isSelected()) {
						File file = new File("pdf/Tonghop.pdf");
						Desktop.getDesktop().open(file);
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Tệp không tồn tại! Hãy chọn ngày trước khi xem báo cáo!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
				dispose();
			}
		});
		opemButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		opemButton.setBounds(79, 355, 97, 41);
		contentPane.add(opemButton);
		
	}
}

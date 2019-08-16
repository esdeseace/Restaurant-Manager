package dialog;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import connection.Sever;
import controller.BillController;
import model.BillModel;
import object.Bill;
import object.DinnerTable;
import subComponent.BillPdf;
import subComponent.MoneyTextField;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class PayDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	private JButton printButton;
	private JButton payButton;
	
	private MoneyTextField defTotalField;
	private MoneyTextField totalField;
	private MoneyTextField guestField;
	private MoneyTextField excessdField;

	private JLabel tableLabel;
	private JLabel endLabel;
	private JLabel startLabel;

	private BillController billController;
	private BillModel billModel;
	private boolean isPrintted = false;
	
	public PayDialog(BillController billController, BillModel billModel) {
	
		this.billController = billController;
		this.billModel = billModel;
		setTitle("Thanh toán".toUpperCase());
		setIconImage(Sever.icon.getImage());
		setView();
		setEvent();
	}
	
	private void setEvent() {
		
		DinnerTable table = billController.getSelectedTable();
		Bill bill = billController.getSelectedBill();
		bill.pay(table.getName());
		
		String tableName = table.getName();
		String start = bill.getStart().toString();
		String end = bill.getEnd().toString();
		String total = Sever.numberFormat.format(bill.getTotal());
		
		bill.pay(tableName);
		tableLabel.setText(tableName);
		startLabel.setText(start);
		endLabel.setText(end);
		defTotalField.setText(total);
		totalField.setText(total);
		bill.setRealTotal(bill.getTotal());
		
		payButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String total = totalField.getText();
				
				if (total.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Thực thu không được để trống!", 
							"Lỗi!", JOptionPane.WARNING_MESSAGE);
				} else {
					
					try {
						Number number = Sever.numberFormat.parse(total);
						int totalInt = number.intValue();
						bill.setRealTotal(totalInt);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					int isPay;
					if (!isPrintted)
						isPay = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác!! Bạc có chắc muốn thanh toán mà không in hóa đơn không?!", 
								"Thanh toán", JOptionPane.YES_NO_OPTION);
					else 
						isPay = 0;
					
					if (isPay == 0) {
						billModel.removeFromStorge(bill);
						billModel.finishBill(bill, table);
						table.setIdOfBill(0);
						billController.setBillPane(table);
						dispose();
					}
				}
			}
		});
		
		printButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int isPrint = JOptionPane.showConfirmDialog(null, 
						"Sau khi in hóa đơn, bàn ăn sẽ tự động được thanh toán1! Bạn có chắc không?!", 
						"In hóa đơn", JOptionPane.YES_NO_OPTION);
				
				if (isPrint == 0) {
					
					isPrintted = true;
					payButton.doClick();
					
					BillPdf billPdf = new BillPdf(bill);
					billPdf.createPdf();
				}
			}
		});
		
		DocumentListener dl = new DocumentListener() {
		
			@Override
			public void removeUpdate(DocumentEvent e) {
				setExcessField();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setExcessField();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setExcessField();
			}
			
			private void setExcessField() {
				String guest = guestField.getText();
				String total = totalField.getText();
				
				if (!guest.isEmpty() && !total.isEmpty()) {
				
					int totalInt = 0;
					int guestInt = 0;
					try {
						Number number = Sever.numberFormat.parse(total);
						totalInt = number.intValue();
						number = Sever.numberFormat.parse(guest);
						guestInt = number.intValue();
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
					
					String excess = Sever.numberFormat.format(guestInt - totalInt);
					excessdField.setText(excess);
				}
			}
		};
		
		guestField.getDocument().addDocumentListener(dl);
		totalField.getDocument().addDocumentListener(dl);
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(419, 796);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel mainBanner = new JLabel("THANH TOÁN");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(0, 28, 401, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(mainBanner);
		
		JLabel defTotalLabel = new JLabel("Tổng tiền:");
		defTotalLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		defTotalLabel.setBounds(82, 248, 108, 19);
		contentPane.add(defTotalLabel);
		
		defTotalField = new MoneyTextField();
		defTotalField.setForeground(Color.RED);
		defTotalField.setEditable(false);
		defTotalField.setFont(new Font("Calibri", Font.BOLD, 20));
		defTotalField.setBorder(new EmptyBorder(0, 15, 0, 0));
		defTotalField.setBounds(82, 267, 228, 44);
		contentPane.add(defTotalField);
		
		JLabel totalLabel = new JLabel("Thực thu:");
		totalLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		totalLabel.setBounds(82, 340, 90, 19);
		contentPane.add(totalLabel);
		
		totalField = new MoneyTextField();
		totalField.setForeground(Color.RED);
		totalField.setFont(new Font("Calibri", Font.BOLD, 20));
		totalField.setBorder(new EmptyBorder(0, 15, 0, 0));
		totalField.setBounds(82, 361, 228, 44);
		contentPane.add(totalField);
		
		JLabel guestLabel = new JLabel("Khách đưa:");
		guestLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		guestLabel.setBounds(82, 431, 108, 19);
		contentPane.add(guestLabel);
		
		guestField = new MoneyTextField();
		guestField.setForeground(Color.RED);
		guestField.setFont(new Font("Calibri", Font.BOLD, 20));
		guestField.setBorder(new EmptyBorder(0, 15, 0, 0));
		guestField.setBounds(82, 450, 228, 44);
		contentPane.add(guestField);
		
		JLabel excessLabel = new JLabel("Tiền thừa:");
		excessLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		excessLabel.setBounds(82, 523, 108, 19);
		contentPane.add(excessLabel);
		
		excessdField = new MoneyTextField();
		excessdField.setEditable(false);
		excessdField.setForeground(Color.RED);
		excessdField.setFont(new Font("Calibri", Font.BOLD, 20));
		excessdField.setBorder(new EmptyBorder(0, 15, 0, 0));
		excessdField.setBounds(82, 542, 228, 44);
		contentPane.add(excessdField);

		final ImageIcon printIcon = new ImageIcon(PayDialog.class.getResource("/image/print.png"));
		final ImageIcon payIcon = new ImageIcon(PayDialog.class.getResource("/image/pay.png"));
		
		printButton = new JButton("In hóa đơn", printIcon);
		printButton.setBounds(218, 612, 120, 100);
		printButton.setHorizontalTextPosition(JButton.CENTER);
		printButton.setVerticalTextPosition(JButton.BOTTOM);
		printButton.setFont(new Font("Calibri", Font.BOLD, 17));
		contentPane.add(printButton);
		
		payButton = new JButton("Thanh toán", payIcon);
		payButton.setBounds(63, 612, 120, 100);
		payButton.setHorizontalTextPosition(JButton.CENTER);
		payButton.setVerticalTextPosition(JButton.BOTTOM);
		payButton.setFont(new Font("Calibri", Font.BOLD, 17));
		contentPane.add(payButton);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.RED, 2));
		panel.setBounds(30, 82, 347, 134);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel defEndLabel = new JLabel("Thời gian kết thúc:");
		defEndLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		defEndLabel.setBounds(12, 96, 142, 26);
		panel.add(defEndLabel);
		
		endLabel = new JLabel();
		endLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		endLabel.setBounds(156, 96, 179, 26);
		panel.add(endLabel);
		
		startLabel = new JLabel();
		startLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		startLabel.setBounds(156, 67, 179, 26);
		panel.add(startLabel);
		
		JLabel defStartLabel = new JLabel("Thời gian bắt đầu:");
		defStartLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		defStartLabel.setBounds(12, 67, 142, 26);
		panel.add(defStartLabel);
		
		JLabel defStaffLabel = new JLabel("Nhân viên:");
		defStaffLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		defStaffLabel.setBounds(12, 42, 142, 26);
		panel.add(defStaffLabel);
		
		JLabel defTableLabel = new JLabel("Tên bàn:");
		defTableLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		defTableLabel.setBounds(12, 13, 142, 26);
		panel.add(defTableLabel);
		
		tableLabel = new JLabel();
		tableLabel.setForeground(Color.RED);
		tableLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		tableLabel.setBounds(156, 13, 179, 26);
		panel.add(tableLabel);
		
		JLabel staffLabel = new JLabel(Sever.staff.getName());
		staffLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		staffLabel.setBounds(156, 42, 179, 26);
		panel.add(staffLabel);
	}	
}

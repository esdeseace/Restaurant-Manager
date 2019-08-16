package panel;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import connection.Sever;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

public class BillPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel payPanel;
	private JPanel mainBillPanel;
	private JPanel detailPanel;
	private JTable billTable;

	private DetailExtraPanel dayPanel;
	private DetailExtraPanel startPanel;
	private DetailExtraPanel tablePanel;
	
	private JTextField totalField;
	
	private JButton payButton;
	private JButton startButton;
	private JButton deleteButton;
	private JButton cancelButton;
	private JButton addButton;
	private JButton acceptButton;

	public JButton getAcceptButton() {
		return this.acceptButton;
	}
	
	public JButton getStartButton() {
		return this.startButton;
	}
	
	public JButton getDeleteButton() {
		return this.deleteButton;
	}
	
	public JButton getCancelButton() {
		return this.cancelButton;
	}
	
	public JButton getAddButton() {
		return this.addButton;
	}
	
	public JButton getPayButton() {
		return this.payButton;
	}
	
	public void setTotalText(int Total) {
		totalField.setText(Sever.numberFormat.format(Total));
	}
	
	public void setTableText(String startText, String dayText) {
		startPanel.extraLabel.setText(startText);
		dayPanel.extraLabel.setText(dayText);
	}
	
	public void setTableText(String startText, String dayText, String tableText) {
		startPanel.extraLabel.setText(startText);
		dayPanel.extraLabel.setText(dayText);
		tablePanel.extraLabel.setText(tableText);
	}
	
	public JTable getBillTable() {
		return this.billTable;
	}

	public BillPane() {
		
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);
		
		detailPanel = new JPanel();
		detailPanel.setBorder(new LineBorder(Color.BLACK));
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.PAGE_AXIS));
		add(detailPanel, BorderLayout.NORTH);
		
		DetailExtraPanel staffPanel = new DetailExtraPanel("Nhân viên:");
		staffPanel.extraLabel.setText(Sever.staff.getName());
		tablePanel = new DetailExtraPanel("Tên bàn:");
		tablePanel.extraLabel.setForeground(Color.RED);
		startPanel = new DetailExtraPanel("Giờ vào:");
		dayPanel = new DetailExtraPanel("Ngày:");
		
		mainBillPanel = new JPanel();
		mainBillPanel.setLayout(new BorderLayout());
		mainBillPanel.setBorder(new LineBorder(Color.BLACK));
		add(mainBillPanel, BorderLayout.CENTER);
		
		billTable = new JTable() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {                
				if (column == 3)
					return true;
				return false;
			};

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row,
					int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				if (col != 1) {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				}
				return comp;
			}
		};
		
		JTableHeader header = billTable.getTableHeader();
		TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		header.setFont(new Font("Calibri", Font.BOLD, 20));
		JScrollPane tableView = new JScrollPane(billTable);
		mainBillPanel.add(tableView, BorderLayout.CENTER);
		billTable.setRowHeight(40);
		billTable.setFont(new Font("Calibri", Font.PLAIN, 20));
		billTable.setAutoCreateRowSorter(true);
		
		JPanel menuBillPanel = new JPanel();
		menuBillPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		menuBillPanel.setBorder(new MatteBorder(0, 1, 1, 1, Color.BLACK));
		mainBillPanel.add(menuBillPanel, BorderLayout.SOUTH);
		
		final ImageIcon addSmall = new ImageIcon(BillPane.class.getResource("/image/addSmall.png"));
		final ImageIcon deleteSmall = new ImageIcon(BillPane.class.getResource("/image/deleteSmall.png"));
		
		addButton = new JButton("Thêm Món ăn", addSmall);
		addButton.setFont(new Font("Calibri", Font.BOLD, 20));
		menuBillPanel.add(addButton);
		
		deleteButton = new JButton("Xóa món ăn", deleteSmall);
		deleteButton.setFont(new Font("Calibri", Font.BOLD, 20));
		menuBillPanel.add(deleteButton);
		
		payPanel = new JPanel();
		payPanel.setBorder(new LineBorder(Color.BLACK));
		payPanel.setLayout(new BoxLayout(payPanel, BoxLayout.PAGE_AXIS));
		add(payPanel, BorderLayout.SOUTH);
		
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		payPanel.add(totalPanel);
		
		JLabel defTotalLabel = new JLabel("Tổng tiền:");
		defTotalLabel.setVerticalAlignment(JLabel.CENTER);
		defTotalLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		totalPanel.add(defTotalLabel);
		
		totalField = new JTextField();
		totalField.setEditable(false);
		totalField.setHorizontalAlignment(JTextField.RIGHT);
		totalField.setPreferredSize(new Dimension(150, 40));
		totalField.setFont(new Font("Caliri", Font.BOLD, 25));
		totalField.setForeground(Color.RED);
		totalPanel.add(totalField);
	}
	
	public void createForDialog() {
		
		final ImageIcon acceptIcon = new ImageIcon(BillPane.class.getResource("/image/accept.png"));
		
		acceptButton = new JButton("Chấp nhận", acceptIcon);
		acceptButton.setHorizontalTextPosition(JLabel.CENTER);
		acceptButton.setVerticalTextPosition(JLabel.BOTTOM);
		acceptButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		acceptButton.setHorizontalTextPosition(JButton.CENTER);
		acceptButton.setVerticalTextPosition(JButton.BOTTOM);
		acceptButton.setFont(new Font("Calibri", Font.BOLD, 20));
		payPanel.add(acceptButton);
	}
	 
	public void createSouthPanel() {
		
		JPanel menuCalPanel = new JPanel();
		menuCalPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		payPanel.add(menuCalPanel);
		
		final ImageIcon startIcon = new ImageIcon(BillPane.class.getResource("/image/start.png"));
		final ImageIcon payIcon = new ImageIcon(BillPane.class.getResource("/image/pay.png"));
		final ImageIcon cancelIcon = new ImageIcon(BillPane.class.getResource("/image/cancel.png"));
		
		startButton = new JButton("Bắt đầu", startIcon);
		startButton.setHorizontalTextPosition(JLabel.CENTER);
		startButton.setVerticalTextPosition(JLabel.BOTTOM);
		startButton.setFont(new Font("Calibri", Font.BOLD, 20));
		startButton.setHorizontalTextPosition(JButton.CENTER);
		startButton.setVerticalTextPosition(JButton.BOTTOM);
		menuCalPanel.add(startButton);
		
		payButton = new JButton("Thanh toán", payIcon);
		payButton.setHorizontalTextPosition(JLabel.CENTER);
		payButton.setVerticalTextPosition(JLabel.BOTTOM);
		payButton.setHorizontalTextPosition(JButton.CENTER);
		payButton.setVerticalTextPosition(JButton.BOTTOM);
		payButton.setFont(new Font("Calibri", Font.BOLD, 20));
		menuCalPanel.add(payButton);
		
		cancelButton = new JButton("Hủy bàn", cancelIcon);
		cancelButton.setHorizontalTextPosition(JLabel.CENTER);
		cancelButton.setVerticalTextPosition(JLabel.BOTTOM);
		cancelButton.setFont(new Font("Calibri", Font.BOLD, 20));
		cancelButton.setHorizontalTextPosition(JButton.CENTER);
		cancelButton.setVerticalTextPosition(JButton.BOTTOM);
		menuCalPanel.add(cancelButton);
	}
	
	private class DetailExtraPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private JLabel extraLabel;
		
		public DetailExtraPanel(String defText) {
			
			setLayout(new FlowLayout(FlowLayout.LEADING));
			detailPanel.add(this);
			
			JLabel defLabel = new JLabel(defText);
			defLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
			add(defLabel);
			
			extraLabel = new JLabel();
			extraLabel.setFont(new Font("Calibri", Font.BOLD, 20));
			add(extraLabel);
		}
	}
}
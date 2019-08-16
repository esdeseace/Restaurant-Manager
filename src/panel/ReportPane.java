package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import connection.Sever;
import object.Bill;
import object.Coupon;
import object.DetailBill;
import object.DetailCoupon;
import object.Food;

public class ReportPane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final String[] COUPON_TITLES = {"STT", "Tên thực phẩm", "Số lượng (lớn)", "Số lượng (mhỏ)", "Đơn giá", "Thành tiền", "Ghi chú"};
	private final String[] BILL_TITLES = {"STT", "Tên món ăn", "Số lượng", "Đơn giá", "Thành tiền"};
	
	private JTable detailTable;
	private JPanel detailPane;
	
	private BannerItem startTime;
	private BannerItem endTime;
	private BannerItem day;
	private BannerItem staffName;
	private BannerItem idPanel;
	private BannerItem tableName;
	private BannerItem total;
	private BannerItem realTotal;
	private BannerItem supllierName;
	private BannerItem kind;
	
	private JButton backButton;
	private JPanel leftBillBanner;
	private JPanel rightBillBanner;
	private TitledBorder titleBorder;

	public JButton getBackButton() {
		return this.backButton;
	}
	
	public JTable getdetailTable() {
		return this.detailTable;
	}
	
	public void setDetail(Coupon coupon) {
		
		if (coupon.isImport()) {
			titleBorder.setTitle("CHI TIẾT PHIẾU NHẬP");
			kind.getLabel().setText("Nhập hàng");
		} else { 
			titleBorder.setTitle("CHI TIẾT PHIẾU XUẤT");
			kind.getLabel().setText("Xuất hàng");
		}
		
		String id = String.valueOf(coupon.getId());
		String total_text = Sever.numberFormat.format( coupon.getTotal() );
		String date = Sever.fullTimeFormat.format(coupon.getDay());
		String name = coupon.getName();
		
		idPanel.getLabel().setText( id );
		total.getLabel().setText(total_text);
		day.getLabel().setText( date );
		staffName.getLabel().setText( name );
		supllierName.getLabel().setText( coupon.getSupplier() );
		
		DefaultTableModel tableModel = new DefaultTableModel(null, COUPON_TITLES);
		detailTable.setModel(tableModel);
		
		ArrayList<DetailCoupon> detailSlips = coupon.getFoods();
		for (DetailCoupon detailCoupon : detailSlips) {
			
			Food food = detailCoupon.getFood();
			
			String big = detailCoupon.getBigNumber() + " " + food.getBigUnit();
			String small = detailCoupon.getSmallNumber() + " " + food.getSmallUnit();
			String price = Sever.numberFormat.format(detailCoupon.getPrice());
			String total = Sever.numberFormat.format(detailCoupon.getTotal());
			String note = detailCoupon.getNote();		
			
			Object[] row = {tableModel.getRowCount() + 1, food, big, small, price, total, note};
			tableModel.addRow(row);
		}
	}
	
	public void setDetail(Bill bill) {
		
		String start =  bill.getStart().toString();
		String end = bill.getEnd().toString();
		String date = Sever.dateFormat.format(bill.getDay());
		String name = bill.getName();
		String id = String.valueOf( bill.getId() );
		String nameOfTable = bill.getNameOfTable();
		String total_text = Sever.numberFormat.format(bill.getTotal());
		String realTotl_textl = Sever.numberFormat.format(bill.getRealTotal());
		
		startTime.getLabel().setText( start );
		endTime.getLabel().setText( end );
		day.getLabel().setText( date );
		staffName.getLabel().setText( name );
		idPanel.getLabel().setText( id );
		tableName.getLabel().setText( nameOfTable );
		total.getLabel().setText( total_text );
		realTotal.getLabel().setText( realTotl_textl );
		
		DefaultTableModel tableModel = new DefaultTableModel(null, BILL_TITLES);
		detailTable.setModel(tableModel);
		
		HashMap<Food, DetailBill> foods = bill.getFoods();
		for (Food food : foods.keySet()) {
			
			DetailBill info = foods.get(food);
			
			String amount = info.getAmount() + " " + food.getSmallUnit();
			String price = Sever.numberFormat.format( info.getPrice() );
			String totalString = Sever.numberFormat.format( info.getTotal() );
			
			Object[] row = {tableModel.getRowCount() + 1, food, price, amount, totalString};
			tableModel.addRow(row);
		}
	}
	
	public ReportPane() {
		
		setLayout(new CardLayout());
		
		detailPane = new JPanel();
		detailPane.setLayout(new BorderLayout(10, 10));
		this.add(detailPane, "Detail");
		
		titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"CHI TIẾT HÓA ĐƠN", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		detailPane.setBorder(titleBorder);
		
		detailTable = new JTable() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {                
				return false;
			};
			
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row,
					int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				if (col == 1) {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				}
				
				return comp;
			}
		};
		
		JTableHeader header = detailTable.getTableHeader();
		TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
		
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		header.setFont(new Font("Calibri", Font.BOLD, 20));
		detailTable.setRowHeight(40);
		detailTable.setFont(new Font("Calibri", Font.PLAIN, 20));
		detailPane.add(new JScrollPane(detailTable), BorderLayout.CENTER);
		
		JPanel detailBillBanner = new JPanel();
		detailBillBanner.setBorder(new EmptyBorder(0, 10, 0, 10));
		detailBillBanner.setLayout(new GridLayout(0, 2, 10, 0));
		detailPane.add(detailBillBanner, BorderLayout.NORTH);
		
		leftBillBanner = new JPanel();
		leftBillBanner.setBorder(new LineBorder(Color.BLACK));
		leftBillBanner.setLayout(new BoxLayout(leftBillBanner, BoxLayout.Y_AXIS));
		detailBillBanner.add(leftBillBanner);
		
		rightBillBanner = new JPanel();
		rightBillBanner.setBorder(new LineBorder(Color.BLACK));
		rightBillBanner.setLayout(new BoxLayout(rightBillBanner, BoxLayout.Y_AXIS));
		detailBillBanner.add(rightBillBanner);
		
		JPanel footPanel = new JPanel();
		detailPane.add(footPanel, BorderLayout.SOUTH);
		footPanel.setLayout(new BoxLayout(footPanel, BoxLayout.Y_AXIS));
		
		backButton = new JButton("Quay lại");
		backButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
		backButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		footPanel.add(backButton);
	}
	
	public void setSlipReport() {
		
		idPanel = new BannerItem("Mã phiếu:");
		leftBillBanner.add(idPanel);
		
		total = new BannerItem("Tổng tiền:");
		leftBillBanner.add(total);
		
		kind = new BannerItem("Loại hình:");
		leftBillBanner.add(kind);
		
		day = new BannerItem("Thời gian:");
		leftBillBanner.add(day);
		
		staffName = new BannerItem("Nhân viên:");
		rightBillBanner.add(staffName);
		
		supllierName = new BannerItem("Nhà cung cấp:");
		rightBillBanner.add(supllierName);	
	}
	
	public void setBillReport() {
		
		titleBorder.setTitle("CHI TIẾT HÓA ĐƠN");
		
		idPanel = new BannerItem("Mã hóa đơn:");
		leftBillBanner.add(idPanel);
		
		tableName = new BannerItem("Tên bàn:");
		leftBillBanner.add(tableName);
		
		total = new BannerItem("Tổng tiền thực tế:");
		leftBillBanner.add(total);
		
		realTotal = new BannerItem("Tổng tiền thực thu:");
		leftBillBanner.add(realTotal);
		
		staffName = new BannerItem("Nhân viên:");
		rightBillBanner.add(staffName);
		
		startTime = new BannerItem("Thời gian bắt đầu:");
		rightBillBanner.add(startTime);
		
		endTime = new BannerItem("Thời gian kết thúc:");
		rightBillBanner.add(endTime);
		
		day = new BannerItem("Ngày:");
		rightBillBanner.add(day);
	}
	
	private class BannerItem  extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private JLabel label;
		
		public JLabel getLabel() {
			return this.label;
		}
		
		public BannerItem(String defText) {
			
			setLayout(new FlowLayout(FlowLayout.LEADING, 10, 0));
			
			JLabel defLabel = new JLabel(defText);
			defLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
			add(defLabel);
			
			label = new JLabel();
			label.setFont(new Font("Calibri", Font.PLAIN, 20));
			add(label);
		}
	}
}

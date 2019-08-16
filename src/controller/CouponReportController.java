package controller;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import connection.Sever;
import model.CouponModel;
import object.Coupon;
import panel.ReportPane;
import subComponent.ButtonTable;

public class CouponReportController {
	
	private final String[] titles = {"STT", "Thời gian", "Loại hình", "Tổng tiền", "Nhân viên", "Nhà cung cấps", ""};
	
	private ReportPane reportPane;
	private CouponModel couponModel;
	private CouponTable couponTable;
	
	public CouponReportController(ReportPane slipReportPane, CouponModel couponModel) {
		this.reportPane = slipReportPane;
		this.couponModel = couponModel;
	}
	
	public void setViewAndEvent() {
		
		couponTable = new CouponTable();
		
		JScrollPane tableView = new JScrollPane(couponTable);
		reportPane.add(tableView, "Table");

		TitledBorder titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"BÁO CÁO NHẬP/XUẤT", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		tableView.setBorder(titleBorder);
		
		CardLayout cardLayout = (CardLayout) reportPane.getLayout();
		cardLayout.show(reportPane, "Table");
		this.updateTable();
		
		reportPane.getBackButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) reportPane.getLayout();
				cardLayout.show(reportPane, "Table");
			}
		});
	}
	
	public void updateTable() {
		
		ArrayList<Coupon> coupons = couponModel.getCoupons();
		couponTable.deleteAllRow();
		int count = 0;
		for (Coupon coupon : coupons) {
			
			String stt = String.valueOf(++count);
			String date = Sever.fullTimeFormat.format( coupon.getDay() );
			String total = Sever.numberFormat.format(coupon.getTotal());
			String name = coupon.getName();
			String supplier = coupon.getSupplier();
			
			Object[] row = {stt, date, coupon, total, name, supplier};
			couponTable.addRow(row);
		}
	}
	
	private class DetailEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		private final JTable table;
		
		private DetailEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
			Coupon coupon = (Coupon) table.getModel().getValueAt(selectedRow, 2);
			reportPane.setDetail(coupon);
			
			CardLayout cardLayout = (CardLayout) reportPane.getLayout();
			cardLayout.show(reportPane, "Detail");
		}
	}
	
	private class CouponTable extends JTable {
		
		private static final long serialVersionUID = 1L;
		
		private DefaultTableModel defaultTableModel;
		
		public CouponTable() {
			
			defaultTableModel = new DefaultTableModel(null, titles) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public Class<?> getColumnClass(int column) {
					
					for (int row = 0; row < getRowCount(); row++)
						if (getValueAt(row, column) != null)
							return getValueAt(row, column).getClass();
					return String.class;
				}
			};
			
			this.setModel(defaultTableModel);
			this.setFont(new Font("Calibri", Font.PLAIN, 20));
			this.setRowHeight(40);
			this.setAutoCreateRowSorter(true);
			
			JTableHeader header = this.getTableHeader();
			header.setReorderingAllowed(false);
			TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
			
			JLabel headerLabel = (JLabel) rendererFromHeader;
			headerLabel.setHorizontalAlignment(JLabel.CENTER);
			header.setFont(new Font("Calibri", Font.BOLD, 20));
			
			int[] positions = {0, 1, 2, 3, 6};
			int[] widths = {80, 280, 150, 140, 150};
			setWidth(positions, widths);
			
			List<String> strings = Arrays.asList("Xem chi tiết");
			List<Action> actions = Arrays.asList(new DetailEvent(this));
			
			ButtonTable buttonTable = new ButtonTable(strings, actions);
			TableColumn column = columnModel.getColumn(6);
			column.setCellRenderer(buttonTable.getButtonsRenderer());
			column.setCellEditor(buttonTable.getButtonEditor(this));
		}
		
		private void addRow(Object[] row) {
			defaultTableModel.addRow(row);
		}
		
		private void deleteAllRow() {
			for (int index = defaultTableModel.getRowCount() - 1; index >= 0; index--)
				defaultTableModel.removeRow(index);
		}
		
		private void setWidth(int[] positions, int[] widths) {
			
			TableColumn column;
			for (int index = 0; index < positions.length; index++) {
				column = columnModel.getColumn(positions[index]);
				column.setMinWidth(widths[index]);
				column.setMaxWidth(widths[index]);
				column.setPreferredWidth(widths[index]);
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {          
			if (column == 6)
				return true;
			return false;
		};
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int col) {
			
			Component comp = super.prepareRenderer(renderer, row, col);
			if (col != 6) {
				if (col == 4 || col == 5) {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				}
			}
			return comp;
		}
	}
}

package controller;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import connection.PanelListener;
import connection.Sever;
import dialog.GoodsDialog;
import model.CouponModel;
import model.FoodModel;
import object.Food;
import panel.StorgePane;

public class StorgeController {
	
	private final String[] titles = new String[]{"STT", "Tên thực phẩm", "Số lượng lớn", "Số lượng nhỏ"};
	
	private StorgePane storgePane;
	private JTabbedPane tabbedPane;
	private FoodModel foodModel;
	private CouponModel couponModel;
	private HashMap<String, StorgeTable> storgeTables;
	
	public StorgeController(StorgePane storgePane, FoodModel foodModel, CouponModel couponModel) {
		this.storgePane = storgePane;
		this.foodModel = foodModel;
		this.couponModel = couponModel;
	}
	
	public CouponModel getCouponModel() {
		return this.couponModel;
	}
	
	public FoodModel getFoodModel() {
		return this.foodModel;
	}
	
	public void setViewAndEvent() {

		this.tabbedPane = storgePane.getTabbedPane();
		
		updateTabbedPane();
		setAction();
	}
	
	private void makeDialog(boolean isImport) {
		GoodsDialog goodsDialog = new GoodsDialog(this, isImport);
		goodsDialog.setModal(true);
		goodsDialog.setVisible(true);
	}

	private void setAction() {
		
		storgePane.setOnPanelOpenned(new PanelListener() {
			
			@Override
			public void onPanelOpenned() {
				updateTabbedPane();
			}
		});
		
		storgePane.getImportButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Sever.staff.isImport)
					makeDialog(true);
				else 
					JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền nhập hàng! Liên hệ administrator để cấp quyền.", 
							"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		storgePane.getExportButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Sever.staff.isExport)
					makeDialog(false);
				else 
					JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền xuất hàng! Liên hệ administrator để cấp quyền.", 
							"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	public void updateTabbedPane() {
		
		String selectedString = findSelectedTab();
		tabbedPane.removeAll();
		storgeTables = new HashMap<>();
		
		StorgeTable fullStorgeTable = new StorgeTable();
		tabbedPane.addTab("Tất cả", new JScrollPane(fullStorgeTable));
		
		ArrayList<Food> foods = foodModel.getFoods();
		for (Food food : foods) {
			if (food.getBigAmount() != 0 || food.getSmallAmount() != 0) {
				
				String key = food.getKindOfFood();
				StorgeTable storgeTable = storgeTables.get(key);
				
				if (storgeTable == null) {
					
					storgeTable = new StorgeTable();
					storgeTables.put(key, storgeTable);
					
					JScrollPane tableView = new JScrollPane(storgeTable);
					tabbedPane.addTab(key, tableView);
					
					if (selectedString.equals(key))
						tabbedPane.setSelectedComponent(tableView);
				}
				
				String name = food.getName();
				String bigAmount = food.getBigAmount() + " " + food.getBigUnit();
				String smallAmount = food.getSmallAmount() + " " + food.getSmallUnit();
				
				Object[] row = { storgeTable.getRowCount() + 1, name, bigAmount, smallAmount};
				storgeTable.addRow(row);
				
				Object[] defaultrow = { fullStorgeTable.getRowCount() + 1, name, bigAmount, smallAmount};
				fullStorgeTable.addRow(defaultrow);
			}
		}
	}
	
	private String findSelectedTab() {
		
		if (tabbedPane.getTabCount() > 0) {
			JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
			JViewport viewport = scrollPane.getViewport();
			JTable selectedTable = (JTable) viewport.getView();
			for (String key : storgeTables.keySet()) {
				JTable storgeTable = storgeTables.get(key);
				if (selectedTable == storgeTable)
					return key;
			}
		} 
		return "";
	}
	
	private class StorgeTable extends JTable {

		private static final long serialVersionUID = 1L;
		
		private DefaultTableModel defaultTableModel;

		public StorgeTable() {
			
			defaultTableModel = new DefaultTableModel(null, titles);;
			
			this.setModel(defaultTableModel);
			this.setFont(new Font("Calibri", Font.PLAIN, 20));
			this.setRowHeight(40);
			this.setAutoCreateRowSorter(true);
			
			JTableHeader header = this.getTableHeader();
			TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
			
			JLabel headerLabel = (JLabel) rendererFromHeader;
			headerLabel.setHorizontalAlignment(JLabel.CENTER);
			header.setFont(new Font("Calibri", Font.BOLD, 20));
		
			columnModel = this.getColumnModel();
			int[] positions = {0, 2, 3};
			int[] widths = {100, 250, 250};
			setWidth(positions, widths);
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
		
		public void addRow(Object[] rowData) {
			defaultTableModel.addRow(rowData);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {                
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
	}

}

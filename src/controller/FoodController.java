package controller;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import connection.PanelListener;
import connection.Sever;
import dialog.BillDialog;
import dialog.FoodDialog;
import model.FoodModel;
import object.Food;
import panel.FoodPane;
import subComponent.ButtonTable;

public class FoodController {

	private final String[] titles = new String[]{"STT", "Tên món ăn", "Giá tiền", "Đơn vị", ""};
	private final String[] noButtonTitles = new String[]{"STT", "Tên món ăn", "Giá tiền", "Đơn vị"};
	
	private FoodPane foodPane;
	private FoodModel foodModel;
	
	private Food selectedFood;
	private JTabbedPane tabbedPane;
	private HashMap<String, FoodTable> foodTables;

	public FoodController(FoodPane foodPane, FoodModel foodModel) {
		this.foodModel = foodModel;
		this.foodPane = foodPane;
	}

	public FoodPane getFoodPane() {
		return this.foodPane;
	}
	
	public FoodModel getFoodModel() {
		return this.foodModel;
	}
	
	public Food getSelectedFood() {
		return this.selectedFood;
	}
	
	private class BillDialogEvent extends MouseAdapter {
		
		private JTable table;
		private DefaultTableModel defaultTableModel;
		
		public BillDialogEvent(JTable table, DefaultTableModel defaultTableModel) {
			this.table = table;
			this.defaultTableModel = defaultTableModel;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			
			if (e.getClickCount() == 2) {
				
				ListSelectionModel lsm = table.getSelectionModel();
				if (lsm != null) {
					int slectedRow = lsm.getMinSelectionIndex();
					TableModel tableModel = table.getModel();
					Food food = (Food) tableModel.getValueAt(slectedRow, 1);
					
					String unit = food.getSmallUnit();
					String price = Sever.numberFormat.format(food.getPrice());
					
					Object[] row = {defaultTableModel.getRowCount() + 1, food, unit, new Integer(1), price, price};
					defaultTableModel.addRow(row);
				}
			}
		}
	}
	
	public void setTabbedPane(BillDialog billDialog) {
		
		JTabbedPane tabbPane = billDialog.getTabbedPane();
		DefaultTableModel defaultTableModel = billDialog.getDefaultTableModel();
		
		FoodTable fullFoodTable = new FoodTable(false);
		fullFoodTable.addMouseListener(new BillDialogEvent(fullFoodTable, defaultTableModel));
		tabbPane.addTab("Tất cả món ăn", new JScrollPane(fullFoodTable));
		
		HashMap<String, FoodTable> billTables = new HashMap<>(); 
		ArrayList<Food> foods = foodModel.getFoods();
		
		for (Food food : foods) {
			
			String key = food.getKindOfFood();
			FoodTable foodTable = billTables.get(key);
			
			if (foodTable == null) {
				
				foodTable = new FoodTable(false);
				foodTable.addMouseListener(new BillDialogEvent(foodTable, defaultTableModel));
				billTables.put(key, foodTable);
				
				JScrollPane tableView = new JScrollPane(foodTable);
				tabbPane.addTab(key, tableView);
			}
			
			String price = Sever.numberFormat.format(food.getPrice());
			String unit = food.getSmallUnit();
			
			Object[] rowData = {foodTable.getRowCount() + 1, food, price, unit};
			foodTable.addRow(rowData);
			
			Object[] fullRowData = {fullFoodTable.getRowCount() + 1, food, price, unit};
			fullFoodTable.addRow(fullRowData);
		}
	}
	
	
	public void updateTabbedPane() {
		
		String selectedString = findSelectedTab();
		tabbedPane.removeAll();
		foodTables = new HashMap<>(); 
		
		FoodTable fullFoodTable = new FoodTable();
		tabbedPane.addTab("Tất cả món ăn", new JScrollPane(fullFoodTable));
		
		ArrayList<Food> foods = foodModel.getFoods();
		for (Food food : foods) {
			
			String key = food.getKindOfFood();
			FoodTable foodTable = foodTables.get(key);
			
			if (foodTable == null) {
				
				foodTable = new FoodTable();
				foodTables.put(key, foodTable);
				
				JScrollPane tableView = new JScrollPane(foodTable);
				tabbedPane.addTab(key, tableView);
				
				if (selectedString.equals(key))
					tabbedPane.setSelectedComponent(tableView);
			}
			
			String price = Sever.numberFormat.format(food.getPrice());
			String unit = food.getSmallUnit();
			
			Object[] rowData = {foodTable.getRowCount() + 1, food, price, unit};
			foodTable.addRow(rowData);
			
			Object[] fullRowData = {fullFoodTable.getRowCount() + 1, food, price, unit};
			fullFoodTable.addRow(fullRowData);
		}
		
		tabbedPane.revalidate();
		tabbedPane.repaint();
	}
	
	private String findSelectedTab() {
		
		if (tabbedPane.getTabCount() > 0) {
			JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
			JViewport viewport = scrollPane.getViewport();
			JTable selectedTable = (JTable) viewport.getView();
			for (String key : foodTables.keySet()) {
				JTable foodTable = foodTables.get(key);
				if (selectedTable == foodTable)
					return key;
			}
		}
		return "";
	}
	
	public void setViewAndEvent() {

		this.tabbedPane = foodPane.getTabbedPane();
		
		updateTabbedPane();
		setAction();
	}
	
	public void setAction() {
		
		foodPane.setOnPanelOpenned(new PanelListener() {
			
			@Override
			public void onPanelOpenned() {
				updateTabbedPane();
			}
		});
		
		
		JButton addButton = foodPane.getAddButton();
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createFoodDialog(false);
			}
		});
	}
	
	private void createFoodDialog(boolean isEdit) {
		FoodDialog foodDialog = new FoodDialog(this, isEdit);
		foodDialog.setModal(true);
		foodDialog.setVisible(true);
	}
	
	private class EditEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private EditEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
			selectedFood = (Food) table.getModel().getValueAt(selectedRow, 1);
			createFoodDialog(true);
		}
	}
	
	private class FoodTable extends JTable {
		
		private static final long serialVersionUID = 1L;
		
		private DefaultTableModel defaultTableModel;
		
		public FoodTable(boolean isHaveButton) {
			if (!isHaveButton) {
				defaultTableModel = new DefaultTableModel(null, noButtonTitles);
				
				setFoodTable();
				columnModel = this.getColumnModel();
				int[] positions = {0, 2, 3};
				int[] widths = {100, 200, 150};
				setWidth(positions, widths);
			}
		}
		
		public FoodTable() {
			
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
			
			setFoodTable();
			int[] positions = {0, 2, 3, 4};
			int[] widths = {100, 200, 150, 100};
			setWidth(positions, widths);
			
			List<String> strings = Arrays.asList("Sửa");
			List<Action> actions = Arrays.asList(new EditEvent(this));
			
			ButtonTable buttonTable = new ButtonTable(strings, actions);
			TableColumn column = columnModel.getColumn(4);
			column.setCellRenderer(buttonTable.getButtonsRenderer());
			column.setCellEditor(buttonTable.getButtonEditor(this));
		}
		
		private void setFoodTable() {
			
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
			if (column == 4)
				return true;
			return false;
		};
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int col) {
			Component comp = super.prepareRenderer(renderer, row, col);
			if (col != 4) {
				if (col != 1) {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				}
			}
			return comp;
		}
	}
}

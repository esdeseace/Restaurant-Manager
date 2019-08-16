package controller;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
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
import dialog.ResourceDialog;
import model.FoodModel;
import object.Food;
import panel.ResourcePane;
import subComponent.ButtonTable;

public class ResourceController {

	private final String[] titles = new String[]{"STT", "Tên thực phẩm", "Giá tiền", "Đơn vị (lớn)",
			"Đơn vị (nhỏ)", "Quy đổi (Ví dụ: 1 thùng = 24 lon)" , ""};
	
	private ResourcePane resourcePane;
	private FoodModel foodModel;
	private JTabbedPane tabbedPane;
	private HashMap<String, ResourceTable> resourceTables;
	private Food selectedFood;
	
	public ResourceController(ResourcePane resourcePane, FoodModel foodModel) {
		
		this.foodModel = foodModel;
		this.resourcePane = resourcePane;
	}
	
	public FoodModel getFoodModel() {
		return this.foodModel;
	}
	
	public Food getSelectedFood() {
		return this.selectedFood;
	}
	
	public void setViewAndEvent() {

		this.tabbedPane = resourcePane.getTabbedPane();
		
		updateTabbedPane();
		setEvent();
	}
	
	public void updateTabbedPane() {
		
		String selectedString = findSelectedTab();
		tabbedPane.removeAll();
		resourceTables = new HashMap<>(); 
		ResourceTable fullResourceTable = new ResourceTable();
		tabbedPane.addTab("Tất cả", new JScrollPane(fullResourceTable));
		
		ArrayList<Food> foods = foodModel.getFoods();
		for (Food food : foods) {
			
			if (food.isResource()) {
				
				String key = food.getKindOfFood();
				ResourceTable resourceTable = resourceTables.get(key);
				
				if (resourceTable == null) {
					
					resourceTable = new ResourceTable();
					resourceTables.put(key, resourceTable);
					
					JScrollPane tableView = new JScrollPane(resourceTable);
					tabbedPane.addTab(key, tableView);
					
					if (selectedString.equals(key))
						tabbedPane.setSelectedComponent(tableView);
				}
				
				String bigUnit = food.getBigUnit();
				String smallUnit = food.getSmallUnit();
				int convert = food.getConvert();
				String price = Sever.numberFormat.format(food.getPrice());
				
				Object[] row = { resourceTable.getRowCount() + 1, food, price, bigUnit, smallUnit, convert};
				resourceTable.addRow(row);
				
				Object[] defaultrow = { fullResourceTable.getRowCount() + 1, food, price, bigUnit, smallUnit, convert};
				fullResourceTable.addRow(defaultrow);
			}
		}
	}
	
	private String findSelectedTab() {
		if (tabbedPane.getTabCount() > 0) {
			JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
			JViewport viewport = scrollPane.getViewport();
			JTable selectedTable = (JTable) viewport.getView();
			for (String key : resourceTables.keySet()) {
				JTable resourceTable = resourceTables.get(key);
				if (selectedTable == resourceTable)
					return key;
			}
		}
		return "";
	}
	
	private void setEvent() {
		
		resourcePane.setOnPanelOpenned(new PanelListener() {
			
			@Override
			public void onPanelOpenned() {
				updateTabbedPane();
			}
		});
		
		resourcePane.getAddButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createResourceDialog(false);
			}
		});
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
			createResourceDialog(true);
		}
	}
	
	private void createResourceDialog(boolean isEdit) {
		ResourceDialog resourceDialog = new ResourceDialog(this, isEdit);
		resourceDialog.setModal(true);
		resourceDialog.setVisible(true);
	}
	
	private class ResourceTable extends JTable {
		
		private static final long serialVersionUID = 1L;
		
		private DefaultTableModel defaultTableModel;

		public ResourceTable() {

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
			
			int[] positions = {0, 2, 3, 4, 5, 6};
			int[] widths = {100, 200, 200, 200, 300, 100};
			setWidth(positions, widths);
			
			List<String> strings = Arrays.asList("Sửa");
			List<Action> actions = Arrays.asList(new EditEvent(this));
			
			ButtonTable buttonTable = new ButtonTable(strings, actions);
			TableColumn column = columnModel.getColumn(6);
			column.setCellRenderer(buttonTable.getButtonsRenderer());
			column.setCellEditor(buttonTable.getButtonEditor(this));
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
			if (column == 6)
				return true;
			return false;
		};
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int col) {
			
			Component comp = super.prepareRenderer(renderer, row, col);
			if (col != 6) {
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


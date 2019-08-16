package controller;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import connection.Sever;
import dialog.BillDialog;
import dialog.PayDialog;
import model.BillModel;
import object.Bill;
import object.DetailBill;
import object.DinnerTable;
import object.Food;
import panel.BillPane;

public class BillController {
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
	private final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
	private final String[] titles = {"STT", "Tên món ăn", "Đơn vị", "Số lượng", "Đơn giá", "Thành tiền"};
	
	private BillPane billPane;
	private BillModel billModel;
	
	private DefaultTableModel defaulttableModel;
	private DefaultTableModel tableModel;
	
	private JComboBox<Integer> comboBox;
	private JTable billTable;
	private Bill selectedBill;
	private DinnerTable selectedTable;
	
	private FoodController foodController;
	
	public BillController(BillPane billPane, BillModel billModel,
			FoodController foodController) {
	
		this.billModel = billModel;
		this.billPane = billPane;
		this.foodController = foodController;
	}
	
	public BillModel getBillModel() {
		return this.billModel;
	}
	
	public JComboBox<Integer> getComboBox() {
		return this.comboBox;
	}
	
	public DefaultTableModel getDefTableModel() {
		return this.tableModel;
	}
	
	public Bill getSelectedBill() {
		return this.selectedBill;
	}
	
	public DinnerTable getSelectedTable() {
		return this.selectedTable;
	}
	
	private void createPayDialog() {
		PayDialog payDialog = new PayDialog(this, billModel);
		payDialog.setModal(true);
		payDialog.setVisible(true);
	}
	
	private void createBillDialog() {
		BillDialog billDialog = new BillDialog(this, foodController);
		billDialog.setModal(true);
		billDialog.setVisible(true);
	}
	
	public void setViewAndEvent() {

		this.billTable = billPane.getBillTable();
		this.defaulttableModel = new DefaultTableModel(null, titles);
		
		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Calibri", Font.PLAIN, 20));
		for (int index = 0; index < 100; index++)
			comboBox.addItem(new Integer(index));
		
		billPane.getStartButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				if (selectedTable != null) {
					billModel.addBill(selectedTable);
					setBillPane(selectedTable);
				} else {
					JOptionPane.showMessageDialog(null, "Chưa chọn bàn!!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		billPane.getCancelButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (checking()) {
					
					if (selectedBill.getFoods().size() == 0) {
					
						int isDelete = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc chắn muốn hủy bàn không?!", 
								"Hủy bàn", JOptionPane.YES_NO_OPTION);
						
						if (isDelete == 0) {
							billModel.deleteBill(selectedBill, selectedTable);
							billTable.setModel(defaulttableModel);
							billPane.setTableText("", "");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Không thẻ hủy bàn khi bàn ăn vẫn còn món ăn!!", 
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		billPane.getAddButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (checking()) {
					createBillDialog();
				}
			}
		});
	
		billPane.getDeleteButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				if (checking()) {	
					
					int[] selectedRows = billTable.getSelectedRows();
					
					if (selectedRows.length > 0) {
					
						int isDelete = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc muốn xóa không?!", 
								"Xóa món ăn", JOptionPane.YES_NO_OPTION);
						
						if (isDelete == 0) {
							
							for (int index = selectedRows.length - 1; index >= 0; index--) {

								Food food = (Food) tableModel.getValueAt(selectedRows[index], 1);
								billModel.deleteFood(selectedBill, food);
								
								tableModel.removeRow(selectedRows[index]);
								billPane.setTotalText(selectedBill.getTotal());
								
								setBillPane(selectedTable);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Hãy chọn món ăn để xóa!!", 
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		billPane.getPayButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (checking()) {
					createPayDialog();
				}
			}
		});
	}
	
	private boolean checking() {
		
		if (selectedTable == null) {
			JOptionPane.showMessageDialog(null, "Chưa chọn bàn!!", 
					"Lỗi", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		if (selectedBill == null) {
			JOptionPane.showMessageDialog(null, "Bàn chưa được sử dụng!! Hãy nhấn bắt đầu trước!!", 
					"Lỗi", JOptionPane.WARNING_MESSAGE);
			return false;
		}	
		
		return true;
	}
	
	public void setBillPane(DinnerTable table) {
		
		int idOfBill = table.getIdOfBill();
		selectedTable = table;
		
		String startText = "";
		String dayText = "";
		String tableText = table.getName();

		if (idOfBill != 0) {
			
			selectedBill = billModel.getBillById(idOfBill);
			tableModel = new DefaultTableModel(null, titles);
			HashMap<Food, DetailBill> foods = selectedBill.getFoods();
			tableModel.addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent e) {
					if (e.getType() == TableModelEvent.UPDATE) {
						if (e.getFirstRow() - e.getLastRow() == 0) {
							
							int row = e.getFirstRow();
							Food food = (Food) tableModel.getValueAt(row, 1);
							int amount = (int) tableModel.getValueAt(row, 3);
							Object priceObject = tableModel.getValueAt(row, 4);
							
							int price = 0;
							try {
								Number number = Sever.numberFormat.parse((String) priceObject);
								price = number.intValue();
							} catch (ParseException e1) {
							}
							
							int money = amount * price;
						
							if (!tableModel.getValueAt(row, 5).equals(numberFormat.format(money)))
								tableModel.setValueAt(numberFormat.format(money), row, 5);
							
							if (selectedBill.getAmountOfFood(food) == amount) 
								return;
							
							billModel.setFood(food, selectedBill, amount);
							billPane.setTotalText(selectedBill.getTotal());
						}
					}
				}
			});
			
			for (Food food : foods.keySet()) {
				
				String unit = food.getSmallUnit();
				DetailBill info = foods.get(food);
				int amount = info.getAmount();
				int price = info.getPrice();
				int money = info.getTotal();
				
				Object[] row = {tableModel.getRowCount() + 1, food, unit, amount, numberFormat.format(price), numberFormat.format(money)};
				tableModel.addRow(row);
			}
			
			billTable.setModel(tableModel);
			TableColumn column = billTable.getColumnModel().getColumn(3);
			column.setCellEditor(new DefaultCellEditor(comboBox));
			
			startText = selectedBill.getStart().toString();
			dayText = dateFormat.format(selectedBill.getDay());
			billPane.setTotalText(selectedBill.getTotal());
		} else {
			selectedBill = null;
			billPane.setTotalText(0);
			billTable.setModel(defaulttableModel);
		}
		
		setWidth(billTable);
		billPane.setTableText(startText, dayText, tableText);
	}
	
	public void setWidth(JTable table) {
		
		int[] positions = {0, 2, 3, 4, 5};
		int[] widths = {50, 100, 100, 100, 150};
		
		TableColumnModel columnModel = table.getColumnModel();
		TableColumn column;
		for (int index = 0; index < positions.length; index++) {
			column = columnModel.getColumn(positions[index]);
			column.setMinWidth(widths[index]);
			column.setMaxWidth(widths[index]);
			column.setPreferredWidth(widths[index]);
		}
	}
}

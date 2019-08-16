package dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import connection.Sever;
import controller.BillController;
import controller.FoodController;
import model.BillModel;
import object.Bill;
import object.Food;
import object.DinnerTable;
import panel.BillPane;
import panel.FoodPane;

public class BillDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
	private final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
	private final String[] titles = {"STT", "Tên món ăn", "Đơn vị", "Số lượng", "Đơn giá", "Thành tiền"};
	
	private FoodController foodController;
	private BillController billController;
	private BillPane billPane;
	private BillModel billModel;
	
	private DinnerTable selectedTable;
	private Bill selectedBill;
	
	private JPanel contentPane;
	private JTable billTable;
	private JTabbedPane tabbedPane;
	private DefaultTableModel defaultTableModel;

	public BillDialog(BillController billController, FoodController foodController) {
		
		this.billController = billController;
		this.billModel = billController.getBillModel();
		this.foodController = foodController;
		
		this.selectedTable = billController.getSelectedTable();
		this.selectedBill = billController.getSelectedBill();
		
		setView();
		setEvent();
	}
	
	public DefaultTableModel getDefaultTableModel() {
		return this.defaultTableModel;
	}
	
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}
	
	private void setEvent() {

		billPane.getAcceptButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int isDelete = JOptionPane.showConfirmDialog(null, 
						"Việc này không thể hoàn tác. Bạn có chắc muốn thêm không?!", 
						"Thêm món ăn", JOptionPane.YES_NO_OPTION);
				
				if (isDelete == 0) {
					Vector<?> data = defaultTableModel.getDataVector();
					for (int index = 0; index < defaultTableModel.getRowCount(); index++) {
						
						Vector<?> row = (Vector<?>) data.elementAt(index);
						Food food = (Food) row.get(1);
						int amount = (int) row.get(3);
						
						if (selectedBill.isHaveFood(food)) {
							billModel.setFood(food, selectedBill, selectedBill.getAmountOfFood(food) + amount);
						} else
							billModel.addFoodToBill(food, selectedBill, amount);
					}
					
					billController.setBillPane(selectedTable);
				}
				dispose();
			}
		});
		
		billPane.getAddButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
				JViewport viewport = scrollPane.getViewport();
				JTable table = (JTable) viewport.getView();
				int[] selectedRows = table.getSelectedRows();
				
				if (selectedRows.length > 0) {
					
					TableModel tableModel = table.getModel();
					for (int index = selectedRows.length - 1; index >= 0; index--) {
						
						Food food = (Food) tableModel.getValueAt(selectedRows[index], 1);
						
						String unit = food.getSmallUnit();
						String price = Sever.numberFormat.format(food.getPrice());
						
						Object[] row = {defaultTableModel.getRowCount() + 1, food, unit, new Integer(1), price, price};
						defaultTableModel.addRow(row);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Hãy chọn món ăn từ thực đơn để thêm vào!!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		billPane.getDeleteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				int[] selectedRows = billTable.getSelectedRows();
				
				if (selectedRows.length > 0)
					for (int index = selectedRows.length - 1; index >= 0; index--)
						defaultTableModel.removeRow(selectedRows[index]);
				else
					JOptionPane.showMessageDialog(null, "Hãy chọn món ăn để xóa!!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		defaultTableModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getFirstRow() - e.getLastRow() == 0) {
					
					int row = e.getFirstRow();
					Food food = (Food) defaultTableModel.getValueAt(row, 1);
					int amount = (int) defaultTableModel.getValueAt(row, 3);
					int money = amount * food.getPrice();
				
					if (!defaultTableModel.getValueAt(row, 5).equals(numberFormat.format(money)))
						defaultTableModel.setValueAt(numberFormat.format(money), row, 5);
					
					int sum = 0;
					for (int index = 0; index < defaultTableModel.getRowCount(); index++) {
						
						Object object = defaultTableModel.getValueAt(index, 5);
						if (object != null) {
							Number number = 0;
							try {
								number = numberFormat.parse((String) object);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							sum += number.intValue();
						}
					}
					billPane.setTotalText(sum);
				}
			}
		});
	}
	
	private void setView() {
		
		setIconImage(Sever.icon.getImage());
		contentPane = new JPanel();
		setSize(1500, 900);
		setTitle("THÊM MÓN ĂN");
		setLocationRelativeTo(this);
		
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new GridLayout());
		setContentPane(contentPane);
		
		billPane = new BillPane();
		billPane.createForDialog();
		
		billTable = billPane.getBillTable();
		defaultTableModel = new DefaultTableModel(null, titles);
		
		billTable.setModel(defaultTableModel);
		billController.setWidth(billTable);
		
		TableColumn column = billTable.getColumnModel().getColumn(3);
		column.setCellEditor(new DefaultCellEditor(billController.getComboBox()));
		
		billPane.setTableText(selectedBill.getStart().toString(), 
				dateFormat.format(selectedBill.getDay()), selectedTable.getName());
		contentPane.add(billPane);
		
		FoodPane foodPane = new FoodPane();
		tabbedPane = foodPane.getTabbedPane();
		foodController.setTabbedPane(this);
		contentPane.add(foodPane);
	}
}

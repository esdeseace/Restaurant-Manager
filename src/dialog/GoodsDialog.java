package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import connection.Sever;
import controller.StorgeController;
import model.CouponModel;
import model.FoodModel;
import object.Coupon;
import object.Food;
import subComponent.MoneyTextField;

public class GoodsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable foodTable;
	private JButton button;
	
	private JTextField supplierField;
	private JTextField sumField;
	private JTextField staffField;
	
	private DefaultTableModel defaultTableModel;
	
	private StorgeController storgeController;
	private FoodModel foodModel;
	private CouponModel couponModel;
	
	private boolean isImport;
	private int maxRow = 0;

	public GoodsDialog(StorgeController storgeController, boolean isImport) {
		
		this.storgeController = storgeController;
		this.foodModel = storgeController.getFoodModel();
		this.isImport = isImport;
		this.couponModel = storgeController.getCouponModel();
		setIconImage(Sever.icon.getImage());
		setView();
		setTable();
		setEvent();
		
		if (isImport) {
			setTitle("Nhập thực phẩm".toUpperCase());
		} else {
			setTitle("Xuất thực phẩm".toUpperCase());
			button.setText("Xuất hàng");
		}
	}
	
	private void setEvent() {
	
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!isImport) {
					
					for (int index = 0; index < maxRow; index++) {
						
						Object foodObject = defaultTableModel.getValueAt(index, 1);
						Object bigAmountObject = defaultTableModel.getValueAt(index, 2);
						Object smallAmountObject = defaultTableModel.getValueAt(index, 3);
						
						if (foodObject != null && (bigAmountObject != null || smallAmountObject != null)) {
							
							int bigAmount = 0;
							if (bigAmountObject != null)
								bigAmount = (int) bigAmountObject;
							
							int smallAmount = 0;
							if (smallAmountObject != null)
								smallAmount = (int) smallAmountObject;
							
							Food food = (Food) foodObject;

							int totalAmount = bigAmount * food.getConvert() + smallAmount;
							int amountInStorge = food.getAmount();
							
							if (bigAmount > food.getBigAmount() || amountInStorge  < totalAmount) {
								JOptionPane.showMessageDialog(null, "Nguyên liệu " + food.getName() + " không đủ số lượng trong kho để xuất!!", 
										"Lỗi", JOptionPane.WARNING_MESSAGE);
								return;
							}
						}
					}
				}
				
				int isOk = JOptionPane.showConfirmDialog(null, 
						"Việc này không thể hoàn tác. Bạn có chắc chắn không?! Những nguyên liệu thiếu tên sẽ bị bỏ qua.", 
						"Nhập/Xuất hàng", JOptionPane.YES_NO_OPTION);
				
				if (isOk == 0) {
				
					String supplier = supplierField.getText();
					String staff = staffField.getText();
					
					Coupon coupon = couponModel.addCoupon(supplier, staff, isImport);
					
					for (int index = 0; index < maxRow; index++) {
						
						Object foodObject = defaultTableModel.getValueAt(index, 1);
						Object bigAmountObject = defaultTableModel.getValueAt(index, 2);
						Object smallAmountObject = defaultTableModel.getValueAt(index, 3);
						Object priceObject = defaultTableModel.getValueAt(index, 4);
						Object totalObject = defaultTableModel.getValueAt(index, 5);
						Object noteObject = defaultTableModel.getValueAt(index, 6);
						
						if (foodObject != null && (bigAmountObject != null || smallAmountObject != null)) {
	
							String note = "";
							if (noteObject != null)
								note = (String) noteObject;
				
							int price = 0;
							if (priceObject != null) {
								try {
									Number number = Sever.numberFormat.parse( (String) priceObject);
									price = number.intValue();
								} catch (ParseException e1) {
								}
							}
							
							int total = 0;
							if (totalObject != null) {
								try {
									Number number = Sever.numberFormat.parse( (String) totalObject);
									total = number.intValue();
								} catch (ParseException e1) {
								}
							}
							
							int bigAmount = 0;
							if (bigAmountObject != null)
								bigAmount = (int) bigAmountObject;
							
							int smallAmount = 0;
							if (smallAmountObject != null)
								smallAmount = (int) smallAmountObject;
							
							Food food = (Food) foodObject;
							couponModel.addFoodToCoupon(coupon, food, bigAmount, smallAmount, price, note, total);
							
							if (isImport) {
								bigAmount = food.getBigAmount() + bigAmount;
								smallAmount = smallAmount + food.getSmallAmount();	
							} else {
								bigAmount = food.getBigAmount() - bigAmount;
								int tmpBigAmount = bigAmount;
								int totalAmount = bigAmount * food.getConvert() + food.getSmallAmount() - smallAmount;
								bigAmount = totalAmount / food.getConvert();
								int tmp = (bigAmount > tmpBigAmount) ? bigAmount - tmpBigAmount : 0;
								bigAmount = bigAmount - tmp;
								smallAmount = tmp * food.getConvert() + totalAmount % food.getConvert();
							}
							foodModel.setAmount(food, bigAmount, smallAmount);
						}
					}
					storgeController.updateTabbedPane();
				}
				dispose();
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(1200, 734);
		setLocationRelativeTo(this);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 20));
		setContentPane(contentPane);
		
		JPanel menuPane = new JPanel();
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.Y_AXIS));
		contentPane.add(menuPane, BorderLayout.NORTH);
		
		JLabel banner = new JLabel();
		if (isImport)
			banner.setText("PHIẾU NHẬP");
		else
			banner.setText("PHIẾU XUẤT");
		banner.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		banner.setHorizontalAlignment(SwingConstants.CENTER);
		banner.setFont(new Font("Calibri", Font.BOLD, 30));
		menuPane.add(banner);

		JPanel infoPanel = new JPanel();
		infoPanel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		menuPane.add(infoPanel);
		
		JPanel staffPanel = new JPanel();
		infoPanel.add(staffPanel);

		JLabel defStaffLabel = new JLabel("Nhân viên:");
		defStaffLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		staffPanel.add(defStaffLabel);
		
		staffField = new JTextField();
		staffField.setPreferredSize(new Dimension(200, 30));
		staffField.setFont(new Font("Tahoma", Font.BOLD, 15));
		staffPanel.add(staffField);
		
		JPanel supplierPanel = new JPanel();
		infoPanel.add(supplierPanel);

		JLabel defSupplierLabel = new JLabel();
		if (isImport)
			defSupplierLabel.setText("Nhà cung cấp:");
		else
			defSupplierLabel.setText("Nơi xuất đến:");
		defSupplierLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		supplierPanel.add(defSupplierLabel);
		
		supplierField = new JTextField();
		supplierField.setPreferredSize(new Dimension(200, 30));
		supplierField.setFont(new Font("Tahoma", Font.BOLD, 15));
		supplierPanel.add(supplierField);
		
		JLabel label = new JLabel("Lưu ý: cột Tên thực phẩm không được bỏ trống.");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		infoPanel.add(label);
		
		JPanel buttonView = new JPanel();
		buttonView.setLayout(new BoxLayout(buttonView, BoxLayout.Y_AXIS));
		
		JPanel sumPanel = new JPanel();
		sumPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		sumPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonView.add(sumPanel);
		
		JLabel defSumLabel = new JLabel("Tổng tiền:");
		defSumLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		sumPanel.add(defSumLabel);
		
		sumField = new JTextField();
		sumField.setFont(new Font("Tahoma", Font.BOLD, 20));
		sumField.setEditable(false);
		sumField.setForeground(Color.RED);
		sumField.setPreferredSize(new Dimension(150, 30));
		sumPanel.add(sumField);
		
		button = new JButton("Nhập hàng");
		button.setFont(new Font("Calibri", Font.BOLD, 20));
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		buttonView.add(button);
		contentPane.add(buttonView, BorderLayout.SOUTH);
	}
	
	private void setTable() {

		foodTable = new JTable() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {                
				if (column == 0)
					return false;
				return true;
			};

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row,
					int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				return comp;
			}
		};
		
		JTableHeader header = foodTable.getTableHeader();
		TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
		
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		header.setFont(new Font("Calibri", Font.BOLD, 15));
		foodTable.setRowSelectionAllowed(false);
		
		JScrollPane tableView = new JScrollPane(foodTable);
		contentPane.add(tableView, BorderLayout.CENTER);
		foodTable.setFont(new Font("Calibri", Font.PLAIN, 20));
		foodTable.setRowHeight(30);
	
		String[] titles = {"STT", "Tên thực phẩm", "Số lượng (lớn)", "Số lượng (nhỏ)", "Đơn giá (theo đơn vị (nhỏ))", "Thành tiền", "Ghi chú"};
		defaultTableModel = new DefaultTableModel(null, titles);
		defaultTableModel.setRowCount(1);
		foodTable.setModel(defaultTableModel);
		
		TableColumn column = foodTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(50);
		column.setMaxWidth(50);
		column.setMinWidth(50);
		
		Vector<Food> resources = foodModel.getResources();
		JComboBox<Food> comboBoxFood = new JComboBox<>(resources);
		comboBoxFood.setFont(new Font("Calibri", Font.PLAIN, 20));
		column = foodTable.getColumnModel().getColumn(1);
		column.setCellEditor(new DefaultCellEditor(comboBoxFood));
		
		JComboBox<Integer> comboBoxNumber = new JComboBox<>();
		comboBoxNumber.setFont(new Font("Calibri", Font.PLAIN, 20));
		for (int index = 0; index < 100; index++)
			comboBoxNumber.addItem(new Integer(index));
		column = foodTable.getColumnModel().getColumn(2);
		column.setCellEditor(new DefaultCellEditor(comboBoxNumber));
		column.setPreferredWidth(130);
		column.setMaxWidth(130);
		column.setMinWidth(130);
		
		column = foodTable.getColumnModel().getColumn(3);
		column.setCellEditor(new DefaultCellEditor(comboBoxNumber));
		column.setPreferredWidth(130);
		column.setMaxWidth(130);
		column.setMinWidth(130);
		
		MoneyTextField priceField = new MoneyTextField();
		priceField.setFont(new Font("Calibri", Font.PLAIN, 20));
		column = foodTable.getColumnModel().getColumn(4);
		column.setCellEditor(new DefaultCellEditor(priceField));
		column.setPreferredWidth(150);
		column.setMaxWidth(150);
		column.setMinWidth(150);
		
		MoneyTextField totalField = new MoneyTextField();
		totalField.setFont(new Font("Calibri", Font.PLAIN, 20));
		column = foodTable.getColumnModel().getColumn(5);
		column.setCellEditor(new DefaultCellEditor(totalField));
		column.setPreferredWidth(150);
		column.setMaxWidth(150);
		column.setMinWidth(150);
		
		JTextField noteField = new JTextField();
		noteField.setFont(new Font("Calibri", Font.PLAIN, 20));
		column = foodTable.getColumnModel().getColumn(6);
		column.setCellEditor(new DefaultCellEditor(noteField));
		
		defaultTableModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					if (e.getFirstRow() - e.getLastRow() == 0) {
						
						int row = e.getFirstRow();
						
						if (row == maxRow) {
							maxRow = row + 1;
							defaultTableModel.setRowCount(maxRow + 1);
							if (defaultTableModel.getValueAt(maxRow, 0) == null)
								defaultTableModel.setValueAt(row + 1, row, 0);
						}
						
						Object foodObject = defaultTableModel.getValueAt(row, 1);
						Object bigAmountObject = defaultTableModel.getValueAt(row, 2);
						Object smallAmountObject = defaultTableModel.getValueAt(row, 3);
						Object priceObject = defaultTableModel.getValueAt(row, 4);
						
						if (foodObject != null && priceObject != null && 
								(bigAmountObject != null || smallAmountObject != null)) {
							
							try {
								
								Food food = (Food) foodObject;
								
								Number number = Sever.numberFormat.parse( (String) priceObject);
								int price = number.intValue();
								
								int bigAmount = 0;
								if (bigAmountObject != null)
									bigAmount = (int) bigAmountObject;
								
								int smallAmount = 0;
								if (smallAmountObject != null)
									smallAmount = (int) smallAmountObject;
								
								int amount = food.getConvert() * bigAmount + smallAmount;
								String total = Sever.numberFormat.format( amount * price );
								
								if (defaultTableModel.getValueAt(row, 5) == null)
									defaultTableModel.setValueAt(total, row, 5);
								else if (!defaultTableModel.getValueAt(row, 5).equals(total))
									defaultTableModel.setValueAt(total, row, 5);
								
							} catch (ParseException e1) {
								//do nothing
							}
						}
						
						int sum = 0;
						
							for (int index = 0; index < maxRow; index++) {
								
								Object totalObject = defaultTableModel.getValueAt(index, 5);
								
								if (totalObject != null) {
									try {	
										Number number = Sever.numberFormat.parse( (String) totalObject);
										int total = number.intValue();
										sum += total;	
									} catch (ParseException e1) {
										//do nothing
									}
								}
							}
						
						sumField.setText( Sever.numberFormat.format(sum) );
					}
				}
			}
		});
	}
}

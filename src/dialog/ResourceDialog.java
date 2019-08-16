package dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import connection.Sever;
import controller.ResourceController;
import model.FoodModel;
import object.Food;
import subComponent.MoneyTextField;

import javax.swing.JComboBox;
import javax.swing.JButton;

public class ResourceDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextField nameField;
	private JTextField bigUnitField;
	private JTextField smallUnitField;
	private JTextField nameKindField;
	private MoneyTextField priceField;
	private JComboBox<Integer> comboBox;
	private JLabel mainBanner;
	
	private JButton button;
	private Food food;
	private boolean isEdit;
	
	private ResourceController resourceController;
	private FoodModel foodModel;

	public ResourceDialog(ResourceController resourceController, boolean isEdit) {
		setIconImage(Sever.icon.getImage());
		this.resourceController = resourceController;
		this.food = resourceController.getSelectedFood();
		this.foodModel = resourceController.getFoodModel();
		this.isEdit = isEdit;
		
		setView();
		setEvent();
		
		if (!isEdit)
			setTitle("THÊM THỰC PHẨM");
		else {
			
			setTitle("SỬA THỰC PHẨM");
			mainBanner.setText("SỬA THỰC PHẨM");
			button.setText("Sửa thực phẩm");
			
			int convert = food.getConvert();
			String name = food.getName();
			String bigUnit = food.getBigUnit();
			String price = Sever.numberFormat.format(food.getPrice());
			String smallUnit = food.getSmallUnit();
			String kinfOfFood = food.getKindOfFood();
			
			nameField.setText(name);
			bigUnitField.setText(bigUnit);
			smallUnitField.setText(smallUnit);
			priceField.setText(price);
			nameKindField.setText(kinfOfFood);
			comboBox.setSelectedItem(new Integer(convert));
		}
	}

	private void setEvent() {
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String name = nameField.getText();
				String bigUnit = bigUnitField.getText();
				String smallUnit = smallUnitField.getText();
				String priceString = priceField.getText();
				int convert = (int) comboBox.getSelectedItem();
				String kindOfFood = nameKindField.getText();
				
				if (name.isEmpty())
					JOptionPane.showMessageDialog(null, "Tên thực phẩm không được trống!", 
						"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (smallUnit.isEmpty())
					JOptionPane.showMessageDialog(null, "Đơn vị (nhỏ) không thể bỏ trống!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (priceString.isEmpty())
					JOptionPane.showMessageDialog(null, "Giá tiền không được trống!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else {
					
					try {
						Number number = Sever.numberFormat.parse(priceString);
						int price = number.intValue();
						
						int isOk = JOptionPane.showConfirmDialog(null, 
							"Việc này không thể hoàn tác. Bạn có chắc chắn không?!", 
							"Thêm/Sửa thực phẩm", JOptionPane.YES_NO_OPTION);
						
						if (isOk == 0) {
							if (kindOfFood.isEmpty())
								kindOfFood = "Món ăn khác";
							
							if (isEdit)
								foodModel.setFood(food, name, bigUnit, smallUnit, price, convert, kindOfFood);	
							else
								foodModel.addFood(name, bigUnit, smallUnit, price, convert, kindOfFood);
							resourceController.updateTabbedPane();
						}
						dispose();
					} catch (ParseException pe) {
						pe.printStackTrace();
						JOptionPane.showMessageDialog(null, "Giá tiền không hợp lệ! Vui lòng xem lại!!",
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(619, 600);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mainBanner = new JLabel("THÊM THỰC PHẨM");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(mainBanner);
		
		JLabel nameLabel = new JLabel("Tên thực phẩm:");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameLabel.setBounds(75, 85, 177, 19);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameField.setBounds(75, 104, 449, 44);
		contentPane.add(nameField);
		
		JLabel bigUnitLabel = new JLabel("Đơn vị (lớn):");
		bigUnitLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		bigUnitLabel.setBounds(75, 183, 177, 19);
		contentPane.add(bigUnitLabel);
		
		bigUnitField = new JTextField("Có thể để trống");
		bigUnitField.setFont(new Font("Calibri", Font.BOLD, 20));
		bigUnitField.setBorder(new EmptyBorder(0, 15, 0, 0));
		bigUnitField.setBounds(75, 202, 154, 44);
		contentPane.add(bigUnitField);
		
		JLabel smallUnitLabel = new JLabel("Đơn vị (nhỏ):");
		smallUnitLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		smallUnitLabel.setBounds(350, 183, 177, 19);
		contentPane.add(smallUnitLabel);
		
		smallUnitField = new JTextField("Chai");
		smallUnitField.setFont(new Font("Calibri", Font.BOLD, 20));
		smallUnitField.setBorder(new EmptyBorder(0, 15, 0, 0));
		smallUnitField.setBounds(350, 202, 154, 44);
		contentPane.add(smallUnitField);
		
		JLabel label = new JLabel("Quy đổi:");
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		label.setBounds(429, 276, 75, 19);
		contentPane.add(label);
		
		comboBox = new JComboBox<>();
		comboBox.setBounds(426, 297, 75, 44);
		comboBox.setFont(new Font("Calibri", Font.PLAIN, 20));
		for (int index = 0; index < 100; index++)
			comboBox.addItem(new Integer(index));
		contentPane.add(comboBox);
		
		JLabel nameKindOfResourceLabel = new JLabel("Tên nhóm thực phẩm:");
		nameKindOfResourceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameKindOfResourceLabel.setBounds(75, 278, 177, 19);
		contentPane.add(nameKindOfResourceLabel);
		
		nameKindField = new JTextField();
		nameKindField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameKindField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameKindField.setBounds(75, 297, 278, 44);
		contentPane.add(nameKindField);
		
		JLabel priceLabel = new JLabel("Giá tiền:");
		priceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		priceLabel.setBounds(162, 374, 177, 19);
		contentPane.add(priceLabel);
		
		priceField = new MoneyTextField();
		priceField.setFont(new Font("Calibri", Font.BOLD, 20));
		priceField.setBorder(new EmptyBorder(0, 15, 0, 0));
		priceField.setBounds(162, 393, 278, 44);
		contentPane.add(priceField);
		
		button = new JButton("Thêm thực phẩm");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 460, 449, 49);
		contentPane.add(button);
	}
}

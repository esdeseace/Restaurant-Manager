package dialog;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import connection.Sever;
import controller.FoodController;
import model.FoodModel;
import object.Food;
import subComponent.MoneyTextField;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class FoodDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JButton button;
	private JLabel mainBanner;
	private JTextField nameField;
	private MoneyTextField priceField;
	private JTextField unitField;
	private JTextField kindField;
	
	private FoodController foodController;
	private FoodModel foodModel;
	private Food selectedFood;
	
	private boolean isEdit;
	
	public FoodDialog( FoodController foodController, boolean isEdit) {
		
		this.foodController = foodController;
		this.isEdit = isEdit;
		setIconImage(Sever.icon.getImage());
		setView();
		setEvent();
		
		if (isEdit) {
			
			setTitle("Sửa món ăn".toUpperCase());
			mainBanner.setText("SỬA MÓN ĂN");
			button.setText("Sửa Món Ăn");
			
			selectedFood = foodController.getSelectedFood();
			
			String name = selectedFood.getName();
			String price = Sever.numberFormat.format(selectedFood.getPrice());
			String unit = selectedFood.getSmallUnit();
			
			nameField.setText(name);
			priceField.setText(price);
			unitField.setText(unit);
			kindField.setText(selectedFood.getKindOfFood());
			
		} else {
			setTitle("Thêm món ăn".toUpperCase());
		}
	}
	
	private void setEvent() {
		
		foodModel = foodController.getFoodModel();
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String name = nameField.getText();
				String unit = unitField.getText();
				String priceString = priceField.getText();
				String kindOfFood = kindField.getText();
				
				if (name.isEmpty())
					JOptionPane.showMessageDialog(null, "Tên món ăn không được trống!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				else if (unit.isEmpty())
					JOptionPane.showMessageDialog(null, "Đơn vị tính không được trống!", 
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
								"Thêm/Sửa món ăn", JOptionPane.YES_NO_OPTION);
						
						if (isOk == 0) {
						
							if (kindOfFood.isEmpty())
								kindOfFood = "Món ăn khác";
							
							if (isEdit)
								foodModel.editFood(selectedFood, name, unit, price, kindOfFood);
							else
								foodModel.addFood(name, unit, price, kindOfFood);
							foodController.updateTabbedPane();
						}
						dispose();
					} catch (ParseException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Giá tiền không hợp lệ! Vui lòng xem lại!!",
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(619, 511);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mainBanner = new JLabel("THÊM MÓN ĂN");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(mainBanner);
		
		JLabel nameLabel = new JLabel("Tên món ăn:");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameLabel.setBounds(75, 85, 108, 19);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameField.setBounds(75, 104, 449, 44);
		contentPane.add(nameField);
		
		JLabel priceLabel = new JLabel("Giá bán:");
		priceLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		priceLabel.setBounds(75, 177, 90, 19);
		contentPane.add(priceLabel);
		
		priceField = new MoneyTextField();
		priceField.setFont(new Font("Calibri", Font.BOLD, 20));
		priceField.setBorder(new EmptyBorder(0, 15, 0, 0));
		priceField.setBounds(75, 198, 173, 44);
		contentPane.add(priceField);
		
		JLabel unitLabel = new JLabel("Đơn vị tính");
		unitLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		unitLabel.setBounds(351, 177, 108, 19);
		contentPane.add(unitLabel);
		
		unitField = new JTextField();
		unitField.setFont(new Font("Calibri", Font.BOLD, 20));
		unitField.setBorder(new EmptyBorder(0, 15, 0, 0));
		unitField.setBounds(351, 196, 173, 44);
		contentPane.add(unitField);
		
		JLabel kindLabel = new JLabel("Loại món ăn");
		kindLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		kindLabel.setBounds(75, 273, 108, 19);
		contentPane.add(kindLabel);
		
		kindField = new JTextField();
		kindField.setFont(new Font("Calibri", Font.BOLD, 20));
		kindField.setBorder(new EmptyBorder(0, 15, 0, 0));
		kindField.setBounds(75, 292, 449, 44);
		contentPane.add(kindField);

		button = new JButton("Thêm món ăn");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 378, 449, 49);
		contentPane.add(button);
	}
}

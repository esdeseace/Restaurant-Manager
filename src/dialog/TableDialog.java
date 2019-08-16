package dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import connection.Sever;
import controller.TableController;
import model.DinnerTableModel;
import object.DinnerTable;

public class TableDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField nameField;
	private JComboBox<Integer> floorCombo;
	private JButton button;

	private DinnerTableModel dinnerTableModel;
	private TableController tableController;
	
	public TableDialog( TableController tableController) {
	
		this.tableController = tableController;
		this.dinnerTableModel= tableController.getDinnerTableModel();
		setTitle("Thêm bàn".toUpperCase());
		setIconImage(Sever.icon.getImage());
		setView();
		setEvent();
	}
	
	private void setEvent() {
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String name = nameField.getText();
				int floor = (int) floorCombo.getSelectedItem();
				
				if (name.isEmpty())
					JOptionPane.showMessageDialog(null, "Tên bàn không được trống!", 
						"Lỗi", JOptionPane.WARNING_MESSAGE);
				else {
					DinnerTable table = dinnerTableModel.addDinnerTable(name, floor);
					tableController.addFloorPane(table);
					dispose();
				}
			}
		});
	}
	
	private void setView() {
		
		contentPane = new JPanel();
		setSize(619, 430);
		setLocationRelativeTo(this);
		contentPane.setLayout(null);
		setContentPane(contentPane);
	
		JLabel mainBanner = new JLabel("THÊM BÀN ĂN");
		mainBanner.setFont(new Font("Calibri", Font.PLAIN, 35));
		mainBanner.setBounds(30, 28, 524, 44);
		mainBanner.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(mainBanner);
		
		JLabel nameLabel = new JLabel("Tên bàn ăn:");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		nameLabel.setBounds(75, 85, 108, 19);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Calibri", Font.BOLD, 20));
		nameField.setBorder(new EmptyBorder(0, 15, 0, 0));
		nameField.setBounds(75, 104, 449, 44);
		contentPane.add(nameField);
		
		JLabel floorLabel = new JLabel("Tầng:");
		floorLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		floorLabel.setBounds(202, 179, 108, 19);
		contentPane.add(floorLabel);
		
		Integer[] list = {1, 2, 3, 4, 5};
		
		floorCombo = new JComboBox<>(list);
		floorCombo.setFont(new Font("Calibri", Font.BOLD, 20));
		floorCombo.setBorder(new EmptyBorder(0, 15, 0, 0));
		floorCombo.setBounds(202, 198, 108, 44);
		contentPane.add(floorCombo);

		button = new JButton("Thêm bàn ăn");
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(75, 291, 449, 49);
		contentPane.add(button);
	}
}

package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import subComponent.MenuItemPane;

public class StaffPane extends MenuItemPane {

	private static final long serialVersionUID = 1L;

	private JButton addButton;
	private JButton deleteButton;
	
	private JPanel mainPane;

	public JButton getDeleteButton() {
		return this.deleteButton;
	}
	
	public JPanel getMainPane() {
		return this.mainPane;
	}
	
	public JButton getAddButton() {
		return this.addButton;
	}

	public StaffPane() {
		
		this.setLayout(new BorderLayout(10, 10));
				
		JPanel menu = new JPanel();
		menu.setOpaque(true);
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		add(menu, BorderLayout.SOUTH);

		final ImageIcon addIcon = new ImageIcon(StaffPane.class.getResource("/image/add.png"));
		final ImageIcon deleteIcon = new ImageIcon(StaffPane.class.getResource("/image/add.png"));
		
		addButton = new JButton("Thêm", addIcon);
		addButton.setVerticalTextPosition(JButton.CENTER);
		addButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(addButton);

		deleteButton = new JButton("Xoá", deleteIcon);
		deleteButton.setVerticalTextPosition(JButton.CENTER);
		deleteButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(deleteButton);
		
		TitledBorder titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"NHÂN VIÊN", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		
		mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		mainPane.setBackground(Color.WHITE);
		mainPane.setBorder(titleBorder);
		add(mainPane, BorderLayout.CENTER);
	}
	
}

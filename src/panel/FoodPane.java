package panel;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import subComponent.MenuItemPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.Font;

public class FoodPane extends MenuItemPane {

	private static final long serialVersionUID = 1L;

	private JButton addButton;

	private JTabbedPane tabbedPane;
	
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}
	
	public JButton getAddButton() {
		return this.addButton;
	}
	
	public FoodPane() {
		
		this.setLayout(new BorderLayout(10, 10));
		
		TitledBorder titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"MÓN ĂN", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Calibri", Font.PLAIN, 20));
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBorder(titleBorder);
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void createMenu() {
		
		JPanel menu = new JPanel();
		menu.setOpaque(true);
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		add(menu, BorderLayout.SOUTH);
		
		final ImageIcon icon = new ImageIcon(FoodPane.class.getResource("/image/add.png"));
		
		addButton = new JButton("Thêm", icon);
		addButton.setVerticalTextPosition(JButton.CENTER);
		addButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(addButton);
	}
}

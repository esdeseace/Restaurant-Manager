package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import subComponent.MenuItemPane;

public class ResourcePane extends MenuItemPane {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private JButton addButton;
	
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}
	
	public JButton getAddButton() {
		return this.addButton;
	}
	
	public ResourcePane() {
		
		setLayout(new BorderLayout(0, 10));
		
		TitledBorder titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"THỰC PHẨm", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font("Calibri", Font.PLAIN, 20));
		tabbedPane.setBorder(titleBorder);
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel menu = new JPanel();
		add(menu, BorderLayout.SOUTH);
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

		final ImageIcon icon = new ImageIcon(ResourcePane.class.getResource("/image/add.png"));
		
		addButton = new JButton("Thêm", icon);
		addButton.setVerticalTextPosition(JButton.CENTER);
		addButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(addButton);
	}
	
}

package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

public class TablePane extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ImageIcon addSmall = new ImageIcon(TablePane.class.getResource("/image/addSmall.png"));
	private final ImageIcon deleteSmall = new ImageIcon(TablePane.class.getResource("/image/deleteSmall.png"));
	
	private JButton addButton;
	private JButton deleteButton;
	private JTabbedPane tabbedPane;
	
	public JButton getAddButton() {
		return this.addButton;
	}
	
	public JButton getDeleteButton() {
		return this.deleteButton;
	}
	
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}
	
	public TablePane() {
		
		setLayout(new BorderLayout(5, 0));
		setBorder(new LineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		
		JPanel menuTable = new JPanel();
		menuTable.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(menuTable, BorderLayout.NORTH);
		
		addButton = new JButton("Thêm Bàn", addSmall);
		addButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		menuTable.add(addButton);
		
		deleteButton = new JButton("Xóa Bàn", deleteSmall);
		deleteButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		menuTable.add(deleteButton);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
	}
}

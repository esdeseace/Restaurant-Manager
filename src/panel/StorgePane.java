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

public class StorgePane extends MenuItemPane {

	private static final long serialVersionUID = 1L;

	private JButton exportButton;
	private JButton importButton;
	private JPanel menu;
	private JTabbedPane tabbedPane;
	
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}
	
	public JButton getExportButton() {
		return this.exportButton;
	}
	
	public JButton getImportButton() {
		return this.importButton;
	}
	
	public StorgePane() {
		
		this.setLayout(new BorderLayout(10, 10));
		
		TitledBorder titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				"QUẢN LÝ KHO", TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font("Calibri", Font.PLAIN, 20));
		tabbedPane.setBorder(titleBorder);
		add(tabbedPane, BorderLayout.CENTER);
		
		menu = new JPanel();
		add(menu, BorderLayout.SOUTH);
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		
		final ImageIcon importIcon = new ImageIcon(StorgePane.class.getResource("/image/import.png"));
		final ImageIcon exportIcon = new ImageIcon(StorgePane.class.getResource("/image/export.png"));
		
		importButton = new JButton("Nhập hàng", importIcon);
		importButton.setVerticalTextPosition(JButton.CENTER);
		importButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(importButton);
		
		exportButton = new JButton("Xuất hàng", exportIcon);
		exportButton.setVerticalTextPosition(JButton.CENTER);
		exportButton.setFont(new Font("Calibri", Font.PLAIN, 25));
		menu.add(exportButton);
	}
}

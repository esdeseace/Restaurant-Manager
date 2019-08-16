package panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import subComponent.MenuItemPane;

public class StatisticPane extends MenuItemPane {

	private static final long serialVersionUID = 1L;

	private JButton selectButton;
	private JButton reportButton;
	private JPanel center;

	public JButton getSelectButton() {
		return this.selectButton;
	}
	
	public JButton getReportButton() {
		return this.reportButton;
	}
	
	public JPanel getCenter() {
		return this.center;
	}
	
	public StatisticPane() {
	
		setLayout(new BorderLayout(10, 10));
		
		JPanel menu = new JPanel();
		add(menu, BorderLayout.NORTH);
		menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		final ImageIcon selectIcon = new ImageIcon(StatisticPane.class.getResource("/image/today.png"));
		final ImageIcon reportIcon = new ImageIcon(StatisticPane.class.getResource("/image/reportSmall.png"));
		
		selectButton = new JButton("Chọn ngày", selectIcon);
		selectButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		menu.add(selectButton);
		
		reportButton = new JButton("Xem báo cáo", reportIcon);
		reportButton.setFont(new Font("Calibri", Font.PLAIN, 20));
		menu.add(reportButton);
		
		center = new JPanel();
		add(center, BorderLayout.CENTER);
	}
}

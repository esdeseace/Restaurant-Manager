package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import connection.Sever;

public class ReportTablePane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable reportTable;

	private TitledBorder titleBorder;
	private JLabel label;
	
	public void setTotal(int total) {
		String money = Sever.numberFormat.format(total);
		label.setText(money);
	}
	
	public JTable getReportTable() {
		return this.reportTable;
	}
	
	public ReportTablePane(String text) {
		
		this.setLayout(new BorderLayout());
		titleBorder = BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK, 2),
				text, TitledBorder.CENTER, TitledBorder.TOP);
		titleBorder.setTitleFont(new Font("Calibri", Font.BOLD, 20));
		this.setBorder(titleBorder);
		
		reportTable = new JTable() {
			private static final long serialVersionUID = 1L;
					
			@Override
			public boolean isCellEditable(int row, int column) {                
				return false;
			};
			
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row,
					int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				if (col == 1 || col == 4) {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				}
				return comp;
			}
		};
		
		JTableHeader header = reportTable.getTableHeader();
		TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
		
		JLabel headerLabel = (JLabel) rendererFromHeader;
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		header.setFont(new Font("Calibri", Font.BOLD, 20));
		reportTable.setRowHeight(40);
		reportTable.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.add(new JScrollPane(reportTable), BorderLayout.CENTER);
	}
	
	public void createLabel() {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		this.add(menuPanel, BorderLayout.SOUTH);
		
		JLabel defLabel = new JLabel("Tổng cộng:");
		defLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
		menuPanel.add(defLabel);
		
		label = new JLabel();
		label.setForeground(Color.RED);
		label.setFont(new Font("Calibri", Font.BOLD, 40));
		menuPanel.add(label);
	}
	
}

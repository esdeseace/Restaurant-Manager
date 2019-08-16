package dialog;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.Font;

import javax.swing.border.LineBorder;

import com.toedter.calendar.JDateChooser;

import connection.Sever;
import controller.StatisticController;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.awt.event.ActionEvent;

public class DayDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public DayDialog(StatisticController statisticController) {
		
		setIconImage(Sever.icon.getImage());
		JPanel contentPane = new JPanel();
		setSize(499, 282);
		setTitle("Thống kê".toUpperCase());
		setLocationRelativeTo(this);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel everyPanel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		everyPanel.setBorder(new LineBorder(Color.GRAY));
		everyPanel.setBounds(32, 28, 146, 136);
		contentPane.add(everyPanel);
		everyPanel.setLayout(null);
		
		JRadioButton dayRadio = new JRadioButton("Hàng ngày");
		dayRadio.setActionCommand("Day");
		dayRadio.setSelected(true);
		dayRadio.setBounds(8, 9, 123, 40);
		dayRadio.setFont(new Font("Calibri", Font.PLAIN, 20));
		everyPanel.add(dayRadio);
		group.add(dayRadio);
		
		JRadioButton monthRadio = new JRadioButton("Hàng tháng");
		monthRadio.setActionCommand("Month");
		monthRadio.setBounds(8, 49, 123, 40);
		monthRadio.setFont(new Font("Calibri", Font.PLAIN, 20));
		everyPanel.add(monthRadio);
		group.add(monthRadio);
		
		JRadioButton yearRadio = new JRadioButton("Hàng năm");
		yearRadio.setActionCommand("Year");
		yearRadio.setBounds(8, 89, 123, 40);
		yearRadio.setFont(new Font("Calibri", Font.PLAIN, 20));
		everyPanel.add(yearRadio);
		group.add(yearRadio);
		
		JPanel dayPanel = new JPanel();
		dayPanel.setLayout(null);
		dayPanel.setBorder(new LineBorder(Color.GRAY));
		dayPanel.setBounds(217, 65, 238, 99);
		contentPane.add(dayPanel);
		
		JLabel fromLabel = new JLabel("Từ:");
		fromLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		fromLabel.setBounds(12, 13, 55, 30);
		dayPanel.add(fromLabel);
		
		Calendar fromCurrent = Calendar.getInstance();
		fromCurrent.set(Calendar.MILLISECOND, 0);
		fromCurrent.set(Calendar.SECOND, 0);
		fromCurrent.set(Calendar.MINUTE, 0);
		fromCurrent.set(Calendar.HOUR_OF_DAY, 0);
		
		JDateChooser fromDateChooser = new JDateChooser(fromCurrent.getTime());
		fromDateChooser.setDateFormatString("dd/MM/yyyy");
		fromDateChooser.setFont(new Font("Calibri", Font.PLAIN, 20));
		fromDateChooser.setBounds(54, 13, 169, 30);
		dayPanel.add(fromDateChooser);

		JLabel tolabel = new JLabel("Đến:");
		tolabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		tolabel.setBounds(12, 56, 55, 30);
		dayPanel.add(tolabel);
		
		Calendar toCurrent = Calendar.getInstance();
		toCurrent.set(Calendar.MILLISECOND, 999);
		toCurrent.set(Calendar.SECOND, 59);
		toCurrent.set(Calendar.MINUTE, 59);
		toCurrent.set(Calendar.HOUR_OF_DAY, 23);
		
		JDateChooser toDateChooser = new JDateChooser(toCurrent.getTime());
		toDateChooser.setDateFormatString("dd/MM/yyyy");
		toDateChooser.setFont(new Font("Calibri", Font.PLAIN, 20));
		toDateChooser.setBounds(54, 56, 169, 30);
		dayPanel.add(toDateChooser);
		
		JButton button = new JButton("Xem");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (fromDateChooser.getDate() != null && toDateChooser.getDate() != null) {	
					
					Date fromDate = new Date(fromDateChooser.getDate().getTime());
					Date toDate = new Date(toDateChooser.getDate().getTime());
					if (toDate.compareTo(fromDate) >= 0) {

						String selection = group.getSelection().getActionCommand();
						statisticController.setView(fromDate, toDate, selection);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Lỗi chọn ngày. Yêu cầu chọn lại!", "Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Chưa chọn ngày tháng", "Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		button.setFont(new Font("Calibri", Font.BOLD, 20));
		button.setBounds(158, 185, 97, 37);
		contentPane.add(button);
	}
}

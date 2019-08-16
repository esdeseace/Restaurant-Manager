package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import connection.Sever;
import panel.ReportTablePane;

public class PayWageController {

	private final String[] TITLES = {"STT", "Họ tên", "Lương", "Tiền ứng", "Tiền thưởng", "Tổng tiền", "Ngày"};
	
	private Connection connection;
	private ReportTablePane reportTablePane;
	private TableColumnModel columnModel;
	
	public PayWageController(ReportTablePane reportTablePane) {
		connection = Sever.connection;
		this.reportTablePane = reportTablePane;
		columnModel = reportTablePane.getReportTable().getColumnModel();
	}
	
	public void updateTable() {
		
		DefaultTableModel defaultTableModel = new DefaultTableModel(null, TITLES);
		reportTablePane.getReportTable().setModel(defaultTableModel);
		
		try {
			
			Statement statement = connection.createStatement();
			String sqlQuery = "SELECT * FROM TRALUONG ORDER BY NGAY DESC";
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			int count = 0;
			int allMoney = 0;
			
			while(resultSet.next()) {
				String stt = String.valueOf( ++ count );
				String name = resultSet.getString(1);
				
				int salary = resultSet.getInt(2);
				int bonus = resultSet.getInt(3);
				int advanceMoney = resultSet.getInt(4);
				int total = salary + bonus + advanceMoney;
				allMoney += total;
				
				Timestamp timestamp = resultSet.getTimestamp(5);
				Date day = new Date(timestamp.getTime());
				String date = Sever.fullTimeFormat.format(day);
				
				String money_1 = Sever.numberFormat.format( salary );
				String money_2 = Sever.numberFormat.format( bonus );
				String money_3 = Sever.numberFormat.format( advanceMoney );
				String money_4 = Sever.numberFormat.format( total );
				
				Object[] row = {stt, name, money_1, money_2, money_3, money_4, date};
				defaultTableModel.addRow(row);
			}
			
			reportTablePane.setTotal(allMoney);
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int[] positions = {0, 2, 3, 4, 5, 6};
		int[] widths = {100, 140, 140, 140, 140, 280};
		setWidth(positions, widths);
	}
	
	private void setWidth(int[] positions, int[] widths) {
	
		TableColumn column;
		for (int index = 0; index < positions.length; index++) {
			column = columnModel.getColumn(positions[index]);
			column.setMinWidth(widths[index]);
			column.setMaxWidth(widths[index]);
			column.setPreferredWidth(widths[index]);
		}
	}
}

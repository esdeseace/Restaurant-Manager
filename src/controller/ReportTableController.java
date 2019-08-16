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

public class ReportTableController {

	private final String[] TITLES = {"STT", "Họ tên", "Số tiền", "Ngày", "Nhân viên thực hiện"};
	
	private Connection connection;

	private TableColumnModel columnModel;

	public ReportTableController() {
		connection = Sever.connection;
	}
	
	public void updateTable(ReportTablePane reportTablePane, String tableName) {
		
		DefaultTableModel defaultTableModel = new DefaultTableModel(null, TITLES);
		reportTablePane.getReportTable().setModel(defaultTableModel);
		columnModel = reportTablePane.getReportTable().getColumnModel();
		
		int total = 0;
		
		try {
			Statement statement = connection.createStatement();
			String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY NGAY DESC";
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			int count = 0;
			
			while(resultSet.next()) {
				String stt = String.valueOf( ++ count );
				String name = resultSet.getString(1);
				int money = resultSet.getInt(2);
				total += money;
				String money_text = Sever.numberFormat.format( money );
				Timestamp timestamp = resultSet.getTimestamp(3);
				Date day = new Date(timestamp.getTime());
				String date = Sever.fullTimeFormat.format(day);
				String staff = resultSet.getString(4);
				Object[] row = {stt, name, money_text, date, staff};
				defaultTableModel.addRow(row);
			}
			
			reportTablePane.setTotal(total);
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int[] positions = {0, 2, 3};
		int[] widths = {100, 200, 280};
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

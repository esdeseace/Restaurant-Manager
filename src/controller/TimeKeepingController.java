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

public class TimeKeepingController {

	private final String[] TITLES = {"STT", "Họ tên", "Ngày chấm công"};
	
	private Connection connection;
	private ReportTablePane reportTablePane;
	private TableColumnModel columnModel;
	
	public TimeKeepingController(ReportTablePane reportTablePane) {
		connection = Sever.connection;
		this.reportTablePane = reportTablePane;
		columnModel = reportTablePane.getReportTable().getColumnModel();
	}
	
	public void updateTable() {
		
		DefaultTableModel defaultTableModel = new DefaultTableModel(null, TITLES);
		reportTablePane.getReportTable().setModel(defaultTableModel);
		
		try {
			Statement statement = connection.createStatement();
			String sqlQuery = "SELECT * FROM CHAMCONG ORDER BY NGAY DESC";
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			int count = 0;
			
			while(resultSet.next()) {
				String stt = String.valueOf( ++ count );
				String name = resultSet.getString(1);
				Timestamp timestamp = resultSet.getTimestamp(2);
				Date day = new Date(timestamp.getTime());
				String date = Sever.fullTimeFormat.format(day);
				
				Object[] row = {stt, name, date};
				defaultTableModel.addRow(row);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int[] positions = {0};
		int[] widths = {100};
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

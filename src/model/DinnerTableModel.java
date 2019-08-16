package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connection.Sever;
import object.DinnerTable;

public class DinnerTableModel {

	private Connection connection;
	private ArrayList<DinnerTable> tables;
	
	public DinnerTableModel() {
		this.connection = Sever.connection;
		tables = new ArrayList<>();
	}
	
	public ArrayList<DinnerTable> getDinnerTables() {
		return this.tables;
	}
	
	public boolean isHaveFloor(int floor) {
		for (DinnerTable table : tables)
			if (table.getFloor() == floor)
				return true;
		return false;
	}
	
	public DinnerTable getDinnerTableById(int id) {

		for (DinnerTable table : tables)
			if (table.getId() == id)
				return table;
		return null;
	}

	public void deleteDinnerTable(int id) {
		
		try {

			PreparedStatement preStatement = connection.prepareStatement("DELETE FROM BANAN WHERE IDBANAN = ?");
			preStatement.setInt(1, id);
			preStatement.executeUpdate();
			preStatement.close();
			
			DinnerTable table = this.getDinnerTableById(id);
			tables.remove(table);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DinnerTable addDinnerTable(String name, int floor) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO BANAN VALUES (?, ?, ?, ?)");
			int id = tables.get(tables.size() - 1).getId() + 1;
			int idOfBill = 0;
			preStatement.setInt(1, id);
			preStatement.setString(2, name);
			preStatement.setInt(3, floor);
			preStatement.setInt(4, idOfBill);
			preStatement.executeUpdate();
			preStatement.close();
			
			DinnerTable table = new DinnerTable(id, name, floor, idOfBill);
			tables.add(table);
			return table;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void connectDatabase() {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM BANAN");
			while(resultSet.next()) {
				
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				int floor = resultSet.getInt(3);
				int idOfBill = resultSet.getInt(4);
				
				DinnerTable table = new DinnerTable(id, name, floor, idOfBill);
				tables.add(table);
			}
			
			resultSet.close();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

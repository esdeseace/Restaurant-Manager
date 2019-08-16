package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import connection.Sever;
import object.Food;

public class FoodModel {
	
	private Connection connection;
	
	private ArrayList<Food> foods;
	private Vector<Food> resources;
	int maxId = 0;
	
	public FoodModel() {
		this.connection = Sever.connection;
		foods = new ArrayList<>();
		resources = new Vector<>();
	}
	
	public ArrayList<Food> getFoods() {
		return this.foods;
	}
	
	public Food getFoodById(int id) {
		for (Food food : foods)
			if (food.getId() == id)
				return food;
		return null;
	}
	
	public Vector<Food> getResources() {
		return this.resources;
	}
	
	public void setAmount(Food food, int bigAmount, int smallAmount) {
		try {
			
			String sqlQuery = "UPDATE MONAN "
					+ "SET SOLUONGLON = ?, SOLUONGNHO = ? WHERE IDMONAN = ?";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			
			preStatement.setInt(1, bigAmount);
			preStatement.setInt(2, smallAmount);
			preStatement.setInt(3, food.getId());
			preStatement.executeUpdate();
			preStatement.close();
			
			food.setAmount(bigAmount, smallAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setFood(Food food, String name,String bigUnit, String smallUnit, int price, int convert, String kindOfFood) {
		
		try {
			
			String sqlQuery = "UPDATE MONAN "
					+ "SET TENMONAN = ?, DONVILON = ?, DONVINHO = ?, DONGIA = ?, QUYDOI = ?, NHOMMONAN = ? WHERE IDMONAN = ?";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			
			preStatement.setString(1, name);
			preStatement.setString(2, bigUnit);
			preStatement.setString(3, smallUnit);
			preStatement.setInt(4, price);
			preStatement.setInt(5, convert);
			preStatement.setString(6, kindOfFood);
			preStatement.setInt(7, food.getId());
			preStatement.executeUpdate();
			preStatement.close();
			food.setFood(name, bigUnit, smallUnit, price, convert, kindOfFood);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFood(String name,String bigUnit, String smallUnit, int price, int convert, String kindOfFood) {
		
		try {
			
			String sqlQuery = "INSERT INTO MONAN VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			
			preStatement.setBoolean(1, true);
			preStatement.setInt(2, ++maxId);
			preStatement.setString(3, name);
			preStatement.setInt(4, 0);
			preStatement.setString(5, bigUnit);
			preStatement.setInt(6, 0);
			preStatement.setString(7, smallUnit);
			preStatement.setInt(8, price);
			preStatement.setInt(9, convert);
			preStatement.setString(10, kindOfFood);
			preStatement.executeUpdate();
			preStatement.close();
			
			Food food = new Food(maxId, name, bigUnit, smallUnit, price, 0, 0, convert, kindOfFood);
			resources.addElement(food);
			foods.add(food);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFood(String name, String unit, int price, String kindOfFood) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO MONAN "
					+ "(NGUYENLIEU, IDMONAN, TENMONAN, DONVINHO, DONGIA, NHOMMONAN) VALUES (?, ?, ?, ?, ?, ?)");
			
			preStatement.setBoolean(1, false);
			preStatement.setInt(2, ++maxId);
			preStatement.setString(3, name);
			preStatement.setString(4, unit);
			preStatement.setInt(5, price);
			preStatement.setString(6, kindOfFood);
			preStatement.executeUpdate();
			preStatement.close();
			
			Food food = new Food(maxId, name, price, unit, kindOfFood);
			foods.add(food);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editFood(Food food, String name, String unit, int price, String kindOfFood) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("UPDATE MONAN "
					+ "SET TENMONAN = ?, DONGIA = ?, DONVINHO = ?, NHOMMONAN = ? WHERE IDMONAN = ?");
			preStatement.setString(1, name);
			preStatement.setInt(2, price);
			preStatement.setString(3, unit);
			preStatement.setString(4, kindOfFood);
			preStatement.setInt(5, food.getId());
			preStatement.executeUpdate();
			preStatement.close();
			food.setFood(name, unit, price, kindOfFood);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void connectDatabase() {

		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM MONAN ORDER BY NHOMMONAN");
			while(resultSet.next()) {
				
				boolean isResource = resultSet.getBoolean(1);
				int id = resultSet.getInt(2);
				if (id > maxId)
					maxId = id;
				String name = resultSet.getString(3);
				String smallUnit = resultSet.getString(7);
				int price = resultSet.getInt(8);
				String kindOfFood = resultSet.getString(10);
				
				Food food = null;
				if (!isResource) {
					 food = new Food(id, name, price, smallUnit, kindOfFood);
				} else {
					int bigAmount = resultSet.getInt(4);
					String bigUnit = resultSet.getString(5);
					int smallAmount = resultSet.getInt(6);
					int convert = resultSet.getInt(9);
					food = new Food(id, name, bigUnit, smallUnit, price, bigAmount, smallAmount, convert, kindOfFood);
					resources.addElement(food);
				}
				foods.add(food);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

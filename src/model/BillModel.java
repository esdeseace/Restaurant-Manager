package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import connection.Sever;
import object.Bill;
import object.DetailBill;
import object.DinnerTable;
import object.Food;

public class BillModel {
	
	private Connection connection;
	
	private ArrayList<Bill> bills;
	private FoodModel foodModel;
	private int maxId = 0;
	
	public BillModel(FoodModel foodModel) {
		
		this.connection = Sever.connection;
		this.foodModel = foodModel;
		
		bills = new ArrayList<>();
	}
	
	public ArrayList<Bill> getBills() {
		return this.bills;
	}
	
	public Bill getBillById(int id) {
		
		for (Bill bill : bills)
			if (bill.getId() == id)
				return bill;
		return null;
	}
	
	public void removeFromStorge(Bill bill) {
		
		HashMap<Food, DetailBill> foods = bill.getFoods();
		for (Food food : foods.keySet()) {
			if (food.isResource()) {
				
				DetailBill info = foods.get(food);
				int amount = info.getAmount();
				int bigA = 0;
				int smallA = 0;
				
				if (food.getAmount() < amount) {
					bigA = 0;
					smallA = food.getAmount() - amount;
				} else if (food.getSmallAmount() >= amount) {
					bigA = food.getBigAmount();
					smallA = food.getSmallAmount() - amount;
				} else {
					amount = amount - food.getSmallAmount();
					int tmp = amount / food.getConvert() + 1;
					bigA = food.getBigAmount() - tmp;
					smallA = tmp * food.getConvert() - amount;
				}
				foodModel.setAmount(food, bigA, smallA);
			}
		}
	}
	
	public void finishBill(Bill bill, DinnerTable table) {
		
		try {
			
			String sqlQuery = "UPDATE HOADON SET KETTHUC = ?, THUCTHU = ?, TENBANAN = ? WHERE IDHOADON = ?";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			preStatement.setTime(1, bill.getEnd());
			preStatement.setInt(2, bill.getRealTotal());
			preStatement.setString(3, table.getName());
			preStatement.setInt(4, bill.getId());
			preStatement.executeUpdate();
			preStatement.close();

			preStatement = connection.prepareStatement("UPDATE BANAN SET IDHOADON = 0 WHERE IDBANAN = ?");
			preStatement.setInt(1, table.getId());
			preStatement.executeUpdate();
			preStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFood(Bill bill, Food food) {

		try {
			
			PreparedStatement preStatement = connection.prepareStatement("DELETE FROM CHITIETHOADON WHERE IDHOADON = ? AND IDMONAN = ?");
			preStatement.setInt(1, bill.getId());
			preStatement.setInt(2, food.getId());
			preStatement.executeUpdate();
			preStatement.close();
			
			bill.deleteFood(food);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setFood(Food food, Bill bill, int amount) {
		
		try {
			
			String sqlQuery = "UPDATE CHITIETHOADON SET SOLUONG = ? WHERE IDHOADON = ? AND IDMONAN = ?";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			preStatement.setInt(1, amount);
			preStatement.setInt(2, bill.getId());
			preStatement.setInt(3, food.getId());
			preStatement.executeUpdate();
			preStatement.close();
			
			bill.setFood(food, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addFoodToBill(Food food, Bill bill, int amount) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?)");
			preStatement.setInt(1, bill.getId());
			preStatement.setInt(2, food.getId());
			preStatement.setInt(3, amount);
			preStatement.setInt(4, food.getPrice());
			preStatement.executeUpdate();
			preStatement.close();

			bill.addFood(food, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBill(Bill bill, DinnerTable table) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("UPDATE BANAN SET IDHOADON = 0 WHERE IDBANAN = ?");
			preStatement.setInt(1, table.getId());
			preStatement.executeUpdate();
			
			preStatement = connection.prepareStatement("DELETE FROM HOADON WHERE IDHOADON = ?");
			preStatement.setInt(1, bill.getId());
			preStatement.executeUpdate();
			preStatement.close();
			
			table.setIdOfBill(0);
			bills.remove(bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addBill(DinnerTable table) {
	
		try {
			
			Bill bill = new Bill(++maxId, Sever.staff.getName());
			table.setIdOfBill(bill.getId());
			bills.add(0, bill);
			
			String sqlQuery = "INSERT INTO HOADON (IDHOADON, BATDAU, NGAY, HOTEN) VALUES (?, ?, ?, ?)";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			preStatement.setInt(1, maxId);
			preStatement.setTime(2, bill.getStart());
			preStatement.setDate(3, bill.getDay());
			preStatement.setString(4, bill.getName());
			preStatement.executeUpdate();
			preStatement.close();
			
			preStatement = connection.prepareStatement("UPDATE BANAN SET IDHOADON = ? WHERE IDBANAN = ?");
			preStatement.setInt(1, maxId);
			preStatement.setInt(2, table.getId());
			preStatement.executeUpdate();
			preStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connectDatabase() {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOADON ORDER BY NGAY DESC");
			while(resultSet.next()) {
				
				int id = resultSet.getInt(1);
				if (id > maxId)
					maxId = id;
				Time start = resultSet.getTime(2);
				Time end = resultSet.getTime(3);
				Date day = resultSet.getDate(4);
				String name = resultSet.getString(5);
				int realTotal = resultSet.getInt(6);
				String nameOfTable = resultSet.getString(7);
				
				Bill bill = new Bill(id, start, end, day, name, realTotal, nameOfTable);
				bills.add(bill);
				
				PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM CHITIETHOADON WHERE IDHOADON = ?");
				preStatement.setInt(1, id);
				ResultSet resultSetDetail = preStatement.executeQuery();
				while(resultSetDetail.next()) {
					int idOfFood = resultSetDetail.getInt(2);
					int amount = resultSetDetail.getInt(3);
					int price = resultSetDetail.getInt(4);
					
					DetailBill info = new DetailBill(amount, price);
					bill.addFood(foodModel.getFoodById(idOfFood), info);
				}
				resultSetDetail.close();
				preStatement.close();
			}
			
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

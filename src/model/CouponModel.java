package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import connection.Sever;
import object.Coupon;
import object.Food;

public class CouponModel {

	private ArrayList<Coupon> coupons;
	private Connection connection;
	private int maxId = 0;
	private FoodModel foodModel;
	
	public CouponModel(FoodModel foodModel) {
		
		this.coupons = new ArrayList<>();
		this.foodModel = foodModel;
		this.connection = Sever.connection;
	}
	
	public ArrayList<Coupon> getCoupons() {
		return this.coupons;
	}
	
	public void addFoodToCoupon(Coupon coupon, Food food, int bigNumber, int smallNumber, int price, String note, int total) {

		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO CHITIETPHIEU VALUES (?, ?, ?, ?, ?, ?, ?)");
			preStatement.setInt(1, coupon.getId());
			preStatement.setInt(2, food.getId());
			preStatement.setInt(3, bigNumber);
			preStatement.setInt(4, smallNumber);
			preStatement.setInt(5, price);
			preStatement.setInt(6, total);
			preStatement.setString(7, note);
			preStatement.executeUpdate();
			preStatement.close();
			
			coupon.addFood(food, bigNumber, smallNumber, price, note, total);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Coupon addCoupon(String supplier, String name, boolean isImport) {
		
		Coupon coupon = new Coupon(++maxId, supplier, name, isImport);
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO PHIEU VALUES (?, ?, ?, ?, ?)");
			preStatement.setInt(1, maxId);
			preStatement.setDate(2, coupon.getDay());
			preStatement.setString(3, name);
			preStatement.setString(4, supplier);
			preStatement.setBoolean(5, isImport);
			preStatement.executeUpdate();
			preStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		coupons.add(0, coupon);
		return coupon;
	}
	
	public void connectDatabase() {

		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM PHIEU ORDER BY NGAYNHAP DESC");
			while(resultSet.next()) {
				
				int id = resultSet.getInt(1);
				if (id > maxId)
					maxId = id;
				Timestamp timestamp = resultSet.getTimestamp(2);
				Date day = new Date(timestamp.getTime());
				String name = resultSet.getString(3);
				String supplier = resultSet.getString(4);
				boolean isImport = resultSet.getBoolean(5);
				
				Coupon coupon = new Coupon(id, day, name, supplier, isImport);
				coupons.add(coupon);
				
				PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM CHITIETPHIEU WHERE IDPHIEU = ?");
				preStatement.setInt(1, id);
				ResultSet resultSetDetail = preStatement.executeQuery();
				while(resultSetDetail.next()) {
					
					int idOfFood = resultSetDetail.getInt(2);
					int bigNumber = resultSetDetail.getInt(3);
					int smallNumber = resultSetDetail.getInt(4);
					int price = resultSetDetail.getInt(5);
					int total = resultSetDetail.getInt(6);
					String note = resultSetDetail.getString(7);
					
					Food food = foodModel.getFoodById(idOfFood);
					coupon.addFood(food, bigNumber, smallNumber, price, note, total);
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

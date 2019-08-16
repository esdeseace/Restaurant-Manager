package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connection.Sever;
import object.Staff;

public class StaffModel {
	
	private ArrayList<Staff> staffs;
	private Connection connection;
	
	public StaffModel() {
		
		this.connection = Sever.connection;
		this.staffs = new ArrayList<>();
	}
	
	public ArrayList<Staff> getStaffs() {
		return this.staffs;
	}
	
	public Staff getStaffByUsername(String username) {
		for (Staff staff : staffs)
			if (staff.getUsername().equals(username))
				return staff;
		return null;
	}
	
	public void deleteStaff(Staff staff) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("DELETE FROM NHANVIEN WHERE TAIKHOAN = ?");
			preStatement.setString(1, staff.getUsername());
			preStatement.executeUpdate();
			preStatement.close();
			staffs.remove(staff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void timekeeping(Staff staff) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO CHAMCONG VALUES (?, ?)");
			preStatement.setString(1, staff.getName());
			long curTime = System.currentTimeMillis();
			preStatement.setDate(2, new Date(curTime));
			preStatement.executeUpdate();
			
			preStatement = connection.prepareStatement("UPDATE NHANVIEN SET "
					+ "SOCONG = ?, CHAMCONG = ? WHERE TAIKHOAN = ?");
			staff.timekeeping();
			preStatement.setInt(1, staff.getTimekeeping());
			preStatement.setBoolean(2, true);
			preStatement.setString(3, staff.getUsername());
			preStatement.executeUpdate();
			preStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void payWage(Staff staff, int timekeeping) {
		
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO TRALUONG VALUES (?, ?, ?, ?, ?)");
			preStatement.setString(1, staff.getName());
			preStatement.setInt(2, staff.getSalary());
			preStatement.setInt(3, staff.getAdvanceMoney());
			preStatement.setInt(4, staff.getBonus());
			long curTime = System.currentTimeMillis();
			preStatement.setDate(5, new Date(curTime));
			preStatement.executeUpdate();
			
			preStatement = connection.prepareStatement("UPDATE NHANVIEN SET "
					+ "SOTIENTHUONG = ?, SOTIENUNG = ?, SOCONG = ? WHERE TAIKHOAN = ?");
			preStatement.setInt(1, 0);
			preStatement.setInt(2, 0);
			staff.payWage(timekeeping);
			preStatement.setInt(3, staff.getTimekeeping());
			preStatement.setString(4, staff.getUsername());
			preStatement.executeUpdate();
			preStatement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addBonus(Staff staff, int money, String name) {
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO TIENTHUONG VALUES (?, ?, ?, ?)");
			preStatement.setString(1, staff.getName());
			preStatement.setInt(2, money);
			long curTime = System.currentTimeMillis();
			preStatement.setDate(3, new Date(curTime));
			preStatement.setString(4, name);
			preStatement.executeUpdate();
			
			preStatement = connection.prepareStatement("UPDATE NHANVIEN SET SOTIENTHUONG = ? WHERE TAIKHOAN = ?");
			money += staff.getBonus();
			preStatement.setInt(1, money);
			preStatement.setString(2, staff.getUsername());
			preStatement.executeUpdate();
			preStatement.close();
			
			staff.setBonus(money);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addAdvanceMoney(Staff staff, int money, String name) {
		try {
			
			PreparedStatement preStatement = connection.prepareStatement("INSERT INTO TIENUNG VALUES (?, ?, ?, ?)");
			preStatement.setString(1, staff.getName());
			preStatement.setInt(2, money);
			long curTime = System.currentTimeMillis();
			preStatement.setDate(3, new Date(curTime));
			preStatement.setString(4, name);
			preStatement.executeUpdate();
			
			preStatement = connection.prepareStatement("UPDATE NHANVIEN SET SOTIENUNG = ? WHERE TAIKHOAN = ?");
			money += staff.getAdvanceMoney();
			preStatement.setInt(1, money);
			preStatement.setString(2, staff.getUsername());
			preStatement.executeUpdate();
			preStatement.close();
			
			staff.setAdvanceMoney(money);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editStaff(Staff staff, String password) {
		
		try {

			PreparedStatement preStatement = connection.prepareStatement("UPDATE NHANVIEN "
					+ "SET MATKHAU = ?  WHERE TAIKHOAN = ?");
			password = Sever.createMD5Password(password);
			preStatement.setString(1, password);
			preStatement.setString(2, staff.getUsername());
			
			preStatement.executeUpdate();
			preStatement.close();
			
			staff.setPassword(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editStaff(Staff staff, String name, String password, String phoneNumber, String position, int salary,
			boolean isTable, boolean isStorge, boolean isImport, boolean isExport, boolean isFood,
			boolean isResource, boolean isStaff, boolean isAdvanceMoney, boolean isBonus, boolean isPay,
			boolean isTime, boolean isEditStaff, boolean isAddStaff, boolean isReport, boolean isStatistic) {
		
		try {

			PreparedStatement preStatement = connection.prepareStatement("UPDATE NHANVIEN "
					+ "SET MATKHAU = ?, HOTEN = ?, SODIENTHOAI = ?, CHUCVU = ?, LUONG = ?,"
					+ "BANAN = ?, KHO = ?, NHAP = ?, XUAT = ?, MONAN = ?, THUCPHAM = ?,"
					+ "NHANVIEN = ?, UNGLUONG = ?, THUONGLUONG = ?, TRALUONG = ?, CHAMCONG2 = ?,"
					+ "SUA = ?, THEM = ?, BAOCAO = ?, THONGKE = ? WHERE TAIKHOAN = ?");
			password = Sever.createMD5Password(password);
			preStatement.setString(1, password);
			preStatement.setString(2, name);
			preStatement.setString(3, phoneNumber);
			preStatement.setString(4, position);
			preStatement.setInt(5, salary);
		
			preStatement.setBoolean(6, isTable );
			preStatement.setBoolean(7, isStorge );
			preStatement.setBoolean(8, isImport );
			preStatement.setBoolean(9, isExport );
			preStatement.setBoolean(10, isFood );
			preStatement.setBoolean(11, isResource );
			preStatement.setBoolean(12, isStaff );
			preStatement.setBoolean(13, isAdvanceMoney );
			preStatement.setBoolean(14, isBonus );
			preStatement.setBoolean(15, isPay );
			preStatement.setBoolean(16, isTime );
			preStatement.setBoolean(17, isEditStaff );
			preStatement.setBoolean(18, isAddStaff );
			preStatement.setBoolean(19, isReport );
			preStatement.setBoolean(20, isStatistic );
			
			preStatement.setString(21, staff.getUsername());
			
			preStatement.executeUpdate();
			preStatement.close();
			
			staff.setStaff(name, password, phoneNumber, position, salary,
					isTable, isStorge, isImport, isExport, isFood, isResource, isStaff, isAdvanceMoney, 
					isBonus, isPay, isTime, isEditStaff, isAddStaff, isReport, isStatistic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addStaff(String name, String username, String password, String phoneNumber, String position, int salary,
			boolean isTable, boolean isStorge, boolean isImport, boolean isExport, boolean isFood,
			boolean isResource, boolean isStaff, boolean isAdvanceMoney, boolean isBonus, boolean isPay,
			boolean isTime, boolean isEditStaff, boolean isAddStaff, boolean isReport, boolean isStatistic) {
		
		try {
			
			String sqlQuery = "INSERT INTO NHANVIEN VALUES "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
			PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
			password = Sever.createMD5Password(password);
			preStatement.setString(1, username);
			preStatement.setString(2, password);
			preStatement.setString(3, name);
			preStatement.setString(4, phoneNumber);
			preStatement.setString(5, position);
			preStatement.setInt(6, 0);
			preStatement.setInt(7, 0);
			preStatement.setInt(8, 0);
			preStatement.setInt(9, 0);
			preStatement.setBoolean(10, false);
			
			preStatement.setBoolean(11, isTable );
			preStatement.setBoolean(12, isStorge );
			preStatement.setBoolean(13, isImport );
			preStatement.setBoolean(14, isExport );
			preStatement.setBoolean(15, isFood );
			preStatement.setBoolean(16, isResource );
			preStatement.setBoolean(17, isStaff );
			preStatement.setBoolean(18, isAdvanceMoney );
			preStatement.setBoolean(19, isBonus );
			preStatement.setBoolean(20, isPay );
			preStatement.setBoolean(21, isTime );
			preStatement.setBoolean(22, isEditStaff );
			preStatement.setBoolean(23, isAddStaff );
			preStatement.setBoolean(24, isReport );
			preStatement.setBoolean(25, isStatistic );
			
			preStatement.executeUpdate();
			preStatement.close();
			
			Staff staff = new Staff(name, username, password, phoneNumber, position, salary, 0, 0, 0, false,
					isTable, isStorge, isImport, isExport, isFood, isResource, isStaff, isAdvanceMoney, 
					isBonus, isPay, isTime, isEditStaff, isAddStaff, isReport, isStatistic);
			staffs.add(staff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connectDatabase() {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM NHANVIEN");
			while(resultSet.next()) {
				
				String username = resultSet.getString(1);
				String password = resultSet.getString(2);
				String name = resultSet.getString(3);
				String phoneNumber = resultSet.getString(4);
				String position = resultSet.getString(5);
				
				int salary = resultSet.getInt(6);
				int timekeeping = resultSet.getInt(7);
				int bonus = resultSet.getInt(8);
				int advanceMoney = resultSet.getInt(9);
				boolean isTimekeeping = resultSet.getBoolean(10);
				
				boolean isTable = resultSet.getBoolean(11);
				boolean isStorge = resultSet.getBoolean(12);
				boolean isImport = resultSet.getBoolean(13);
				boolean isExport = resultSet.getBoolean(14);
				boolean isFood = resultSet.getBoolean(15);
				boolean isResource = resultSet.getBoolean(16);
				boolean isStaff = resultSet.getBoolean(17);
				boolean isAdvanceMoney = resultSet.getBoolean(18);
				boolean isBonus = resultSet.getBoolean(19);
				boolean isPay = resultSet.getBoolean(20);
				boolean isTime = resultSet.getBoolean(21);
				boolean isEditStaff = resultSet.getBoolean(22);
				boolean isAddStaff = resultSet.getBoolean(23);
				boolean isReport = resultSet.getBoolean(24);
				boolean isStatistic = resultSet.getBoolean(25);
				
				if (username.equals(Sever.staff.getUsername())) {
					staffs.add(Sever.staff);
				} else {
					Staff staff = new Staff(name, username, password, phoneNumber, position, salary, bonus, advanceMoney, timekeeping,
							isTimekeeping, isTable, isStorge, isImport, isExport, isFood, isResource, isStaff, isAdvanceMoney, 
							isBonus, isPay, isTime, isEditStaff, isAddStaff, isReport, isStatistic);
					staffs.add(staff);
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
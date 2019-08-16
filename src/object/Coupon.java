package object;

import java.sql.Date;
import java.util.ArrayList;

public class Coupon {
	
	private int id;
	private ArrayList<DetailCoupon> foods;
	private String name;
	private Date day;
	private String supplier;
	private boolean isImport;
	private int total = 0;
	
	public Coupon(int id, String supplier, String name, boolean isImport) {
		
		this.id = id;
		long curTime = System.currentTimeMillis();
		this.day = new Date(curTime);
		this.supplier = supplier;
		this.isImport = isImport;
		this.foods = new ArrayList<>();
		this.name = name;
	}
	
	public Coupon(int id, Date day, String name, String supplier, boolean isImport) {
		
		this.id = id;
		this.day = day;
		this.name = name;
		this.supplier = supplier;
		this.isImport = isImport;
		this.foods = new ArrayList<>();
	}
	
	public ArrayList<DetailCoupon> getFoods() {
		return this.foods;
	}
	
	public void addFood(Food food, int bigAmount, int smallAmount, int price, String note, int total) {
		
		DetailCoupon info = new DetailCoupon(food, bigAmount, smallAmount, price, note, total);
		foods.add(info);
		this.total += total;
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public boolean isImport() {
		return this.isImport;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Date getDay() {
		return this.day;
	}
	
	public String getSupplier() {
		return this.supplier;
	}
	
	@Override
	public String toString() {
		if (this.isImport)
			return "Nhập hàng";
		else
			return "Xuất hàng";
	}
}

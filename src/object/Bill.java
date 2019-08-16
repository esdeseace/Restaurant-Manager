package object;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;

public class Bill {
	
	private int id;
	private Time start;
	private Time end;
	private Date day;
	private String name;
	private HashMap<Food, DetailBill> foods;
	private int total;
	private int realTotal;
	private String nameOfTable;
	
	public Bill(int id, String name) {
		
		this.id = id;
		this.name = name;
		long curTime = System.currentTimeMillis();
		this.start = new Time(curTime);
		this.day = new Date(curTime);
		this.foods = new HashMap<>();
		this.total = 0;
	}
	
	public Bill(int id, Time start, Time end, Date day, String name, int realTotal, String nameOfTable) {
		
		this.id = id;
		this.start = start;
		this.end = end;
		this.day = day;
		this.name = name;
		this.realTotal = realTotal;
		this.nameOfTable = nameOfTable;
		this.foods = new HashMap<>();
		this.total = 0;
	}
	
	public int getRealTotal() {
		return this.realTotal;
	}
	
	public void setRealTotal(int realTotal) {
		this.realTotal = realTotal;
	}
	
	public int getAmountOfFood(Food food) {
		return (foods.get(food) != null) ? this.foods.get(food).getAmount() : 0;
	}
	
	public boolean isHaveFood(Food food) {
		if (foods.get(food) == null)
			return false;
		return true;
	}
	
	public void pay(String nameOfTable) {
		long curTime = System.currentTimeMillis();
		this.end = new Time(curTime);
		this.nameOfTable = nameOfTable;
	}
	
	public void addFood(Food food, DetailBill info) {
		foods.put(food, info);
		total += info.getTotal();
	}
	
	public void addFood(Food food, int amount) {
		if (foods.get(food) == null) {
			DetailBill info = new DetailBill(amount, food.getPrice());
			foods.put(food, info);
			total += info.getTotal();
		}
	}
	
	public void setFood(Food food, int amount) {
		DetailBill info = foods.get(food);
		if (info != null) {
			total -= info.getTotal();
			info.setAmount(amount);
			total += info.getTotal();
		}
	}
	
	public void deleteFood(Food food) {
		DetailBill info = foods.get(food);
		total = total - info.getTotal();
		foods.remove(food);
	}
	
	public String getNameOfTable() {
		return this.nameOfTable;
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public String getName() {
		return this.name;
	}
	
	public HashMap<Food, DetailBill> getFoods() {
		return this.foods;
	}

	public int getId() {
		return this.id;
	}
	
	public Time getStart() {
		return this.start;
	}
	
	public Time getEnd() {
		return this.end;
	}
	
	public Date getDay() {
		return this.day;
	}
	
	@Override
	public String toString() {
		return this.nameOfTable;
	}
	
}

package object;

import java.util.Calendar;

public class FullStatistic {

	private Calendar fromCalendar;
	private Calendar toCalendar;
	
	private int realTotal = 0;
	private int importTotal = 0;
	private int exportTotal = 0;
	private int advancedMoney = 0;
	private int bonus = 0;
	private int salary = 0;
	private int revenue = 0;
	private int cost = 0;
	
	public FullStatistic() {
		
	}
	
	public FullStatistic(Calendar fromCalendar, Calendar toCalendar) {
		this.fromCalendar = Calendar.getInstance();
		this.fromCalendar.setTime(fromCalendar.getTime());
		this.toCalendar = Calendar.getInstance();
		this.toCalendar.setTime(toCalendar.getTime());
	}
	
	public Calendar getFromCalendar() {
		return this.fromCalendar;
	}
	
	public Calendar getToCalendar() {
		return this.toCalendar;
	}
	
	public void setRealTotal(int realTotal) {
		this.realTotal = realTotal;
	}
	
	public void setImportTotal(int importTotal) {
		this.importTotal = importTotal;
	}
	
	public void setExportTotal(int exportTotal) {
		this.exportTotal = exportTotal;
	}
	
	public void setAdvancedMoney(int advancedMoney) {
		this.advancedMoney = advancedMoney;
	}
	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}

	public void addRevenue(int revenue) {
		this.revenue += revenue;
	}
	
	public void addCost(int cost) {
		this.cost += cost;
	}
	
	public int getRevenue() {
		return this.revenue;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public int getRealTotal() {
		return this.realTotal;
	}
	
	public int getImportTotal() {
		return this.importTotal;
	}
	
	public int getExportTotal() {
		return this.exportTotal;
	}
	
	public int getAdvancedMoney() {
		return this.advancedMoney;
	}
	
	public int getBonus() {
		return this.bonus;
	}
	
	public int getSalary() {
		return this.salary;
	}
	
}

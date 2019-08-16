package object;

public class Staff {

	private String username;
	private String password;
	private String name;
	private String phoneNumber;
	private String position;
	private int salary;
	private int bonus;
	private int advanceMoney;
	private int timekeeping;
	private boolean isTimekeeping;
	
	public boolean isTable;
	public boolean isStorge;
	public boolean isImport;
	public boolean isExport;
	public boolean isFood;
	public boolean isResource;
	public boolean isStaff;
	public boolean isAdvanceMoney;
	public boolean isBonus;
	public boolean isPay;
	public boolean isTime;
	public boolean isEditStaff;
	public boolean isAddStaff;
	public boolean isReport;
	public boolean isStatistic;
	
	public Staff(String name, String username, String password, String phoneNumber, String position, 
			int salary, int bonus, int advanceMoney, int timekeeping, boolean isTimekeeping,
			boolean isTable, boolean isStorge, boolean isImport, boolean isExport, boolean isFood,
			boolean isResource, boolean isStaff, boolean isAdvanceMoney, boolean isBonus, boolean isPay,
			boolean isTime, boolean isEditStaff, boolean isAddStaff, boolean isReport, boolean isStatistic) {
		
		this.name = name;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.position = position;
		this.salary = salary;
		this.bonus = bonus;
		this.advanceMoney = advanceMoney;
		this.timekeeping = timekeeping;
		this.isTimekeeping = isTimekeeping;
	
		this.isTable = isTable;
		this.isStorge = isStorge;
		this.isImport = isImport;
		this.isExport = isExport;
		this.isFood = isFood;
		this.isResource = isResource;
		this.isStaff = isStaff;
		this.isAdvanceMoney = isAdvanceMoney;
		this.isBonus = isBonus;
		this.isPay = isPay;
		this.isTime = isTime;
		this.isEditStaff = isEditStaff;
		this.isAddStaff = isAddStaff;
		this.isReport = isReport;
		this.isStatistic = isStatistic;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setStaff(String name, String password, String phoneNumber, String position, int salary,
			boolean isTable, boolean isStorge, boolean isImport, boolean isExport, boolean isFood,
			boolean isResource, boolean isStaff, boolean isAdvanceMoney, boolean isBonus, boolean isPay,
			boolean isTime, boolean isEditStaff, boolean isAddStaff, boolean isReport, boolean isStatistic) {
		
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.position = position;
		this.salary = salary;
		
		this.isTable = isTable;
		this.isStorge = isStorge;
		this.isImport = isImport;
		this.isExport = isExport;
		this.isFood = isFood;
		this.isResource = isResource;
		this.isStaff = isStaff;
		this.isAdvanceMoney = isAdvanceMoney;
		this.isBonus = isBonus;
		this.isPay = isPay;
		this.isTime = isTime;
		this.isEditStaff = isEditStaff;
		this.isAddStaff = isAddStaff;
		this.isReport = isReport;
		this.isStatistic = isStatistic;
	}
	
	public int getRealSalary() {
		return salary + bonus - advanceMoney;
	}
	
	public void payWage(int timekeeping) {
		this.timekeeping -= timekeeping;
		this.advanceMoney = 0;
		this.bonus = 0;
	}
	
	public boolean isTimekeeping() {
		return this.isTimekeeping;
	}
	
	public void newday() {
		this.isTimekeeping = false;
	}
	
	public void timekeeping() {
		this.timekeeping++;
		this.isTimekeeping = true;
	}
	
	public int getTimekeeping() {
		return this.timekeeping;
	}
	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	public void setAdvanceMoney(int advanceMoney) {
		this.advanceMoney = advanceMoney;
	}
	
	public int getSalary() {
		return this.salary;
	}
	
	public int getBonus() {
		return this.bonus;
	}
	
	public int getAdvanceMoney() {
		return this.advanceMoney;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	@Override
	public String toString() {
		return this.username;
	}
}

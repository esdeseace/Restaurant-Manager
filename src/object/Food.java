package object;

public class Food {
	
	private boolean isResource;
	private int id;
	private String name;
	private String bigUnit;
	private String smallUnit;
	private String kindOfFood;
	private int price = 0;
	private int bigAmount = 0;
	private int smallAmount = 0;
	private int convert = 0;
	
	public Food(int id, String name, int price, String smallUnit, String kindOfFood) {
		
		this.isResource = false;
		this.id = id;
		this.name = name;
		this.price = price;
		this.smallUnit = smallUnit;
		this.kindOfFood = kindOfFood;
	}
	
	public Food(int id, String name,String bigUnit, String smallUnit, int price, 
			int bigAmount, int smallAmount, int convert, String kindOfFood) {
		
		this.isResource = true;
		this.id = id;
		this.name = name;
		this.bigUnit = bigUnit;
		this.smallUnit = smallUnit;
		this.price = price;
		this.bigAmount = bigAmount;
		this.smallAmount = smallAmount;
		this.convert = convert;
		this.kindOfFood = kindOfFood;
	}
	
	public void setFood(String name,String bigUnit, String smallUnit, int price, int convert, String kindOfFood) {
		this.name = name;
		this.bigUnit = bigUnit;
		this.smallUnit = smallUnit;
		this.price = price;
		this.convert = convert;
		this.kindOfFood = kindOfFood;
	}
	
	public void setFood(String name, String smallUnit, int price, String kindOfFood) {
		this.name = name;
		this.smallUnit = smallUnit;
		this.price = price;
		this.kindOfFood = kindOfFood;
	}
	
	public void setAmount(int smallAmount) {
		this.smallAmount = smallAmount;
	}
	
	public int getAmount() {
		return bigAmount * convert + smallAmount;
	}
	
	public void setAmount(int bigAmount, int smallAmount) {
		this.bigAmount = bigAmount;
		this.smallAmount = smallAmount;
	}
	
	public boolean isResource() {
		return this.isResource;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBigUnit() {
		return this.bigUnit;
	}
	
	public String getSmallUnit() {
		return this.smallUnit;
	}
	
	public String getKindOfFood() {
		return this.kindOfFood;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getBigAmount() {
		return this.bigAmount;
	}
	
	public int getSmallAmount() {
		return this.smallAmount;
	}
	
	public int getConvert() {
		return this.convert;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}

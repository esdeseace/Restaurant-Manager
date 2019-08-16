package object;

public class DinnerTable {
	
	private int id;
	private String name;
	private int floor;
	private int idOfBill;
	
	public DinnerTable(int id, String name,int floor, int idOfBill) {
		this.id = id;
		this.name = name;
		this.floor = floor;
		this.idOfBill = idOfBill;
	}
	
	public void setIdOfBill(int idOfBill) {
		this.idOfBill = idOfBill;
	}

	public int getId() {
		return this.id;
	}
	
	public int getIdOfBill() {
		return this.idOfBill;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getFloor() {
		return this.floor;
	}

}

package object;

public class DetailBill {

	private int amount;
	private int price;
	
	public DetailBill(DetailBill info) {
		this.amount = info.getAmount();
		this.price = info.getPrice();
	}
	
	public DetailBill(int amount, int price) {
		this.amount = amount;
		this.price = price;
	}
	
	public int getTotal() {
		return this.price * this.amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public int getPrice() {
		return this.price;
	}
}

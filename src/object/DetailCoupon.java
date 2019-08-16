package object;

public class DetailCoupon {
	
	private Food food;
	private String note;
	private int bigNumber;
	private int smallNumber;
	private int price;
	private int total;
	
	public DetailCoupon(DetailCoupon detailCoupon) {
		this.bigNumber = detailCoupon.getBigNumber();
		this.smallNumber = detailCoupon.getSmallNumber();
		this.total = detailCoupon.getTotal();
	}
	
	public DetailCoupon(Food food, int bigNumber, int smallNumber, int price, String note, int total) {
		this.food = food;
		this.bigNumber = bigNumber;
		this.smallNumber = smallNumber;
		this.price = price;
		this.note = note;
		this.total = total;
	}
	
	public void addDetailCoupon(DetailCoupon detailCoupon) {
		this.bigNumber += detailCoupon.getBigNumber();
		this.smallNumber += detailCoupon.getSmallNumber();
		this.total += detailCoupon.getTotal();
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public String getNote() {
		return this.note;
	}
	
	public Food getFood() {
		return this.food;
	}
	
	public int getSmallNumber() {
		return this.smallNumber;
	}
	
	public int getBigNumber() {
		return this.bigNumber;
	}
	
	public int getPrice() {
		return this.price;
	}
}

package it.polito.ezshop.data;

public class TicketEntryImpl implements TicketEntry {
	private String barCode;
	private String productDescription;
	private int amount = 0;
	private double pricePerUnit = 0;
	private double discountRate=0 ;

	public TicketEntryImpl(String barCode, String producString, int amount, double pricePerUnit, double discountRate) {
		this.barCode = barCode;
		this.productDescription = producString;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.discountRate = discountRate;
	}

	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public String getProductDescription() {
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;

	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

}

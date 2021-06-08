package it.polito.ezshop.data;

import java.util.ArrayList;

public class TicketEntryImpl implements TicketEntry {
	private String barCode;
	private String productDescription;
	private int amount = 0;
	private double pricePerUnit = 0;
	private double discountRate = 0;
	private ArrayList<String> RFID = new ArrayList<String>();



	public TicketEntryImpl(String barCode, String productString, int amount, double pricePerUnit,
			double discountRate) {
		this.barCode = barCode;
		this.productDescription = productString;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.discountRate = discountRate;
	}

	public TicketEntryImpl(String barCode, String productString, int amount, double pricePerUnit,
			String RFID) {
		this.barCode = barCode;
		this.productDescription = productString;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.RFID.add(RFID);
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

	public ArrayList<String> getRFID() {
		return RFID;
	}

	public void addRFID(String RFID) {
		this.RFID.add(RFID);
	}
}

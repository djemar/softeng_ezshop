package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.List;

public class SaleTransactionImpl implements SaleTransaction {

	Integer transactionID;
	ArrayList<TicketEntry> ticketsList = new ArrayList<TicketEntry>();

	private double discountRate = 0;
	private double price;
	// (open, closed, payed)
	private String status = "open";

	public SaleTransactionImpl(Integer transactionID, double discountRate, double price) {
		this.transactionID = transactionID;
		this.discountRate = discountRate;
		this.price = price;
	}

	public SaleTransactionImpl(Integer transactionID, double discountRate, double price,
			String status) {
		this.transactionID = transactionID;
		this.discountRate = discountRate;
		this.price = price;
		this.status = status;
	}

	public SaleTransactionImpl(int i) {
		this.transactionID = i;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Integer getTicketNumber() {

		return transactionID;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {

		this.transactionID = ticketNumber;

	}

	@Override
	public List<TicketEntry> getEntries() {

		return this.ticketsList;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {

		entries.forEach((a) -> this.ticketsList.add(a));

	}

	@Override
	public double getDiscountRate() {

		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {

		this.discountRate = discountRate;
	}

	@Override
	public double getPrice() {
		return this.price;
	}


	public void estimatePrice() {
		if (this.ticketsList.isEmpty())
			this.setPrice(0);
		else {
			this.price = ticketsList.stream()
					.mapToDouble(
							a -> a.getPricePerUnit() * a.getAmount() * (1 - a.getDiscountRate()))
					.sum() * (1 - this.discountRate);
		}
	}

	@Override
	public void setPrice(double price) {

		this.price = price;
	}

}

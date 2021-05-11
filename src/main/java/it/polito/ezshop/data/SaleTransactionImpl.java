package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.List;

public class SaleTransactionImpl implements SaleTransaction {

	Integer transactionID;
	ArrayList<TicketEntry> ticketsList = new ArrayList<TicketEntry>();;
	private double discountRate;
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

	@Override
	public void setPrice(double price) {

		this.price = price;
	}

}

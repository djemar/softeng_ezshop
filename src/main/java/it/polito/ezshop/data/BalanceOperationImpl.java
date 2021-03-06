package it.polito.ezshop.data;

import java.time.LocalDate;

public class BalanceOperationImpl implements BalanceOperation {
	private int balanceId;
	private LocalDate date;
	private double money;
	private String type;

	public BalanceOperationImpl(int balanceId, LocalDate date, double money, String type) {
		this.balanceId = balanceId;
		this.date = date;
		this.money = money;
		this.type = type;
	}


	public BalanceOperationImpl(LocalDate date, double money, String type) {
		this.date = date;
		this.money = money;
		this.type = type;
	}

	@Override
	public int getBalanceId() {
		return balanceId;
	}

	@Override
	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}

	@Override
	public LocalDate getDate() {
		return date;
	}

	@Override
	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public double getMoney() {
		return money;
	}

	@Override
	public void setMoney(double money) {
		this.money = money;

	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;

	}

}

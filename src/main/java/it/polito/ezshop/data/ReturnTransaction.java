package it.polito.ezshop.data;

import java.util.HashMap;

public class ReturnTransaction {

	private Integer returnId;
	private Integer transactionId;
	private String productCode;
	private String status;
	private int amount;
	private double total = 0;

	private HashMap<String, Integer> returnedProductsMap = new HashMap<String, Integer>();

	public ReturnTransaction(Integer returnId, String productCode, int amount) {
		// this should be a useless constructor
		this.returnId = returnId;
		this.productCode = productCode;
		this.amount = amount;
	}

	public ReturnTransaction(Integer returnId, Integer transactionId) {
		this.returnId = returnId;
		this.transactionId = transactionId;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public HashMap<String, Integer> getReturnedProductsMap() {
		return returnedProductsMap;
	}

	public void addProductToReturn(String productCode, int amount) {
		this.returnedProductsMap.put(productCode, amount);
	}

	public void setReturnedProductsMap(HashMap<String, Integer> returnedProductsMap) {
		this.returnedProductsMap = returnedProductsMap;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void updateTotal(double money) {
		this.total += money;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}

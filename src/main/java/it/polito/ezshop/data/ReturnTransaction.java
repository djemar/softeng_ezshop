package it.polito.ezshop.data;

import java.util.HashMap;
import java.util.LinkedList;

public class ReturnTransaction {

	private Integer returnId;
	private Integer transactionId;
	private String status;
	private double total = 0;

	private HashMap<String, Integer> returnedProductsMap = new HashMap<String, Integer>();
	private LinkedList<String> returnedProductsbyRFID = new LinkedList<String>();


	public ReturnTransaction(Integer returnId, Integer transactionId) {
		this.returnId = returnId;
		this.transactionId = transactionId;
		this.status = "OPEN";
	}

	public ReturnTransaction(Integer returnId, Integer transactionId, String status, double total) {
		this.returnId = returnId;
		this.transactionId = transactionId;
		this.status = status;
		this.total = total;
	}
	
	public LinkedList<String> getReturnedProductsbyRFID() {
		return returnedProductsbyRFID;
	}

	public void setReturnedProductsMapbyRFID(String RFID) {
		this.returnedProductsbyRFID.add(RFID);
	}
	
	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
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

package it.polito.ezshop.data;

public class ReturnTransaction{

	private Integer returnId;
	private String productCode;
	private int amount;

	public ReturnTransaction(Integer returnId, String productCode, int amount) {
		this.returnId=returnId;
		this.productCode=productCode;
		this.amount=amount;
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

}

package it.polito.ezshop.data;

public class Product {
	private String RFID;
	private String ProductCode;
	private Double price;
	public Product(String RFID, String ProductCode, Double price) {
		this.RFID = RFID;
		this.ProductCode = ProductCode;
		this.price = price;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getRFID() {
		return RFID;
	}
	public void setRFID(String rFID) {
		RFID = rFID;
	}
	public String getProductCode() {
		return ProductCode;
	}
	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}
}

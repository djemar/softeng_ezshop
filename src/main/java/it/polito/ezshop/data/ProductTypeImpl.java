package it.polito.ezshop.data;

public class ProductTypeImpl implements ProductType {
	private String description;
	private String productCode;
	private double pricePerUnit;
	private String note;
	private String location;
	private Integer quantity;
	private Integer id;

	public ProductTypeImpl(String description, String productCode, double pricePerUnit, String note) {
		this.description = description;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.note = note;
	}
	public ProductTypeImpl(Integer id, String description, String productCode, double pricePerUnit, String note, String location, Integer quantity) {
		this.description = description;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.note = note;
		this.id = id;
		this.location = location;
		this.quantity = quantity;
	}

	@Override
	public Integer getQuantity() {

		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {

		this.quantity = quantity;

	}

	@Override
	public String getLocation() {

		return location;
	}

	@Override
	public void setLocation(String location) {

		this.location = location;

	}

	@Override
	public String getNote() {

		return this.note;
	}

	@Override
	public void setNote(String note) {

		this.note = note;
	}

	@Override
	public String getProductDescription() {

		return this.description;
	}

	@Override
	public void setProductDescription(String productDescription) {

		this.description = productDescription;

	}

	@Override
	public String getBarCode() {

		return this.productCode;
	}

	@Override
	public void setBarCode(String barCode) {

		this.productCode = barCode;

	}

	@Override
	public Double getPricePerUnit() {

		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {

		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public Integer getId() {

		return this.id;
	}

	@Override
	public void setId(Integer id) {

		this.id = id;
	}

}

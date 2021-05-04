package it.polito.ezshop.data;

public class CustomerImpl implements Customer {
    private String customerName;
    private Integer id;
    private String customerCard;
    private Integer points;

    public CustomerImpl(String customerName, Integer id) {
        this.customerName = customerName;
        this.id = id;
    }

    @Override
    public String getCustomerName() {

        return this.customerName;
    }

    @Override
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }

    @Override
    public String getCustomerCard() {

        return this.customerCard;
    }

    @Override
    public void setCustomerCard(String customerCard) {

        this.customerCard = customerCard;
    }

    @Override
    public Integer getId() {

        return this.id;
    }

    @Override
    public void setId(Integer id) {

        this.id = id;
    }

    @Override
    public Integer getPoints() {

        return this.points;
    }

    @Override
    public void setPoints(Integer points) {

        this.points = points;
    }

}

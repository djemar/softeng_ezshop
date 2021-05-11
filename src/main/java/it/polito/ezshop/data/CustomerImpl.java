package it.polito.ezshop.data;

public class CustomerImpl implements Customer {
    private String customerName;
    private Integer id;
    private String customerCard;
    Integer points;

    public CustomerImpl(String customerName) {
        this.customerName = customerName;
        
    }

    public CustomerImpl(String string, int int1, String string2) {
        
    }

    public CustomerImpl(int id, String name, String card, int points) {
        this.id=id;
        this.customerName=name;
        this.customerCard=card;
        this.points=points;
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

package it.polito.ezshop.data;

public class CustomerCard {
   
    String card;
    int customer=-1;
    int points=0;
    public CustomerCard(String customerCard) {
        this.card=customerCard;
    }
    public CustomerCard(String customerCard,int customer) {
        this.card=customerCard;
        this.customer=customer;
    }
    public CustomerCard(String customerCard,int customer,int points) {
        this.card=customerCard;
        this.customer=customer;
        this.points=points;
    }
    public String getCard() {
        return card;
    }
    public int getPoints() {
        return points;
    }
    public Integer getCustomerId() {
        return customer;
    }
}

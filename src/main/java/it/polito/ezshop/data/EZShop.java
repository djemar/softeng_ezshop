package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.List;


public class EZShop implements EZShopInterface {
    EZShopDb db= new EZShopDb();
    UserImpl currentUser=new UserImpl("michele", "Soldi", "Administrator", 3);
    public void setCurrentUser(UserImpl user){
        this.currentUser=user;
    }
    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        return null;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return null;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        return null;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        ProductTypeImpl p=null;
        int i=-1;
        try{ 
            
            if(productCode==null || productCode=="")
                throw new InvalidProductCodeException ("Invalid product Code ") ;
            

            if(quantity<=0)
                throw new InvalidQuantityException("Invalid Quantity");
            if(pricePerUnit<=0)
                throw new InvalidPricePerUnitException("Invalid price per unit");   
            if(currentUser==null || currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager"))
                throw new UnauthorizedException("Unauthorized user");


            ProductTypeImpl prod=db.getProductTypeByBarCode(productCode);    
            if(prod!=null && db.getBalance()>pricePerUnit*quantity){

                int orderId=db.getOrderNumber()+1;
                int bn=db.getBalnceOperationsNumber()+1;
                

                BalanceOperationImpl b= new BalanceOperationImpl(bn,LocalDate.now(), quantity*pricePerUnit, "ORDER");
                OrderImpl o=new OrderImpl(orderId+1, productCode, pricePerUnit, quantity,"PAYED",bn);
                db.insertOrder(o);
                db.insertBalanceOperation(b);
                i=orderId;
            } 
        } catch ( InvalidProductCodeException e){

            System.err.println(e.getMessage());
        } catch(InvalidQuantityException e){
            System.err.println(e.getMessage());
        } catch (InvalidPricePerUnitException e){
            System.err.println(e.getMessage());
        } catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        
        return i;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        boolean b=false;
        try{
            if(orderId<=0 || orderId==null)
                throw new InvalidOrderIdException("Invalid order id");
            
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")))
                throw new UnauthorizedException("Unauthorized user");
            OrderImpl o= db.getOrder(orderId);
            if(o!=null && (o.getStatus()=="ISSUED" || o.getStatus()=="ORDERED")){
                int bn=db.getBalnceOperationsNumber()+1;
                

                BalanceOperationImpl bo= new BalanceOperationImpl(bn,LocalDate.now(), o.getQuantity()*o.getPricePerUnit(), "ORDER");
                db.insertBalanceOperation(bo);
                db.updateOrder(o.getOrderId(), o.getProductCode(),o.getPricePerUnit(), o.getQuantity(), "PAYED", bo.getBalanceId());
                b=true;
            }

            
        }catch(InvalidOrderIdException e){
            System.err.println(e.getMessage());
        }catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return b;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        boolean b=false;
        try{
            if(orderId<=0 || orderId==null)
                throw new InvalidOrderIdException("Invalid order id");
            
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")))
                throw new UnauthorizedException("Unauthorized user");
                OrderImpl o= db.getOrder(orderId);
            if(o!=null && (o.getStatus()=="PAYED" ))   {
                if( o.getStatus()=="COMPLETED")
                    return true;

                ProductTypeImpl prod = db.getProductTypeByBarCode(o.getProductCode());
                if(prod==null ||prod.getLocation()!=null)
                    throw new InvalidLocationException("Invalid Location");
                db.updateOrder(o.getOrderId(), o.getProductCode(),o.getPricePerUnit(), o.getQuantity(), "COMPLETED", o.getBalanceId());    
                db.updateProductType(prod.getId(), prod.getProductDescription(), prod.getBarCode(), prod.getPricePerUnit(), prod.getNote());
                b=true;
            } 
           
        }catch(InvalidOrderIdException e){
            System.err.println(e.getMessage());
        }catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return b;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        List <Order> list =null;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")))
                throw new UnauthorizedException("Unauthorized user");
            list= db.getAllOrders();
        }catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return list;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        int i=-1;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if(customerName==null || customerName=="")
                throw new InvalidCustomerNameException();
            i=db.getCustomerNumber()+1;
            db.insertCustomer(customerName,i,"",0);
            
        }catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }catch (InvalidCustomerNameException e ){
            System.err.println(e.getMessage());
        }
    


        return i;
    }
    public static boolean onlyDigits(String str, int n)
    {
  
        // Traverse the string from
        // start to end
        for (int i = 0; i < n; i++) {
  
            // Check if the sepecified
            // character is a digit then
            // return true,
            // else return false
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        boolean b=false;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if(newCustomerName==null || newCustomerName=="")
                throw new InvalidCustomerNameException();

            if(newCustomerCard == null||newCustomerCard==""||newCustomerCard.length()!=10||onlyDigits(newCustomerCard, newCustomerCard.length()))
                throw new InvalidCustomerCardException("Invalid customer card");
            this.attachCardToCustomer(newCustomerCard, id);  
        }catch (InvalidCustomerNameException e ){
            System.err.println(e.getMessage());
        }catch(InvalidCustomerCardException e){
            System.err.println(e.getMessage());
        }
        return b;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        Customer c=null;
        try{
        if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
        if(id == null || id<=0)
            throw new InvalidCustomerCardException("Invalid customer id");
        c=db.getCustomer(id);
        }catch(InvalidCustomerCardException e){
            System.err.println(e.getMessage());
        } catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return c;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        List<Customer> l= null;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")||currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                    throw new UnauthorizedException("Unauthorized user");
            l=db.getAllCustomer();    
        }catch(UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return l;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        String c = null;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")|| currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");

            int n=db.getCustomerCardNumber()+1;
            String ns=Integer.toString(n);
            String customerCard=ns;
            for(int i=0;10-ns.length()>i;i++){
                customerCard+="0";
            }
            db.insertCustomerCard(customerCard);
            c=customerCard;
        } catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return c;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        boolean b=false;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")|| currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if(customerCard == null||customerCard==""||customerCard.length()!=10||!onlyDigits(customerCard,customerCard.length()))
                throw new InvalidCustomerCardException("Invalid customer card");    
            
           
            b=db.attachCardToCustomer(customerCard,customerId);
            
        } catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return b;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        boolean b=false;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")|| currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if(customerCard == null||customerCard==""||customerCard.length()!=10||!onlyDigits(customerCard,customerCard.length()))
                throw new InvalidCustomerCardException("Invalid customer card");    
            CustomerCard c= db.getCustomerCard(customerCard);
            if(c!=null && pointsToBeAdded+c.getPoints()<0){
                int points=c.getPoints()+pointsToBeAdded;
                db.updateCard(c.getCustomerId(), customerCard, points);
                b=true;
            }
        } catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        } catch (InvalidCustomerCardException e){
            System.err.println(e.getMessage());
        }    
        
        return true;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        int i=-1;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")|| currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            i=db.getSaleTransactionNumber()+1;
            db.insertSaleTransaction(i);
        } catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        }
        return i;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        boolean b=false;
        try{
            if(currentUser==null || !(currentUser.getRole().equals("Administrator")|| currentUser.getRole().equals("ShopManager")||currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if(transactionId==null || transactionId<=0)
                throw new InvalidTransactionIdException("Invalid transaction ID");
            ProductTypeImpl p=db.getProductTypeByBarCode(productCode);
            SaleTransactionImpl s=db.getSaleTransaction(transactionId);
            if(p!=null && s!= null){
                
                b=true;
            }
        }catch (UnauthorizedException e){
            System.err.println(e.getMessage());
        }catch (InvalidTransactionIdException e){
            System.err.println(e.getMessage());
        }
        return b;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}

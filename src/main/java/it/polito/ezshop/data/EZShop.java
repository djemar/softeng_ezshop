package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import java.time.LocalDate;
import java.util.List;



public class EZShop implements EZShopInterface {
	public User loggedUser = null;
    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        Integer id = -1;
        if( username == null || username.isEmpty())
        	throw new InvalidUsernameException();
        if(password == null || password.isEmpty())
        	throw new InvalidPasswordException();
        if(role == null || role.isEmpty() || (role.compareToIgnoreCase("Administrator")!=0 && role.compareToIgnoreCase("Cashier")!=0 && role.compareToIgnoreCase("ShopManager")!=0))
        	throw new InvalidRoleException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(!ezshopDb.getUserbyName(username)) {
        	//correggere per id
        	 id = ezshopDb.getUserId();
        	 ezshopDb.insertUser(new UserImpl(username, password, role, id));
        }
        ezshopDb.closeConnection();
        return id;
        
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	boolean done = false;
    	if(this.loggedUser == null || this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0)
    		throw new UnauthorizedException();
        if(id == null || id <= 0)
        	throw new InvalidUserIdException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(ezshopDb.getUser(id) != null) {
        	ezshopDb.deleteUser(id);
        	done = true;
        }
         ezshopDb.closeConnection();
         return done;
        
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	if(this.loggedUser == null || this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0)
    		throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        List<User> users = ezshopDb.getAllUsers();
        ezshopDb.closeConnection();
        return users;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if(id == null || id <= 0)
        	throw new InvalidUserIdException();
        if(this.loggedUser == null || this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0)
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        User u = ezshopDb.getUser(id);
        ezshopDb.closeConnection();
        return u;
   }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	boolean done = false;
        if(id == null || id <= 0)
        	throw new InvalidUserIdException();
        if(role == null || role.isEmpty() || (role.compareToIgnoreCase("Administrator")!=0 && role.compareToIgnoreCase("Cashier")!=0 && role.compareToIgnoreCase("ShopManager")!=0))
        	throw new InvalidRoleException();
        if(this.loggedUser == null || this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0)
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(ezshopDb.getUser(id) != null) {
        	ezshopDb.updateUserRights(id, role);
        	done = true;
        }
        ezshopDb.closeConnection();
        return done;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if(username == null || username.isEmpty())
        	throw new InvalidUsernameException();
        if(password == null || password.isEmpty())
        	throw new InvalidPasswordException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        this.loggedUser = ezshopDb.checkCredentials(username, password);
        ezshopDb.closeConnection();
        return loggedUser;
    }

    @Override
    public boolean logout() {
    	boolean logged = false;
    	if(this.loggedUser != null) {
    		this.loggedUser = null;
    		logged = true;
    	}
        return logged;
    }

    public static boolean isValid(String code) {
    	if(code.length() == 13) {
	    	int sum = 0;
	    	for(int i = 0; i < 12; i++) {
	    		if((i + 1) % 2 ==0)
	    			sum += Character.getNumericValue(code.charAt(i)) * 3;
	    		else
	    			sum += Character.getNumericValue(code.charAt(i));
	    	}
	    	if(Math.round((sum+5)/10.0) * 10- sum == Character.getNumericValue(code.charAt(12)))
	    		return true;
    	}
    	
    	return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(description == null || description.isEmpty())
        	throw new InvalidProductDescriptionException();
        if(pricePerUnit <= 0)
        	throw new  InvalidPricePerUnitException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        if(productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?") || !isValid(productCode))
        	throw new InvalidProductCodeException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        Integer id = -1;
        if(ezshopDb.getProductTypeByBarCode(productCode) == null) {
        	//correggere per id
        	 id = ezshopDb.getProductId();
        	 if (note == null)
        		 note = new String("");
        	 ezshopDb.insertProductType(new ProductTypeImpl(id, description, productCode, pricePerUnit, note));
        }
        ezshopDb.closeConnection();
        return id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        boolean done = false;
    	if(id == null || id <= 0)
        	throw new InvalidProductIdException();
    	if( newDescription == null || newDescription.isEmpty())
        	throw new InvalidProductDescriptionException();
        if(newPrice <= 0)
        	throw new  InvalidPricePerUnitException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        if(newCode == null || newCode.isEmpty() || !newCode.matches("-?\\d+(\\.\\d+)?") || !isValid(newCode))
        	throw new InvalidProductCodeException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(ezshopDb.getProductTypeByBarCode(newCode) == null && ezshopDb.getProductTypeById(id) != null) {
        	ezshopDb.updateProductType(id, newDescription, newCode, newPrice, newNote);
        	done = true;
        }
        ezshopDb.closeConnection();
        return done;
    }
    
    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        boolean done = false;
    	if(id == null || id <= 0)
        	throw new InvalidProductIdException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(ezshopDb.getProductTypeById(id) != null) {
        	ezshopDb.deleteProductType(id);
        	done=true;
        }
    	ezshopDb.closeConnection();
    	return done;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0 && this.loggedUser.getRole().compareToIgnoreCase("Cashier")!=0 ))
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        List<ProductType> products= ezshopDb.getAllProductTypes();
       	ezshopDb.closeConnection();
        return products;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        if(barCode == null || barCode.isEmpty() || !barCode.matches("-?\\d+(\\.\\d+)?") || !isValid(barCode))
        	throw new InvalidProductCodeException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        ProductType p = ezshopDb.getProductTypeByBarCode(barCode);
       	ezshopDb.closeConnection();
        return p;
   }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        if(description == null)
        	description = new String("");
        List<ProductType> products= ezshopDb.getProductTypesByDescription(description);
       	ezshopDb.closeConnection();
       	return products;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        boolean done = false;
    	if(productId == null || productId <= 0)
        	throw new InvalidProductIdException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        ProductType p = ezshopDb.getProductTypeById(productId);
        if(!p.getLocation().isEmpty() && ezshopDb.getProductTypeById(productId) != null && !ezshopDb.updateQuantity(productId, toBeAdded))
        	done = true;
       	ezshopDb.closeConnection();
       	return done;
    }

    @Override
    /*check condition!*/
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	boolean done = false;
    	if(productId == null || productId <= 0)
        	throw new InvalidProductIdException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
        String n[] = newPos.split("-");
        if((newPos != null && !n[0].isEmpty() && !n[2].isEmpty() && !n[1].isEmpty()) && (!n[0].matches("-?\\d+(\\.\\d+)?") || !n[2].matches("-?\\d+(\\.\\d+)?"))) //n[0] and n[2] are integers
        	throw new InvalidLocationException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        /*questo controllo non va*/
        if(newPos == null || (n[0].isEmpty() && n[2].isEmpty() && n[1].isEmpty()))
        	newPos = new String("");
        if(ezshopDb.getProductTypeById(productId) != null && !ezshopDb.checkExistingPosition(newPos)) {
        	ezshopDb.updatePosition(productId, newPos);
        	done = true;
        }
        ezshopDb.closeConnection();
        return done;
    }

    @Override
    /**
     * This method issues an order of <quantity> units of product with given <productCode>, each unit will be payed
     * <pricePerUnit> to the supplier. <pricePerUnit> can differ from the re-selling price of the same product. The
     * product might have no location assigned in this step.
     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
     *
     * @param productCode the code of the product that we should order as soon as possible
     * @param quantity the quantity of product that we should order
     * @param pricePerUnit the price to correspond to the supplier (!= than the resale price of the shop) per unit of
     *                     product
     *
     * @return  the id of the order (> 0)
     *          -1 if the product does not exists, if there are problems with the db
     *
     * @throws InvalidProductCodeException if the productCode is not a valid bar code, if it is null or if it is empty
     * @throws InvalidQuantityException if the quantity is less than or equal to 0
     * @throws InvalidPricePerUnitException if the price per unit of product is less than or equal to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        Integer id = -1;
    	if(productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?") || !isValid(productCode))
        	throw new InvalidProductCodeException();
        if(this.loggedUser == null || (this.loggedUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.loggedUser.getRole().compareToIgnoreCase("ShopManager")!=0))
        	throw new UnauthorizedException();
    	if(quantity <= 0)
        	throw new InvalidQuantityException();
    	if(pricePerUnit <= 0)
        	throw new InvalidPricePerUnitException();
        EZShopDb ezshopDb = new EZShopDb();
        ezshopDb.createConnection();
        //gestire id
        //insertOrder(OrderImpl order)
        ezshopDb.closeConnection();
    	return id;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return null;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
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

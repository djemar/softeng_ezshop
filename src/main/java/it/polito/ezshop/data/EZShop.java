package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EZShop implements EZShopInterface {
    EZShopDb ezshopDb = new EZShopDb();
    User currentUser = null;
    SaleTransactionImpl activeSaleTransaction = null;
    ReturnTransaction activeReturnTransaction = null;
    

    // //Michele
    // UserImpl currentUser = new UserImpl("michele", "Soldi", "Administrator", 3);
    // public void setCurrentUser(UserImpl user) {
    // this.currentUser = user;
    // }


    @Override
    public void reset() {
        if (ezshopDb.createConnection()) {
            ezshopDb.resetTables();
            ezshopDb.closeConnection();
        }
    }

    @Override
    public Integer createUser(String username, String password, String role)
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        Integer id = -1;
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException();
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException();
        if (role == null || role.isEmpty()
                || (role.compareToIgnoreCase("Administrator") != 0
                        && role.compareToIgnoreCase("Cashier") != 0
                        && role.compareToIgnoreCase("ShopManager") != 0))
            throw new InvalidRoleException();
        if (ezshopDb.createConnection()) {
            if (!ezshopDb.getUserbyName(username))
                id = ezshopDb.insertUser(new UserImpl(username, password, role));
            ezshopDb.closeConnection();
        }
        return id;

    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        boolean done = false;
        if (this.currentUser == null
                || this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0)
            throw new UnauthorizedException();
        if (id == null || id <= 0)
            throw new InvalidUserIdException();
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getUser(id) != null) {
                if (ezshopDb.deleteUser(id))
                    done = true;
            }
            ezshopDb.closeConnection();
        }
        return done;

    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        List<User> users = new ArrayList<User>();
        if (this.currentUser == null
                || this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0)
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            users = ezshopDb.getAllUsers();
            ezshopDb.closeConnection();
        }
        return users;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        User u = null;
        if (id == null || id <= 0)
            throw new InvalidUserIdException();
        if (this.currentUser == null
                || this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0)
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            u = ezshopDb.getUser(id);
            ezshopDb.closeConnection();
        }
        return u;
    }

    @Override
    public boolean updateUserRights(Integer id, String role)
            throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        boolean done = false;
        if (id == null || id <= 0)
            throw new InvalidUserIdException();
        if (role == null || role.isEmpty()
                || (role.compareToIgnoreCase("Administrator") != 0
                        && role.compareToIgnoreCase("Cashier") != 0
                        && role.compareToIgnoreCase("ShopManager") != 0))
            throw new InvalidRoleException();
        if (this.currentUser == null
                || this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0)
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getUser(id) != null) {
                if (ezshopDb.updateUserRights(id, role))
                    done = true;
            }
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    public User login(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException();
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException();
        if (ezshopDb.createConnection()) {
            this.currentUser = ezshopDb.checkCredentials(username, password);
            ezshopDb.closeConnection();
        }
        return currentUser;
    }

    @Override
    public boolean logout() {
        boolean logged = false;
        if (this.currentUser != null) {
            this.currentUser = null;
            logged = true;
        }
        return logged;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit,
            String note) throws InvalidProductDescriptionException, InvalidProductCodeException,
            InvalidPricePerUnitException, UnauthorizedException {
        int id = -1;
        if (description == null || description.isEmpty())
            throw new InvalidProductDescriptionException();
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getProductTypeByBarCode(productCode) == null) {
                if (note == null)
                    note = new String("");
                id = ezshopDb.insertProductType(
                        new ProductTypeImpl(description, productCode, pricePerUnit, note));
            }
        }
        ezshopDb.closeConnection();
        return id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice,
            String newNote) throws InvalidProductIdException, InvalidProductDescriptionException,
            InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        boolean done = false;
        if (id == null || id <= 0)
            throw new InvalidProductIdException();
        if (newDescription == null || newDescription.isEmpty())
            throw new InvalidProductDescriptionException();
        if (newPrice <= 0)
            throw new InvalidPricePerUnitException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (newCode == null || newCode.isEmpty() || !newCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(newCode))
            throw new InvalidProductCodeException();
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getProductTypeByBarCode(newCode) == null
                    && ezshopDb.getProductTypeById(id) != null) {
                if (ezshopDb.updateProductType(id, newDescription, newCode, newPrice, newNote))
                    done = true;
            }
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    public boolean deleteProductType(Integer id)
            throws InvalidProductIdException, UnauthorizedException {
        boolean done = false;
        if (id == null || id <= 0)
            throw new InvalidProductIdException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getProductTypeById(id) != null) {
                if (ezshopDb.deleteProductType(id))
                    done = true;
            }
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        List<ProductType> products = new ArrayList<ProductType>();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("Cashier") != 0))
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            products = ezshopDb.getAllProductTypes();
            ezshopDb.closeConnection();
        }
        return products;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode)
            throws InvalidProductCodeException, UnauthorizedException {
        ProductType p = null;
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (barCode == null || barCode.isEmpty() || !barCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(barCode))
            throw new InvalidProductCodeException();
        if (ezshopDb.createConnection()) {
            p = ezshopDb.getProductTypeByBarCode(barCode);
            ezshopDb.closeConnection();
        }
        return p;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description)
            throws UnauthorizedException {
        List<ProductType> products = new ArrayList<ProductType>();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            if (description == null)
                description = new String("");
            products = ezshopDb.getProductTypesByDescription(description);
            ezshopDb.closeConnection();
        }
        return products;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded)
            throws InvalidProductIdException, UnauthorizedException {
        boolean done = false;
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            ProductType p = ezshopDb.getProductTypeById(productId);
            if (!p.getLocation().isEmpty() && ezshopDb.getProductTypeById(productId) != null
                    && !ezshopDb.updateQuantity(productId, toBeAdded))
                done = true;
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    /* check condition! */
    public boolean updatePosition(Integer productId, String newPos)
            throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        boolean done = false;
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        String n[] = newPos.split("-");
        if ((newPos != null && !n[0].isEmpty() && !n[2].isEmpty() && !n[1].isEmpty())
                && (!n[0].matches("-?\\d+(\\.\\d+)?") || !n[2].matches("-?\\d+(\\.\\d+)?")))
            // n[0] and n[2] are integers
            throw new InvalidLocationException();
        if (ezshopDb.createConnection()) {
            /* questo controllo non va */
            if (newPos == null || (n[0].isEmpty() && n[2].isEmpty() && n[1].isEmpty()))
                newPos = new String("");
            if (ezshopDb.getProductTypeById(productId) != null
                    && !ezshopDb.checkExistingPosition(newPos)) {
                if (ezshopDb.updatePosition(productId, newPos))
                    done = true;
            }
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {
        Integer id = -1;
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (quantity <= 0)
            throw new InvalidQuantityException();
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();
        ezshopDb.createConnection();
        // gestire id
        // insertOrder(OrderImpl order)
        ezshopDb.closeConnection();
        return id;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {

        int i = -1;


        if (productCode == null || productCode == "")
            throw new InvalidProductCodeException("Invalid product Code ");


        if (quantity <= 0)
            throw new InvalidQuantityException("Invalid Quantity");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("Invalid price per unit");
        if (currentUser == null || currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException("Unauthorized user");
        boolean b = ezshopDb.createConnection();
        if (b == false)
            return -1;
        ProductTypeImpl prod = ezshopDb.getProductTypeByBarCode(productCode);
        if (prod != null && ezshopDb.getBalance() > pricePerUnit * quantity) {

            int orderId = ezshopDb.getOrderNumber() + 1; // diego- function
            int bn = ezshopDb.getBalnceOperationsNumber() + 1;


            BalanceOperationImpl balanceOp =
                    new BalanceOperationImpl(bn, LocalDate.now(), quantity * pricePerUnit, "ORDER");
            OrderImpl o =
                    new OrderImpl(orderId + 1, productCode, pricePerUnit, quantity, "PAYED", bn);
            ezshopDb.insertOrder(o);
            ezshopDb.insertBalanceOperation(balanceOp);
            i = orderId;
        }

        ezshopDb.closeConnection();
        return i;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        boolean b = false;


        if (orderId <= 0 || orderId == null)
            throw new InvalidOrderIdException("Invalid order id");

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        b = ezshopDb.createConnection();
        if (b == false)
            return b;
        OrderImpl o = ezshopDb.getOrder(orderId);
        if (o != null && (o.getStatus() == "ISSUED" || o.getStatus() == "ORDERED")) {
            int bn = ezshopDb.getBalnceOperationsNumber() + 1;


            BalanceOperationImpl bo = new BalanceOperationImpl(bn, LocalDate.now(),
                    o.getQuantity() * o.getPricePerUnit(), "ORDER");
            ezshopDb.insertBalanceOperation(bo);
            ezshopDb.updateOrder(o.getOrderId(), o.getProductCode(), o.getPricePerUnit(),
                    o.getQuantity(), "PAYED", bo.getBalanceId());
            b = true;
        }


        ezshopDb.closeConnection();
        return b;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId)
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        boolean b = false;



        if (orderId <= 0 || orderId == null)
            throw new InvalidOrderIdException("Invalid order id");

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        OrderImpl o = ezshopDb.getOrder(orderId);
        if (o != null && (o.getStatus() == "PAYED")) {
            if (o.getStatus() == "COMPLETED")
                return true;
            b = ezshopDb.createConnection();
            if (b == false)
                return false;
            ProductTypeImpl prod = ezshopDb.getProductTypeByBarCode(o.getProductCode());
            if (prod == null || prod.getLocation() != null)
                throw new InvalidLocationException("Invalid Location");
            ezshopDb.updateOrder(o.getOrderId(), o.getProductCode(), o.getPricePerUnit(),
                    o.getQuantity(), "COMPLETED", o.getBalanceId());
            ezshopDb.updateProductType(prod.getId(), prod.getProductDescription(),
                    prod.getBarCode(), prod.getPricePerUnit(), prod.getNote());
            b = true;
            ezshopDb.closeConnection();
        }

        return b;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        List<Order> list = null;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            list = ezshopDb.getAllOrders();
            ezshopDb.closeConnection();
        }
        return list;
    }

    @Override
    public Integer defineCustomer(String customerName)
            throws InvalidCustomerNameException, UnauthorizedException {
        int i = -1;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerName == null || customerName == "")
            throw new InvalidCustomerNameException();

        if (ezshopDb.createConnection()) {
            CustomerImpl c = new CustomerImpl(customerName);
            i = ezshopDb.insertCustomer(c);

            ezshopDb.closeConnection();
        }

        return i;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
            throws InvalidCustomerNameException, InvalidCustomerCardException,
            InvalidCustomerIdException, UnauthorizedException {
        boolean b = false;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (newCustomerName == null || newCustomerName == "")
            throw new InvalidCustomerNameException();

        if (newCustomerCard == null || newCustomerCard == "" || newCustomerCard.length() != 10
                || Utils.isOnlyDigit(newCustomerCard))
            throw new InvalidCustomerCardException("Invalid customer card");
        CustomerImpl c = ezshopDb.getCustomer(id);
        if (c != null) {
            b = ezshopDb.updateCustomer(id, newCustomerName, c.getCustomerCard(), c.getPoints());
            b &= ezshopDb.attachCardToCustomer(newCustomerCard, id);
        }
        return b;
    }

    @Override
    public boolean deleteCustomer(Integer id)
            throws InvalidCustomerIdException, UnauthorizedException {
        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException("Invalid customer id ");
        boolean boo = false;
        CustomerImpl c = ezshopDb.getCustomer(id);
        if (c != null)
            boo = ezshopDb.deleteCustomer(c);

        return boo;
    }

    @Override
    public Customer getCustomer(Integer id)
            throws InvalidCustomerIdException, UnauthorizedException {
        Customer c = null;
        try {
            if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                    || currentUser.getRole().equals("ShopManager")
                    || currentUser.getRole().equals("Cashier")))
                throw new UnauthorizedException("Unauthorized user");
            if (id == null || id <= 0)
                throw new InvalidCustomerCardException("Invalid customer id");
            c = ezshopDb.getCustomer(id);
        } catch (InvalidCustomerCardException e) {
            System.err.println(e.getMessage());
        } catch (UnauthorizedException e) {
            System.err.println(e.getMessage());
        }
        return c;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        List<Customer> l = null;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            l = ezshopDb.getAllCustomer();
            ezshopDb.closeConnection();
        }
        return (List<Customer>) l;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        String c = null;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");



        int n = ezshopDb.getCustomerCardNumber() + 1;
        String ns = Integer.toString(n);
        String customerCard = ns;
        for (int i = 0; 10 - ns.length() > i; i++) {
            customerCard += "0";
        }
        ezshopDb.insertCustomerCard(customerCard);
        c = customerCard;

        return c;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        boolean b = false;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerCard == null || customerCard == "" || customerCard.length() != 10
                || !Utils.isOnlyDigit(customerCard))
            throw new InvalidCustomerCardException("Invalid customer card");

        if (ezshopDb.createConnection()) {
            b = ezshopDb.attachCardToCustomer(customerCard, customerId);
            ezshopDb.closeConnection();
        }

        return b;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {
        boolean b = false;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerCard == null || customerCard == "" || customerCard.length() != 10
                || !Utils.isOnlyDigit(customerCard))
            throw new InvalidCustomerCardException("Invalid customer card");
        if (ezshopDb.createConnection()) {
            CustomerImpl c = ezshopDb.getCustomerByCard(customerCard);
            if (c != null && pointsToBeAdded + c.getPoints() >= 0) {
                int points = c.getPoints() + pointsToBeAdded;
                b = ezshopDb.updateCustomer(c.getId(), c.getCustomerName(), c.getCustomerCard(),
                        points);
            }
            ezshopDb.closeConnection();
        }

        return b;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        int i = -1;
        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection() && (i = ezshopDb.SaleTransactionNumber() + 1) > 0) {

            activeSaleTransaction = new SaleTransactionImpl(i);
            ezshopDb.closeConnection();
        }
        return i;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidQuantityException, UnauthorizedException {
        boolean b = false;
        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")
                || currentUser.getRole().equals("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException("Invalid transaction ID");
        if (ezshopDb.createConnection()) {
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);

            if (p != null && activeSaleTransaction.getStatus().equalsIgnoreCase("open") && p.getQuantity() >= amount && activeSaleTransaction != null)
            	activeSaleTransaction.getEntries().add(new TicketEntryImpl(p.getBarCode(), p.getProductDescription(),
                        amount, p.getPricePerUnit(), -1));
            b = true;
        }
        ezshopDb.closeConnection();

        return b;

    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode,
            double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidDiscountRateException, UnauthorizedException {
        boolean done = false;
        if(this.currentUser == null || (this.currentUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.currentUser.getRole().compareToIgnoreCase("ShopManager")!=0 && this.currentUser.getRole().compareToIgnoreCase("Cashier")!=0))
        	throw new UnauthorizedException();
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?") || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();
        //check su activetrans è corretto?
        if(activeSaleTransaction != null && activeSaleTransaction.getStatus().equalsIgnoreCase("open") && this.activeSaleTransaction.getEntries().stream().anyMatch(x-> x.getBarCode().equals(productCode))) { 
        	TicketEntry t = this.activeSaleTransaction.getEntries().stream().filter(x-> x.getBarCode().equals(productCode)).collect(Collectors.toList()).get(0);
        	t.setDiscountRate(discountRate);
        	done = true;
        }
            return done;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
            throws InvalidTransactionIdException, InvalidDiscountRateException,
            UnauthorizedException {
        boolean done = false;
        if(this.currentUser == null || (this.currentUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.currentUser.getRole().compareToIgnoreCase("ShopManager")!=0 && this.currentUser.getRole().compareToIgnoreCase("Cashier")!=0))
        	throw new UnauthorizedException();
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();
        if (activeSaleTransaction != null || !activeSaleTransaction.getStatus().equalsIgnoreCase("payed")) { 
        	this.activeSaleTransaction.setDiscountRate(discountRate);
        	done = true;
        }
        return done;
    }

    @Override
    public int computePointsForSale(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
    	int points = -1;
    	if(transactionId == null || transactionId <= 0)
        	throw new InvalidTransactionIdException();
        if(this.currentUser == null || (this.currentUser.getRole().compareToIgnoreCase("Administrator")!=0 && this.currentUser.getRole().compareToIgnoreCase("ShopManager")!=0 && this.currentUser.getRole().compareToIgnoreCase("Cashier")!=0))
        	throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
        	SaleTransaction s = ezshopDb.getSaleTransaction(transactionId);
        	if (s != null || this.activeSaleTransaction != null) 
            	points = (int) (s.getPrice() / 10);
            ezshopDb.closeConnection();
        }

        return points;

    }

    @Override
    public boolean endSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        if (activeSaleTransaction == null
                || activeSaleTransaction.getStatus().equalsIgnoreCase("closed"))
            return false;
        else
            activeSaleTransaction.setStatus("closed");

        // TODO decrease items in inventory

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        boolean isSuccess = ezshopDb.insertSaleTransaction(activeSaleTransaction);
        ezshopDb.closeConnection();

        return isSuccess;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber)
            throws InvalidTransactionIdException, UnauthorizedException {
        Integer transactionId = saleNumber;
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        if (ezshopDb.getSaleTransaction(transactionId).getStatus().equalsIgnoreCase("payed")) {
            ezshopDb.closeConnection();
            // TODO add items back to inventory? no info in interface doc cfr.
            // deleteReturnTransaction

            return false;
        }

        boolean isSuccess = ezshopDb.deleteSaleTransaction(transactionId);

        ezshopDb.closeConnection();

        return isSuccess;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return null;

        SaleTransactionImpl saleTransaction = ezshopDb.getSaleTransaction(transactionId);
        if (saleTransaction.getStatus().equalsIgnoreCase("payed")) {
            ezshopDb.closeConnection();
            return null;
        }

        ezshopDb.closeConnection();

        return saleTransaction;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber)
            throws /* InvalidTicketNumberException, */InvalidTransactionIdException,
            UnauthorizedException {
        Integer transactionId = saleNumber;
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();


        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;

        if (!(ezshopDb.getSaleTransaction(transactionId)).getStatus().equalsIgnoreCase("payed")) {
            ezshopDb.closeConnection();
            return -1;
        }

        Integer returnId = ezshopDb.newReturnTransactionId();
        ezshopDb.closeConnection();

        if (returnId >= 0)
            activeReturnTransaction = new ReturnTransaction(returnId, transactionId);

        return returnId;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidQuantityException, UnauthorizedException {
        SaleTransactionImpl saleTransaction;
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0)
            throw new InvalidQuantityException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        if (activeReturnTransaction.getReturnId() != returnId)
            return false;

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        saleTransaction = ezshopDb.getSaleTransaction(activeReturnTransaction.getTransactionId());
        if (saleTransaction == null) {
            ezshopDb.closeConnection();
            return false;
        }

        List<TicketEntry> entries = saleTransaction.getEntries();
        if (!Utils.containsProduct(entries, productCode)) {
            ezshopDb.closeConnection();
            return false;
        }
        TicketEntryImpl ticketEntry =
                (TicketEntryImpl) Utils.getProductFromEntries(entries, productCode);
        if (ticketEntry.getAmount() < amount) {
            ezshopDb.closeConnection();
            return false;
        }

        double money = ticketEntry.getPricePerUnit()
                - ((ticketEntry.getPricePerUnit() * ticketEntry.getDiscountRate()));
        activeReturnTransaction.updateTotal(money);

        activeReturnTransaction.addProductToReturn(productCode, amount);

        return true;

    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        if (returnId != activeReturnTransaction.getReturnId())
            return false;

        if (!commit) {
            activeReturnTransaction = null;
            return true;
        }

        if (activeReturnTransaction == null)
            return false;
        else
            activeReturnTransaction.setStatus("closed");

        // TODO decrease saleTransaction items and total price and add items back to inventory

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        boolean isSuccess = ezshopDb.insertReturnTransaction(activeReturnTransaction);
        conn = ezshopDb.closeConnection();

        return isSuccess;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        if (ezshopDb.getReturnTransaction(returnId).getStatus().equalsIgnoreCase("payed")) {
            ezshopDb.closeConnection();
            return false;
        }

        boolean isSuccess = ezshopDb.deleteReturnTransaction(returnId);

        // TODO undo decrease saleTransaction items and total price and add items back to inventory


        ezshopDb.closeConnection();

        return isSuccess;
    }    		
    
    public void setFinalPrice(SaleTransactionImpl s) {
    	if(s.getEntries().isEmpty())
    		s.setPrice(0);
    	else {
    		double price = s.getEntries().stream().mapToDouble(x->{
    			double discount;
    			if(x.getDiscountRate() != -1)
    				discount = x.getDiscountRate();
    			else
    				discount = 0;
    			return x.getAmount() * x.getPricePerUnit() - x.getAmount() * x.getPricePerUnit() * discount; 
    		})
    				.sum();
    		if(s.getDiscountRate() != -1)
    			price = price - s.getDiscountRate() * price;
    		 s.setPrice(price);
    	}
    }

    @Override
    //settare stato "payed"
    public double receiveCashPayment(Integer ticketNumber, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        Integer transactionID = ticketNumber;
        if (transactionID == null || transactionID <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        if (cash <= 0)
            throw new InvalidPaymentException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;
        SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezshopDb.closeConnection();
            return -1;
        }
        setFinalPrice(s);
        Double totalPrice = s.getPrice();
        if (cash < totalPrice) {
            ezshopDb.closeConnection();
            return -1;
        }
        // TODO insertBalanceOperation
        return cash - totalPrice;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException,
            UnauthorizedException {
        Integer transactionID = ticketNumber;
        if (transactionID == null || transactionID <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        if (creditCard == null || creditCard.isEmpty() || !Utils.validateCreditCard(creditCard))
            throw new InvalidCreditCardException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezshopDb.closeConnection();
            return false;
        }

        // TODO check credit cards

        // TODO insertBalanceOperation

        return true;
    }

    @Override
    public double returnCashPayment(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;
        ReturnTransaction r = ezshopDb.getReturnTransaction(returnId);
        if (r == null) {
            ezshopDb.closeConnection();
            return -1;
        }
        if (!r.getStatus().equalsIgnoreCase("closed")) {
            ezshopDb.closeConnection();
            return -1;
        }

        // TODO insert balance operation

        return r.getTotal();
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException,
            UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        if (creditCard == null || creditCard.isEmpty() || !Utils.validateCreditCard(creditCard))
            throw new InvalidCreditCardException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;
        ReturnTransaction r = ezshopDb.getReturnTransaction(returnId);
        if (r == null) {
            ezshopDb.closeConnection();
            return -1;
        }
        if (!r.getStatus().equalsIgnoreCase("closed")) {
            ezshopDb.closeConnection();
            return -1;
        }

        // TODO check if card is not registered

        // TODO insert balance operation
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        if (toBeAdded + computeBalance() < 0)
            return false;

        boolean isSuccess = ezshopDb.recordBalanceUpdate(toBeAdded);

        return isSuccess;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
            throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return new ArrayList<BalanceOperation>();

        List<BalanceOperation> list = ezshopDb.getAllBalanceOperations(from, to);

        return list;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;

        List<BalanceOperation> list = ezshopDb.getAllBalanceOperations(null, null);
        if (list.isEmpty())
            return -1;

        double balance = list.stream().mapToDouble(item -> item.getMoney()).sum();
        return balance;
    }
}

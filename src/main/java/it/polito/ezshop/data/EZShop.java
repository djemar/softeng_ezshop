package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EZShop implements EZShopInterface {
    public User loggedUser = null;
    EZShopDb ezshopDb = new EZShopDb();
    EZShopDb db = new EZShopDb();
    List<TicketEntryImpl> sales = new ArrayList<TicketEntryImpl>();
    UserImpl currentUser = new UserImpl("michele", "Soldi", "Administrator", 3);
    SaleTransactionImpl activeSaleTransaction = null;

    public void setCurrentUser(UserImpl user) {
        this.currentUser = user;
    }

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
        if (this.loggedUser == null
                || this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0)
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
        if (this.loggedUser == null
                || this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0)
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
        if (this.loggedUser == null
                || this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0)
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
        if (this.loggedUser == null
                || this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0)
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
            this.loggedUser = ezshopDb.checkCredentials(username, password);
            ezshopDb.closeConnection();
        }
        return loggedUser;
    }

    @Override
    public boolean logout() {
        boolean logged = false;
        if (this.loggedUser != null) {
            this.loggedUser = null;
            logged = true;
        }
        return logged;
    }

    public static boolean isValid(String code) {
        if (code.length() == 13) {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                if ((i + 1) % 2 == 0)
                    sum += Character.getNumericValue(code.charAt(i)) * 3;
                else
                    sum += Character.getNumericValue(code.charAt(i));
            }
            if (Math.round((sum + 5) / 10.0) * 10 - sum == Character
                    .getNumericValue(code.charAt(12)))
                return true;
        }

        return false;
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !isValid(productCode))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (newCode == null || newCode.isEmpty() || !newCode.matches("-?\\d+(\\.\\d+)?")
                || !isValid(newCode))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("Cashier") != 0))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (barCode == null || barCode.isEmpty() || !barCode.matches("-?\\d+(\\.\\d+)?")
                || !isValid(barCode))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
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
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        String n[] = newPos.split("-");
        if ((newPos != null && !n[0].isEmpty() && !n[2].isEmpty() && !n[1].isEmpty())
                && (!n[0].matches("-?\\d+(\\.\\d+)?") || !n[2].matches("-?\\d+(\\.\\d+)?"))) // n[0]
                                                                                             // and
                                                                                             // n[2]
                                                                                             // are
                                                                                             // integers
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
    /**
     * This method issues an order of <quantity> units of product with given <productCode>, each
     * unit will be payed <pricePerUnit> to the supplier. <pricePerUnit> can differ from the
     * re-selling price of the same product. The product might have no location assigned in this
     * step. It can be invoked only after a user with role "Administrator" or "ShopManager" is
     * logged in.
     *
     * @param productCode the code of the product that we should order as soon as possible
     * @param quantity the quantity of product that we should order
     * @param pricePerUnit the price to correspond to the supplier (!= than the resale price of the
     *        shop) per unit of product
     *
     * @return the id of the order (> 0) -1 if the product does not exists, if there are problems
     *         with the db
     *
     * @throws InvalidProductCodeException if the productCode is not a valid bar code, if it is null
     *         or if it is empty
     * @throws InvalidQuantityException if the quantity is less than or equal to 0
     * @throws InvalidPricePerUnitException if the price per unit of product is less than or equal
     *         to 0
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to
     *         perform the operation
     */
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {
        Integer id = -1;
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !isValid(productCode))
            throw new InvalidProductCodeException();
        if (this.loggedUser == null
                || (this.loggedUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.loggedUser.getRole().compareToIgnoreCase("ShopManager") != 0))
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
        boolean b = db.createConnection();
        if (b == false)
            return -1;
        ProductTypeImpl prod = db.getProductTypeByBarCode(productCode);
        if (prod != null && db.getBalance() > pricePerUnit * quantity) {

            int orderId = db.getOrderNumber() + 1; // diego- function
            int bn = db.getBalnceOperationsNumber() + 1;


            BalanceOperationImpl b =
                    new BalanceOperationImpl(bn, LocalDate.now(), quantity * pricePerUnit, "ORDER");
            OrderImpl o =
                    new OrderImpl(orderId + 1, productCode, pricePerUnit, quantity, "PAYED", bn);
            db.insertOrder(o);
            db.insertBalanceOperation(b);
            i = orderId;
        }

        db.closeConnection();
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
        b = db.createConnection();
        if (b == false)
            return b;
        OrderImpl o = db.getOrder(orderId);
        if (o != null && (o.getStatus() == "ISSUED" || o.getStatus() == "ORDERED")) {
            int bn = db.getBalnceOperationsNumber() + 1;


            BalanceOperationImpl bo = new BalanceOperationImpl(bn, LocalDate.now(),
                    o.getQuantity() * o.getPricePerUnit(), "ORDER");
            db.insertBalanceOperation(bo);
            db.updateOrder(o.getOrderId(), o.getProductCode(), o.getPricePerUnit(), o.getQuantity(),
                    "PAYED", bo.getBalanceId());
            b = true;
        }


        db.closeConnection();
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
        OrderImpl o = db.getOrder(orderId);
        if (o != null && (o.getStatus() == "PAYED")) {
            if (o.getStatus() == "COMPLETED")
                return true;
            b = db.createConnection();
            if (b == false)
                return false;
            ProductTypeImpl prod = db.getProductTypeByBarCode(o.getProductCode());
            if (prod == null || prod.getLocation() != null)
                throw new InvalidLocationException("Invalid Location");
            db.updateOrder(o.getOrderId(), o.getProductCode(), o.getPricePerUnit(), o.getQuantity(),
                    "COMPLETED", o.getBalanceId());
            db.updateProductType(prod.getId(), prod.getProductDescription(), prod.getBarCode(),
                    prod.getPricePerUnit(), prod.getNote());
            b = true;
            db.closeConnection();
        }

        return b;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        List<Order> list = null;

        if (currentUser == null || !(currentUser.getRole().equals("Administrator")
                || currentUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        if (db.createConnection()) {
            list = db.getAllOrders();
            db.closeConnection();
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

        if (db.createConnection()) {
            CustomerImpl c = new CustomerImpl(customerName);
            i = db.insertCustomer(c);

            db.closeConnection();
        }

        return i;
    }

    public static boolean onlyDigits(String str, int n) {
        for (int i = 0; i < n; i++) {


            if (!Character.isDigit(str.charAt(i)))
                return false;


        }
        return true;
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
                || onlyDigits(newCustomerCard, newCustomerCard.length()))
            throw new InvalidCustomerCardException("Invalid customer card");
        CustomerImpl c = db.getCustomer(id);
        if (c != null) {
            b = db.updateCustomer(id, newCustomerName, c.getCustomerCard(), c.getPoints());
            b &= db.attachCardToCustomer(newCustomerCard, id);
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
        CustomerImpl c = db.getCustomer(id);
        if (c != null)
            boo = db.deleteCustomer(c);

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
            c = db.getCustomer(id);
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
        if (db.createConnection()) {
            l = db.getAllCustomer();
            db.closeConnection();
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



        int n = db.getCustomerCardNumber() + 1;
        String ns = Integer.toString(n);
        String customerCard = ns;
        for (int i = 0; 10 - ns.length() > i; i++) {
            customerCard += "0";
        }
        db.insertCustomerCard(customerCard);
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
                || !onlyDigits(customerCard, customerCard.length()))
            throw new InvalidCustomerCardException("Invalid customer card");

        if (db.createConnection()) {
            b = db.attachCardToCustomer(customerCard, customerId);
            db.closeConnection();
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
                || !onlyDigits(customerCard, customerCard.length()))
            throw new InvalidCustomerCardException("Invalid customer card");
        if (db.createConnection()) {
            CustomerImpl c = db.getCustemByCard(customerCard);
            if (c != null && pointsToBeAdded + c.getPoints() >= 0) {
                int points = c.getPoints() + pointsToBeAdded;
                b = db.updateCustomer(c.getId(), c.getCustomerName(), c.getCustomerCard(), points);
            }
            db.closeConnection();
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
        if (db.createConnection() && (i = db.SaleTransactionNumber() + 1) > 0) {

            activeSaleTransaction = new SaleTransactionImpl(i);
            sales.clear();
            db.closeConnection();
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
        if (db.createConnection()) {
            ProductTypeImpl p = db.getProductTypeByBarCode(productCode);

            if (p != null && p.getQuantity() >= amount && activeSaleTransaction != null)
                sales.add(new TicketEntryImpl(p.getBarCode(), p.getProductDescription(), amount,
                        p.getPricePerUnit(), 0));
            b = true;
        }
        db.closeConnection();
    }

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
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
            throws InvalidTransactionIdException, InvalidDiscountRateException,
            UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber)
            throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber)
            throws /* InvalidTicketNumberException, */InvalidTransactionIdException,
            UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit)
            throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException,
            UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException,
            UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
            throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}

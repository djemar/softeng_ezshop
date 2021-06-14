package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EZShop implements EZShopInterface {
    EZShopDb ezshopDb = new EZShopDb();
    User currentUser = null;
    public SaleTransactionImpl activeSaleTransaction = null;
    public ReturnTransaction activeReturnTransaction = null;

    @Override
    public void reset() {
        if (ezshopDb.createConnection()) {
            ezshopDb.resetDB();
            ezshopDb.closeConnection();
        }
    }

    @Override
    public Integer createUser(String username, String password, String role)
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        Integer id = -1;
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("InvalidUsernameException");
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("InvalidPasswordException");
        if (role == null || role.isEmpty()
                || (role.compareToIgnoreCase("Administrator") != 0
                        && role.compareToIgnoreCase("Cashier") != 0
                        && role.compareToIgnoreCase("ShopManager") != 0))
            throw new InvalidRoleException("invalid role");
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
            throw new InvalidUserIdException("invalid id");
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
            throw new InvalidProductDescriptionException("InvalidProductDescriptionException");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("InvalidPricePerUnitException");
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException("InvalidProductCodeException");
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
            throw new InvalidPricePerUnitException("InvalidPricePerUnitException");
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (newCode == null || newCode.isEmpty() || !newCode.matches("-?\\d+(\\.\\d+)?")
                || !Utils.validateBarcode(newCode))
            throw new InvalidProductCodeException();
        if (ezshopDb.createConnection()) {
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(newCode);
            if (((p != null && p.getId().equals(id)) || p == null)
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
            throw new InvalidProductIdException("InvalidProductIdException");
        if (this.currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
            throw new UnauthorizedException("UnauthorizedException");
        if (ezshopDb.createConnection()) {
            ProductType p = ezshopDb.getProductTypeById(productId);
            if (p != null && p.getLocation() != null && !p.getLocation().isEmpty()
                    && !ezshopDb.updateQuantity(productId, toBeAdded))
                done = true;
            ezshopDb.closeConnection();
        }
        return done;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos)
            throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        boolean done = false;
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        String pos = null;
        if (newPos == null || newPos.isEmpty())
            pos = new String("");
        else {
            String n[] = newPos.split("-");
            if (n.length != 3 || (n.length == 3
                    && (!n[0].matches("-?\\d+(\\.\\d+)?") || !n[2].matches("-?\\d+(\\.\\d+)?"))))
                throw new InvalidLocationException();
        }
        if (pos == null)
            pos = newPos;
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getProductTypeById(productId) != null
                    && (!ezshopDb.checkExistingPosition(pos) || pos.isEmpty())) {
                if (ezshopDb.updatePosition(productId, pos))
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
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0))
            throw new UnauthorizedException();
        if (quantity <= 0)
            throw new InvalidQuantityException();
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();
        if (ezshopDb.createConnection() && ezshopDb.getProductTypeByBarCode(productCode) != null) {
            id = ezshopDb.insertOrder(new OrderImpl(productCode, pricePerUnit, quantity));
            ezshopDb.closeConnection();
        }
        return id;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException,
            InvalidPricePerUnitException, UnauthorizedException {
        int id = -1;
        if (productCode == null || productCode == "" || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException("Invalid product Code ");
        if (quantity <= 0)
            throw new InvalidQuantityException("Invalid Quantity");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("Invalid price per unit");
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            ProductTypeImpl prod = ezshopDb.getProductTypeByBarCode(productCode);
            if (prod != null && ezshopDb.getBalance() >= pricePerUnit * quantity) {
                BalanceOperationImpl balanceOp = new BalanceOperationImpl(LocalDate.now(),
                        -quantity * pricePerUnit, "ORDER");
                int idBOp = ezshopDb.insertBalanceOperation(balanceOp);
                if (idBOp == -1)
                    return -1;
                OrderImpl o = new OrderImpl(productCode, pricePerUnit, quantity, "PAYED", idBOp);
                id = ezshopDb.insertOrder(o);
            }
            ezshopDb.closeConnection();
        }
        return id;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        boolean isSuccess = false;
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            OrderImpl o = ezshopDb.getOrder(orderId);
            if (o != null
                    && (o.getStatus().equalsIgnoreCase("ISSUED")
                            || o.getStatus().equalsIgnoreCase("ORDERED"))
                    && (ezshopDb.getBalance() >= o.getQuantity() * o.getPricePerUnit())) {
                BalanceOperationImpl bo = new BalanceOperationImpl(LocalDate.now(),
                        -o.getQuantity() * o.getPricePerUnit(), "ORDER");
                int idBOp = ezshopDb.insertBalanceOperation(bo);
                if (idBOp == -1)
                    return false;
                if ((ezshopDb.updateOrder(o.getOrderId(), "PAYED", idBOp)))
                    isSuccess = true;

            }
            ezshopDb.closeConnection();
        }
        return isSuccess;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId)
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        boolean isSuccess = false;
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("Invalid order id");
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            OrderImpl o = ezshopDb.getOrder(orderId);
            if (o != null && o.getStatus().equals("COMPLETED")) {
                ezshopDb.closeConnection();
                return true;
            }
            if (o != null && o.getStatus().equals("PAYED")) {
                ProductTypeImpl prod = ezshopDb.getProductTypeByBarCode(o.getProductCode());
                if (prod == null || prod.getLocation() == null || prod.getLocation().isEmpty()) {
                    ezshopDb.closeConnection();
                    throw new InvalidLocationException("Invalid Location");
                }
                if (ezshopDb.updateOrder(o.getOrderId(), "COMPLETED", o.getBalanceId())
                        && !ezshopDb.updateQuantity(prod.getId(), o.getQuantity())) {
                    isSuccess = true;
                }
            }
            ezshopDb.closeConnection();
        }
        return isSuccess;
    }

    @Override
    /* TODO */
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom)
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException,
            InvalidRFIDException {
        boolean isSuccess = false;
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("Invalid order id");
        if (RFIDfrom == null || RFIDfrom.isEmpty() || RFIDfrom.length() != 12
                || !Utils.isOnlyDigit(RFIDfrom))
            throw new InvalidRFIDException();
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
            throw new UnauthorizedException("Unauthorized user");

        if (ezshopDb.createConnection()) {

            OrderImpl o = ezshopDb.getOrder(orderId);
            if (o == null) {
                ezshopDb.closeConnection();
                return false;
            }

            if (o != null && o.getStatus().equals("COMPLETED")) {
                ezshopDb.closeConnection();
                return true;
            }
            if (!ezshopDb.verifyRFID(RFIDfrom, o.getQuantity())) {
                ezshopDb.closeConnection();
                throw new InvalidRFIDException();
            }


            if (o.getStatus().equals("PAYED")) {
                ProductTypeImpl prod = ezshopDb.getProductTypeByBarCode(o.getProductCode());
                if (prod == null || prod.getLocation() == null || prod.getLocation().isEmpty()) {
                    ezshopDb.closeConnection();
                    throw new InvalidLocationException("Invalid Location");
                }


                if (ezshopDb.updateOrder(o.getOrderId(), "COMPLETED", o.getBalanceId())
                        && !ezshopDb.updateQuantity(prod.getId(), o.getQuantity())
                        && ezshopDb.insertProducts(RFIDfrom, o.getQuantity(), o.getProductCode())) {
                    isSuccess = true;
                }
            }
            ezshopDb.closeConnection();
        }
        return isSuccess;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        List<Order> list = null;

        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")))
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

        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerName == null || customerName.isEmpty())
            throw new InvalidCustomerNameException();
        if (ezshopDb.createConnection()
                && !Utils.containsCustomer(ezshopDb.getAllCustomers(), customerName)) {
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
        boolean isSuccess = false;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (newCustomerName == null || newCustomerName == "")
            throw new InvalidCustomerNameException("invalid customer name");
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();
        if (ezshopDb.createConnection()) {
            CustomerImpl c = ezshopDb.getCustomer(id);
            if (c == null) {
                ezshopDb.closeConnection();
            }
            if (newCustomerCard != null && newCustomerCard.isEmpty())
                isSuccess = ezshopDb.updateCustomer(id, newCustomerName, "", 0);
            else if (newCustomerCard != null) {
                if (newCustomerCard.length() != 10 || !Utils.isOnlyDigit(newCustomerCard)) {
                    ezshopDb.closeConnection();
                    throw new InvalidCustomerCardException();
                }
                if (ezshopDb.getCustomerCard(newCustomerCard)
                        && ezshopDb.getCustomerByCard(newCustomerCard) == null)
                    isSuccess = ezshopDb.updateCustomer(id, newCustomerName, newCustomerCard,
                            c.getPoints());
            }
            ezshopDb.closeConnection();
        }

        return isSuccess;
    }

    @Override
    public boolean deleteCustomer(Integer id)
            throws InvalidCustomerIdException, UnauthorizedException {
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();
        boolean boo = false;
        if (ezshopDb.createConnection()) {
            CustomerImpl c = ezshopDb.getCustomer(id);
            if (c != null)
                boo = ezshopDb.deleteCustomer(c);
            ezshopDb.closeConnection();
        }
        return boo;
    }

    @Override
    public Customer getCustomer(Integer id)
            throws InvalidCustomerIdException, UnauthorizedException {
        Customer c = null;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException("");
        if (ezshopDb.createConnection()) {
            c = ezshopDb.getCustomer(id);
            ezshopDb.closeConnection();
        }

        return c;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        List<Customer> customers = new ArrayList<Customer>();

        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            customers = ezshopDb.getAllCustomers();
            ezshopDb.closeConnection();
        }
        return customers;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        String customerCard = null;

        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (ezshopDb.createConnection()) {
            int n = ezshopDb.getCustomerCardNumber() + 1;
            String ns = Integer.toString(n);
            customerCard = new String("");
            for (int i = 0; 10 - ns.length() > i; i++) {
                customerCard += "0";
            }
            customerCard += ns;
            if (!ezshopDb.insertCustomerCard(customerCard)) {
                ezshopDb.closeConnection();
                return "";
            }
            ezshopDb.closeConnection();
        }
        return customerCard;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        boolean b = false;

        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerCard == null || customerCard == "" || customerCard.length() != 10
                || !Utils.isOnlyDigit(customerCard))
            throw new InvalidCustomerCardException("Invalid customer card");

        if (customerId == null || customerId <= 0)
            throw new InvalidCustomerIdException();



        if (ezshopDb.createConnection()) {
            if (ezshopDb.getCustomerCard(customerCard)) {
                b = ezshopDb.attachCardToCustomer(customerCard, customerId);
            }
            ezshopDb.closeConnection();
        }

        return b;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {
        boolean isSuccess = false;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (customerCard == null || customerCard == "" || customerCard.length() != 10
                || !Utils.isOnlyDigit(customerCard))
            throw new InvalidCustomerCardException("Invalid customer card");
        if (ezshopDb.createConnection()) {
            if (ezshopDb.getCustomerCard(customerCard)) {
                CustomerImpl c = ezshopDb.getCustomerByCard(customerCard);
                if (c == null)
                    System.out.print("customer non trovato!!!!!!!!!");
                if (c != null && pointsToBeAdded + c.getPoints() >= 0) {
                    int points = c.getPoints() + pointsToBeAdded;
                    isSuccess = ezshopDb.updateCustomer(c.getId(), c.getCustomerName(),
                            c.getCustomerCard(), points);
                }
            }

            ezshopDb.closeConnection();
        }

        return isSuccess;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        int i = -1;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
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
        boolean isSuccess = false;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (amount < 0)
            throw new InvalidQuantityException();
        if (ezshopDb.createConnection()) {
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);
            if (p != null && activeSaleTransaction != null
                    && activeSaleTransaction.getStatus().equalsIgnoreCase("open")
                    && p.getQuantity() >= amount) {
                if (!activeSaleTransaction.getEntries().stream()
                        .anyMatch(x -> x.getBarCode().equals(productCode)))
                    activeSaleTransaction.getEntries().add(new TicketEntryImpl(p.getBarCode(),
                            p.getProductDescription(), amount, p.getPricePerUnit(), 0));
                else {
                    TicketEntry t = activeSaleTransaction.getEntries().stream()
                            .filter(x -> x.getBarCode().equals(productCode)).findFirst().get();
                    int amountf = t.getAmount() + amount;
                    t.setAmount(amountf);
                }
                activeSaleTransaction.estimatePrice();
                if (!ezshopDb.updateQuantity(p.getId(), -amount))
                    isSuccess = true;
            }
            ezshopDb.closeConnection();
        }
        return isSuccess;

    }

    @Override
    /* TODO */
    public boolean addProductToSaleRFID(Integer transactionId, String RFID)
            throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException,
            UnauthorizedException {
        boolean isSuccess = false;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (RFID == null || RFID.isEmpty() || !Utils.isOnlyDigit(RFID) || RFID.length() != 12)
            throw new InvalidRFIDException("rfid invalid");
        if (ezshopDb.createConnection()) {
            String productCode = ezshopDb.getBarCodebyRFID(RFID);
            if (productCode == null)
                return false;
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);
            if (p != null && activeSaleTransaction != null
                    && activeSaleTransaction.getTicketNumber() == transactionId
                    && activeSaleTransaction.getStatus().equalsIgnoreCase("open")) {
                if (!activeSaleTransaction.getEntries().stream()
                        .anyMatch(x -> x.getBarCode().equals(productCode)))
                    activeSaleTransaction.getEntries().add(new TicketEntryImpl(p.getBarCode(),
                            p.getProductDescription(), 1, p.getPricePerUnit(), RFID));
                else {
                    TicketEntryImpl t = (TicketEntryImpl) activeSaleTransaction.getEntries()
                            .stream().filter(x -> x.getBarCode().equals(productCode)).findFirst()
                            .get();
                    int amountf = t.getAmount() + 1;
                    t.setAmount(amountf);
                    t.addRFID(RFID);
                }

                activeSaleTransaction.estimatePrice();
                if (!ezshopDb.updateQuantity(p.getId(), -1))
                    isSuccess = true;
            }
            ezshopDb.closeConnection();
        }
        return isSuccess;
    }


    @Override
    /* TODO */
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID)
            throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException,
            UnauthorizedException {
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (RFID == null || RFID.isEmpty() || RFID.length() != 12 || !Utils.isOnlyDigit(RFID))
            throw new InvalidRFIDException();

        if (ezshopDb.createConnection()) {
            String productCode = ezshopDb.getBarCodebyRFID(RFID);
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);
            if (productCode == null || activeSaleTransaction == null || p == null
                    || activeSaleTransaction.getTicketNumber() != transactionId
                    || !activeSaleTransaction.getStatus().equalsIgnoreCase("open")) {
                ezshopDb.closeConnection();
                return false;
            }
            Optional<TicketEntry> ticketEntry = activeSaleTransaction.getEntries().stream()
                    .filter(x -> x.getBarCode().equals(productCode)).findFirst();
            if (!ticketEntry.isPresent() || ticketEntry.get() == null) {
                ezshopDb.closeConnection();
                return false;
            }
            TicketEntryImpl t = (TicketEntryImpl) ticketEntry.get();
            if (!t.getRFID().stream().filter(x -> x.equals(RFID)).findFirst().isPresent()) {
                ezshopDb.closeConnection();
                return false;
            }

            int a = t.getAmount();
            if (a == 1)
                activeSaleTransaction.getEntries().remove(t);
            else
                t.setAmount(a - 1);
            activeSaleTransaction.estimatePrice();
            if (ezshopDb.updateQuantity(p.getId(), 1)) {
                ezshopDb.closeConnection();
                return false;
            }

            activeSaleTransaction.estimatePrice();

        } else {
            return false;
        }

        ezshopDb.closeConnection();
        return true;
    }


    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidQuantityException, UnauthorizedException {
        boolean isSuccess = false;
        if (currentUser == null || !(currentUser.getRole().equalsIgnoreCase("Administrator")
                || currentUser.getRole().equalsIgnoreCase("ShopManager")
                || currentUser.getRole().equalsIgnoreCase("Cashier")))
            throw new UnauthorizedException("Unauthorized user");
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (amount < 0)
            throw new InvalidQuantityException();
        if (ezshopDb.createConnection()) {
            ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);
            if (p != null && activeSaleTransaction != null
                    && activeSaleTransaction.getStatus().equalsIgnoreCase("open")) {
                Optional<TicketEntry> ticketEntry = activeSaleTransaction.getEntries().stream()
                        .filter(x -> x.getBarCode().equals(productCode)).findFirst();
                if (!ticketEntry.isPresent()) {
                    ezshopDb.closeConnection();
                    return false;
                }
                TicketEntry t = ticketEntry.get();
                int a = t.getAmount();
                if (a < amount) {
                    ezshopDb.closeConnection();
                    return false;
                } else if (a == amount)
                    activeSaleTransaction.getEntries().remove(t);
                else
                    t.setAmount(a - amount);
                activeSaleTransaction.estimatePrice();
                if (!ezshopDb.updateQuantity(p.getId(), amount))
                    isSuccess = true;
            }
            ezshopDb.closeConnection();
        }
        return isSuccess;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode,
            double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException,
            InvalidDiscountRateException, UnauthorizedException {
        boolean done = false;
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("Cashier") != 0))
            throw new UnauthorizedException();
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();

        if (activeSaleTransaction != null
                && activeSaleTransaction.getStatus().equalsIgnoreCase("open")
                && Utils.containsProduct(activeSaleTransaction.getEntries(), productCode)) {
            long count = this.activeSaleTransaction.getEntries().stream().count();
            TicketEntry t = this.activeSaleTransaction.getEntries().stream().skip(count - 1)
                    .filter(x -> x.getBarCode().equalsIgnoreCase(productCode)).findFirst().get();
            t.setDiscountRate(discountRate);
            activeSaleTransaction.estimatePrice();
            done = true;
        }
        return done;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
            throws InvalidTransactionIdException, InvalidDiscountRateException,
            UnauthorizedException {
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("Cashier") != 0))
            throw new UnauthorizedException();
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (discountRate < 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException("invalid discount rate");

        if (activeSaleTransaction != null
                && activeSaleTransaction.getTicketNumber() == transactionId) {
            this.activeSaleTransaction.setDiscountRate(discountRate);
            activeSaleTransaction.estimatePrice();
            return true;
        }
        if (ezshopDb.createConnection() && ezshopDb.getSaleTransaction(transactionId) != null) {
            boolean isSuccess = false;
            SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionId);
            if (!s.getStatus().equalsIgnoreCase("PAYED")) {
                isSuccess = ezshopDb.setSaleDiscount(transactionId, discountRate);
            }
            ezshopDb.closeConnection();
            return isSuccess;
        }
        ezshopDb.closeConnection();
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        int points = -1;
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (this.currentUser == null
                || (this.currentUser.getRole().compareToIgnoreCase("Administrator") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("ShopManager") != 0
                        && this.currentUser.getRole().compareToIgnoreCase("Cashier") != 0))
            throw new UnauthorizedException();
        if (activeSaleTransaction != null
                && activeSaleTransaction.getTicketNumber() == transactionId) {
            points = (int) (this.activeSaleTransaction.getPrice() / 10);
            return points;
        }
        if (ezshopDb.createConnection()) {
            SaleTransaction s = ezshopDb.getSaleTransaction(transactionId);
            if (s != null)
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
                || activeSaleTransaction.getTicketNumber() != transactionId)
            return false;
        else {
            activeSaleTransaction.setStatus("CLOSED");
            activeSaleTransaction.estimatePrice();
        }
        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        boolean isSuccess = ezshopDb.insertSaleTransaction(activeSaleTransaction);
        if (isSuccess)
            activeSaleTransaction = null;
        ezshopDb.closeConnection();
        return isSuccess;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber)
            throws InvalidTransactionIdException, UnauthorizedException {
        boolean isSuccess = false;
        Integer transactionId = saleNumber;
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        if (ezshopDb.createConnection()) {
            SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionId);
            if (s != null && !s.getStatus().equalsIgnoreCase("PAYED")) {
                s.getEntries().stream().forEach(x -> {
                    ezshopDb.updateQuantity(
                            ezshopDb.getProductTypeByBarCode(x.getBarCode()).getId(),
                            x.getAmount());
                });
                isSuccess = ezshopDb.deleteSaleTransaction(transactionId);
            }
            ezshopDb.closeConnection();
        }
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
        if (saleTransaction == null) {
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
            throw new InvalidTransactionIdException("invalid id");
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();


        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;
        SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionId);
        if (s == null || !s.getStatus().equalsIgnoreCase("PAYED")) {
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
            throw new InvalidTransactionIdException("invalid id");
        if (productCode == null || productCode.isEmpty() || !Utils.validateBarcode(productCode))
            throw new InvalidProductCodeException();
        if (amount <= 0)
            throw new InvalidQuantityException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        if (activeReturnTransaction == null || activeReturnTransaction.getReturnId() != returnId)
            return false;

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        saleTransaction = ezshopDb.getSaleTransaction(activeReturnTransaction.getTransactionId());
        ezshopDb.closeConnection();

        if (saleTransaction == null) {
            return false;
        }

        List<TicketEntry> entries = saleTransaction.getEntries();
        if (!Utils.containsProduct(entries, productCode)) {
            return false;
        }
        TicketEntryImpl ticketEntry =
                (TicketEntryImpl) Utils.getProductFromEntries(entries, productCode);
        if (ticketEntry.getAmount() < amount) {
            return false;
        }

        double money = ticketEntry.getPricePerUnit() * amount
                - ((ticketEntry.getPricePerUnit() * amount * ticketEntry.getDiscountRate()));
        activeReturnTransaction.updateTotal(money);

        activeReturnTransaction.addProductToReturn(productCode, amount);

        return true;

    }

    @Override
    /* TODO!!!!!!! */
    public boolean returnProductRFID(Integer returnId, String RFID)
            throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException {
        SaleTransactionImpl saleTransaction;
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException("invalid id");
        if (RFID == null || RFID.isEmpty() || !Utils.isOnlyDigit(RFID) || RFID.length() != 12)
            throw new InvalidRFIDException("rfid invalid");
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        if (activeReturnTransaction == null || activeReturnTransaction.getReturnId() != returnId)
            return false;

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        saleTransaction = ezshopDb.getSaleTransaction(activeReturnTransaction.getTransactionId());
        Integer idRFID = ezshopDb.getTransactionByRFID(RFID);


        if (saleTransaction == null || saleTransaction.getTicketNumber() != idRFID) {
            ezshopDb.closeConnection();
            return false;
        }
        String productCode = ezshopDb.getBarCodebyRFID(RFID);
        if (productCode == null) {
            ezshopDb.closeConnection();
            return false;
        }
        ProductTypeImpl p = ezshopDb.getProductTypeByBarCode(productCode);
        double money = p.getPricePerUnit();
        activeReturnTransaction.updateTotal(money);
        activeReturnTransaction.addProductToReturn(productCode, 1);
        ezshopDb.closeConnection();

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

        if (activeReturnTransaction == null || returnId != activeReturnTransaction.getReturnId())
            return false;

        if (!commit) {
            activeReturnTransaction = null;
            return true;
        }

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;

        SaleTransaction s = ezshopDb.getSaleTransaction(activeReturnTransaction.getTransactionId());
        double diffPrice = activeReturnTransaction.getTotal() * (1 - s.getDiscountRate());
        activeReturnTransaction.setTotal(diffPrice);
        activeReturnTransaction.setStatus("CLOSED");
        boolean isSuccess = ezshopDb.insertReturnTransaction(activeReturnTransaction);
        if (isSuccess) {
            activeReturnTransaction.getReturnedProductsMap().entrySet().forEach(x -> {
                ezshopDb.updateQuantity(ezshopDb.getProductTypeByBarCode(x.getKey()).getId(),
                        x.getValue());
            });

            isSuccess = ezshopDb.updateSaleTransaction(activeReturnTransaction.getTransactionId(),
                    activeReturnTransaction.getReturnedProductsMap(), diffPrice, false);
        }
        conn = ezshopDb.closeConnection();

        return isSuccess;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException("invalid id");
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        ReturnTransaction returnTrans = ezshopDb.getReturnTransaction(returnId);
        if (returnTrans == null || returnTrans.getStatus().equalsIgnoreCase("PAYED")) {
            ezshopDb.closeConnection();
            return false;
        }

        boolean isSuccess = ezshopDb.deleteReturnTransaction(returnId);
        if (isSuccess) {
            returnTrans.getReturnedProductsMap().entrySet().forEach(x -> {
                ezshopDb.updateQuantity(ezshopDb.getProductTypeByBarCode(x.getKey()).getId(),
                        (-x.getValue()));
            });
            double diffPrice = -returnTrans.getTotal();
            ezshopDb.updateSaleTransaction(returnTrans.getTransactionId(),
                    returnTrans.getReturnedProductsMap(), diffPrice, true);
        }



        ezshopDb.closeConnection();

        return isSuccess;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        Integer transactionID = ticketNumber;
        if (transactionID == null || transactionID <= 0)
            throw new InvalidTransactionIdException();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("cashier")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        if (cash < 0)
            throw new InvalidPaymentException("invalid payment");
        if (cash == 0)
            throw new InvalidPaymentException();
        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return -1;
        SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezshopDb.closeConnection();
            return -1;
        }
        if (s.getStatus().equalsIgnoreCase("PAYED")) {
            ezshopDb.closeConnection();
            return -1;
        }
        s.estimatePrice();
        Double totalPrice = s.getPrice();
        if (cash < totalPrice) {
            ezshopDb.closeConnection();
            return -1;
        }
        ezshopDb.insertBalanceOperation(
                new BalanceOperationImpl(LocalDate.now(), totalPrice, "SALE"));
        if (!ezshopDb.payForSaleTransaction(transactionID)) {
            ezshopDb.closeConnection();
            return -1;
        }
        ezshopDb.closeConnection();
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
            throw new InvalidCreditCardException("invalid credit card");

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        SaleTransactionImpl s = ezshopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezshopDb.closeConnection();
            return false;
        } else {
            if (s.getStatus().equalsIgnoreCase("PAYED")) {
                ezshopDb.closeConnection();
                return false;
            }
            s.estimatePrice();
            if (!Utils.fromFile(creditCard, s.getPrice(), "creditcards.txt")) {
                ezshopDb.closeConnection();
                return false;
            }
            Utils.updateFile("creditcards.txt", creditCard, s.getPrice());
            ezshopDb.insertBalanceOperation(
                    new BalanceOperationImpl(LocalDate.now(), s.getPrice(), "SALE"));

            if (!ezshopDb.payForSaleTransaction(transactionID)) {
                ezshopDb.closeConnection();
                return false;
            }
            ezshopDb.closeConnection();
            return true;
        }
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
        if (!ezshopDb.payForReturnTransaction(returnId)) {
            ezshopDb.closeConnection();
            return -1;
        }
        ezshopDb.insertBalanceOperation(
                new BalanceOperationImpl(LocalDate.now(), -r.getTotal(), "RETURN"));
        ezshopDb.closeConnection();
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
        if (!ezshopDb.payForReturnTransaction(returnId)) {
            ezshopDb.closeConnection();
            return -1;
        }
        if (!Utils.updateFile("creditcards.txt", creditCard, -r.getTotal())) {
            ezshopDb.closeConnection();
            return -1;
        }
        ezshopDb.insertBalanceOperation(
                new BalanceOperationImpl(LocalDate.now(), -r.getTotal(), "RETURN"));
        ezshopDb.closeConnection();
        return r.getTotal();
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return false;
        List<BalanceOperation> list = ezshopDb.getAllBalanceOperations(null, null);
        double balance = 0;
        if (!list.isEmpty())
            balance = list.stream().mapToDouble(item -> item.getMoney()).sum();
        if (toBeAdded + balance < 0)
            return false;

        boolean isSuccess = ezshopDb.recordBalanceUpdate(toBeAdded);
        ezshopDb.closeConnection();
        return isSuccess;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
            throws UnauthorizedException {
        List<BalanceOperation> list = new ArrayList<BalanceOperation>();
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezshopDb.createConnection();
        if (!conn)
            return new ArrayList<BalanceOperation>();

        list = ezshopDb.getAllBalanceOperations(from, to);
        ezshopDb.closeConnection();

        return list;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        double balance = 0;
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        boolean conn = ezshopDb.createConnection();
        if (conn) {
            List<BalanceOperation> list = ezshopDb.getAllBalanceOperations(null, null);
            ezshopDb.closeConnection();
            if (!list.isEmpty())
                balance = list.stream().mapToDouble(item -> item.getMoney()).sum();

        }
        return balance;


    }
}

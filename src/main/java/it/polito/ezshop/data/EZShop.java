package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EZShop implements EZShopInterface {
    EZShopDb ezShopDb = new EZShopDb();
    SaleTransactionImpl activeSaleTransaction = null;
    ReturnTransaction activeReturnTransaction = null;
    User currentUser = null;

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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;
        boolean isSuccess = ezShopDb.insertSaleTransaction(activeSaleTransaction);
        ezShopDb.closeConnection();

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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;

        if (ezShopDb.getSaleTransaction(transactionId).getStatus().equalsIgnoreCase("payed")) {
            ezShopDb.closeConnection();
            // TODO add items back to inventory? no info in interface doc cfr.
            // deleteReturnTransaction

            return false;
        }

        boolean isSuccess = ezShopDb.deleteSaleTransaction(transactionId);

        ezShopDb.closeConnection();

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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return null;

        SaleTransactionImpl saleTransaction = ezShopDb.getSaleTransaction(transactionId);
        if (saleTransaction.getStatus().equalsIgnoreCase("payed")) {
            ezShopDb.closeConnection();
            return null;
        }

        ezShopDb.closeConnection();

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


        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return -1;

        if (!(ezShopDb.getSaleTransaction(transactionId)).getStatus().equalsIgnoreCase("payed")) {
            ezShopDb.closeConnection();
            return -1;
        }

        Integer returnId = ezShopDb.newReturnTransactionId();
        ezShopDb.closeConnection();

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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;

        saleTransaction = ezShopDb.getSaleTransaction(activeReturnTransaction.getTransactionId());
        if (saleTransaction == null) {
            ezShopDb.closeConnection();
            return false;
        }

        List<TicketEntry> entries = saleTransaction.getEntries();
        if (!Utils.containsProduct(entries, productCode)) {
            ezShopDb.closeConnection();
            return false;
        }
        TicketEntryImpl ticketEntry =
                (TicketEntryImpl) Utils.getProductFromEntries(entries, productCode);
        if (ticketEntry.getAmount() < amount) {
            ezShopDb.closeConnection();
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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;

        boolean isSuccess = ezShopDb.insertReturnTransaction(activeReturnTransaction);
        conn = ezShopDb.closeConnection();

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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;

        if (ezShopDb.getReturnTransaction(returnId).getStatus().equalsIgnoreCase("payed")) {
            ezShopDb.closeConnection();
            return false;
        }

        boolean isSuccess = ezShopDb.deleteReturnTransaction(returnId);

        // TODO undo decrease saleTransaction items and total price and add items back to inventory


        ezShopDb.closeConnection();

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
        if (cash <= 0)
            throw new InvalidPaymentException();

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return -1;
        SaleTransactionImpl s = ezShopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezShopDb.closeConnection();
            return -1;
        }
        Double totalPrice = s.getPrice();
        if (cash < totalPrice) {
            ezShopDb.closeConnection();
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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;
        SaleTransactionImpl s = ezShopDb.getSaleTransaction(transactionID);
        if (s == null) {
            ezShopDb.closeConnection();
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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return -1;
        ReturnTransaction r = ezShopDb.getReturnTransaction(returnId);
        if (r == null) {
            ezShopDb.closeConnection();
            return -1;
        }
        if (!r.getStatus().equalsIgnoreCase("closed")) {
            ezShopDb.closeConnection();
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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return -1;
        ReturnTransaction r = ezShopDb.getReturnTransaction(returnId);
        if (r == null) {
            ezShopDb.closeConnection();
            return -1;
        }
        if (!r.getStatus().equalsIgnoreCase("closed")) {
            ezShopDb.closeConnection();
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

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return false;

        if (toBeAdded + computeBalance() < 0)
            return false;

        boolean isSuccess = ezShopDb.recordBalanceUpdate(toBeAdded);

        return isSuccess;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
            throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();

        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return new ArrayList<BalanceOperation>();

        List<BalanceOperation> list = ezShopDb.getAllBalanceOperations(from, to);

        return list;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (currentUser == null || (!currentUser.getRole().equalsIgnoreCase("administrator")
                && !currentUser.getRole().equalsIgnoreCase("shopmanager")))
            throw new UnauthorizedException();
        boolean conn = ezShopDb.createConnection();
        if (!conn)
            return -1;

        List<BalanceOperation> list = ezShopDb.getAllBalanceOperations(null, null);
        if (list.isEmpty())
            return -1;

        double balance = list.stream().mapToDouble(item -> item.getMoney()).sum();
        return balance;
    }
}

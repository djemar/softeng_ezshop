# Design Document

Authors: Diego Marino, Michele Massetti, Mohamed Shehab, Elisa Tedde

Date: 30/04/2021

Version: 1.0

# Contents

- [Design Document](#design-document)
- [Contents](#contents)
- [Instructions](#instructions)
- [High level design](#high-level-design)
- [Low level design](#low-level-design)
  - [EZShopModel](#ezshopmodel)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design

```plantuml
package EZShop <<Folder>>{

}
package EZShopGUI <<Folder>>{

}
package EZShopModel <<Folder>>{


}
package EZShopExceptions <<Folder>>{

}
EZShop <|-- EZShopModel
EZShop <|-- EZShopGUI
EZShop <|-- EZShopExceptions
```

Architectural pattern: MVC

# Low level design

```plantuml
scale 1400 width
note top of User : Instances are persistent in the db
note top of ProductType : Instances are persistent in the db
note top of SaleTransaction : Instances are persistent in the db once closed
note top of BalanceOperation : Instances are persistent in the db once closed
note top of ReturnTransaction : Instances are persistent in the db once closed
note top of Order : Instances are persistent in the db
note top of Customer : Instances are persistent in the db



class EZShop{
}

class EZShopData{
    + currentUser: User
    + activeReturnTransaction: ReturnTransaction
    + activeSaleTransaction: SaleTransaction
    + ezshopDb: EZShopDb
    +createUser(String username, String password, String role)
    +getAllUsers()
    +getUser(Integer id)
    +deleteUser(Integer id)
    +updateUserRights(Integer id, String role)
    +login(String username, String password)
    +logout()
    +createProductType(String description, String productCode, double pricePerUnit, String note)
    +updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
    +deleteProductType(Integer id)
    +getAllProductTypes()
    +getProductTypeByBarCode(String barCode)
    +getProductTypesByDescription(String description)
    +updateQuantity(Integer productId, int toBeAdded)
    +updatePosition(Integer productId, String newPos)
    +issueOrder(String productCode, int quantity, double pricePerUnit)
    +payOrderFor(String productCode, int quantity, double pricePerUnit)
    +payOrder(Integer orderId)
    +recordOrderArrival(Integer orderId)
    +getAllOrders()
    +defineCustomer(String customerName)
    +modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
    +deleteCustomer(Integer id)
    +getCustomer(Integer id)
    +getAllCustomers()
    +createCard()
    +attachCardToCustomer(String customerCard, Integer customerId)
    +modifyPointsOnCard(String customerCard, int pointsToBeAdded)
    +startSaleTransaction()
    +addProductToSale(Integer transactionId, String productCode, int amount)
    +deleteProductFromSale(Integer transactionId, String productCode, int amount)
    +applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    +applyDiscountRateToSale(Integer transactionId, double discountRate)
    +computePointsForSale(Integer transactionId)
    +closeSaleTransaction(Integer transactionId)
    +deleteSaleTransaction(Integer transactionId)
    +getSaleTransaction(Integer transactionId)
    +startReturnTransaction(Integer transactionId)
    +returnProduct(Integer returnId, String productCode, int amount)
    +endReturnTransaction(Integer returnId, boolean commit)
    +deleteReturnTransaction(Integer returnId)
    +receiveCashPayment(Integer transactionId, double cash)
    +receiveCreditCardPayment(Integer transactionId, String creditCard)
    +returnCashPayment(Integer returnId)
    +returnCreditCardPayment(Integer returnId, String creditCard)
    +recordBalanceUpdate(double toBeAdded)
    +getCreditsAndDebits(LocalDate from, LocalDate to)
    +computeBalance()
}

class EZShopDb{
    + public boolean createConnection()
    + public boolean closeConnection()
    + public Integer insertUser(UserImpl user)
    + public boolean deleteUser(Integer id)
    + public List<User> getAllUsers()
    + public UserImpl getUser(Integer id)
    + public boolean getUserbyName(String username)
    + public boolean updateUserRights(Integer id, String role)
    + public UserImpl checkCredentials(String username, String password)
    + public Integer insertProductType(ProductTypeImpl product)
    + public boolean updateProductType(Integer id, String newDescription, String newCode,
    + public ProductTypeImpl getProductTypeById(Integer id)
    + public boolean deleteProductType(Integer id)
    + public List<ProductType> getAllProductTypes()
    + public ProductTypeImpl getProductTypeByBarCode(String barCode)
    + public List<ProductType> getProductTypesByDescription(String descr)
    + public boolean updateQuantity(Integer productId, int toBeAdded)
    + public boolean updatePosition(Integer productId, String newPos)
    + public boolean checkExistingPosition(String position)
    + public Integer insertOrder(OrderImpl order)
    + public boolean resetTables()
    + public boolean resetDB()
    + public boolean updateOrder(int orderId, String status, int balanceid)
    + public OrderImpl getOrder(Integer orderId)
    + public Double getBalance()
    + public List<Order> getAllOrders()
    + public int insertBalanceOperation(BalanceOperationImpl b)
    + public Integer insertCustomer(Customer c)
    + public boolean updateCustomer(int id, String name, String fidelityCard, Integer points)
    + public int getCustomerCardNumber()
    + public boolean insertCustomerCard(String customerCard)
    + public CustomerImpl getCustomer(Integer id)
    + public boolean getCustomerCard(String customerCard)
    + public boolean attachCardToCustomer(String customerCard, Integer customerId)
    + public List<Customer> getAllCustomers()
    + public boolean deleteCustomer(CustomerImpl c)
    + public CustomerImpl getCustomerByCard(String customerCard)
    + public int SaleTransactionNumber()
    + public boolean insertSaleTransaction(SaleTransactionImpl saleTransaction)
    + public boolean payForSaleTransaction(Integer transactionID)
    + public boolean setSaleDiscount(Integer transactionId, Double discountRate)
    + public boolean updateSaleTransaction(Integer transactionId,
    + public boolean deleteSaleTransaction(Integer transactionId)
    + public SaleTransactionImpl getSaleTransaction(Integer transactionId)
    + public Integer newReturnTransactionId()
    + public boolean insertReturnTransaction(ReturnTransaction returnTransaction)
    + public boolean deleteReturnTransaction(Integer returnId)
    + public ReturnTransaction getReturnTransaction(Integer returnId)
    + public boolean payForReturnTransaction(Integer returnID)
    + public boolean recordBalanceUpdate(double toBeAdded)
    + public List<BalanceOperation> getAllBalanceOperations(LocalDate from, LocalDate to)
}

class EZShopData implements EZShopDataInterface

class User{
    -username : String
    -password : String
    -role : String
    -Id : Integer
}

class ProductType{
    -description : String
    -productCode : String
    -pricePerUnit : Double
    -note : String
    -Id : Integer
    -quantity : Integer
    -position : String
}

class Customer{
    -Id : Integer
    -name : String
    -card : String
    -points : Integer

}
class Order{
    -pricePerUnit : Double
    -quantity : Integer
    -status : String
    -orderId : Integer
    -productCode: String
    -balanceId: Integer
}

class SaleTransaction{
    -id : Integer
    -discountRate : Double
    -price : Double
    -status : String
    -ticketList : List<TicketEntry>
}

class BalanceOperation{
    -type : String
    -money : Double
    -date : Date
    -Id : Integer
}

class ReturnTransaction{
    -quantity : Integer
    -status : String
    -productcode : String
    -transactionId : Integer
    -returnId: Integer
    -total : double
    -returnProductMap : Map<String, Integer>
}
class TicketEntry{
    -amount : Integer
    -productcode : String
    -productdescription : String
    -priceperunit : double
    -discountrate : double
}


EZShopData --"*" Customer
EZShopData "*" -- SaleTransaction
EZShop -- EZShopData
User "*"-- EZShopData
EZShopData -- ProductType
EZShopData -- EZShopDB
EZShopData -- ReturnTransaction
BalanceOperation -- EZShopData
BalanceOperation -- ReturnTransaction
BalanceOperation -- SaleTransaction
BalanceOperation -- Order
SaleTransaction --"*" TicketEntry
SaleTransaction --"*" ReturnTransaction
TicketEntry "*" --  ProductType
ReturnTransaction "*"  --"*" ProductType
Order "*"--"*" ProductType
```

Design pattern: Facade

# Verification traceability matrix

| FR  | EZShop | User | EZShopDB | ProdType | Customer | Order | ReturnTrans | SaleTrans | TicketEntry | BalanceOp |
| --- | :----: | :--: | :------: | :------: | :------: | :---: | :---------: | :-------: | :---------: | :-------: |
| FR1 |   x    |  x   |    x     |          |          |       |             |           |             |           |
| FR3 |   x    |      |    x     |    x     |          |       |             |           |             |           |
| FR4 |   x    |      |    x     |    x     |          |   x   |             |           |             |     x     |
| FR5 |   x    |      |    x     |          |    x     |       |             |           |             |           |
| FR6 |   x    |      |    x     |    x     |    x     |       |      x      |     x     |      x      |     x     |
| FR7 |   x    |      |    x     |    x     |    x     |       |      x      |     x     |      x      |     x     |
| FR8 |   x    |      |    x     |          |          |       |             |           |             |     x     |

# Verification sequence diagrams

Scenario 1-1 : Create product type X

```plantuml
"User" -> "EZShopData": 1. wants to create a new Product
"EZShopData" -> "EZShopDB": 2. createProductType(String description, String productCode, double pricePerUnit, String note)
"User" -> "EZShopData": 3. wants to set the Position
"EZShopData" -> "EZShopDB": 4. updatePosition(Integer productId, String newPos)
```

Scenario 1-2 : Modify product type location

```plantuml
"Employee" -> "EZShopData": 1. scans the Product
"EZShopData" -> "EZShopDB" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "EZShopData": 3. wants to set a new free Position
"EZShopData" -> "EZShopDB": 4. updatePosition(Integer productId, String newPos)
```

Scenario 1-3 : Modify product type price per unit

```plantuml
"Employee" -> "EZShopData": 1. scans the Product
"EZShopData" -> "EZShopDB" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "EZShopData": 3. wants to set a new Price
"EZShopData" -> "EZShopDB": 4. updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
"EZShopData" -> "Employee": 5. Confirmation message is displayed
```

Scenario 2-1 : Create user and define rights

```plantuml
"Administrator" -> "EZShopData": 1. wants to create a new Account
"EZShopData" -> "EZShopDB": 2. createUser(String username, String password, String role)
"Administrator" -> "EZShopData": 3. wants to set access rights
"EZShopData" -> "EZShopDB": 4. updateUserRights(Integer id, String role)
```

Scenario 2-2: Delete user

```plantuml
"Administrator" -> "EZShopData": 1. wants to delete user account
"EZShopData" -> "EZShopDB": 2. deleteUser(Integer id)

```

Scenario 2-3: Modify user rights

```plantuml
"Administrator" -> "EZShopData": 1. wants to select user account A
"EZShopData" -> "EZShopDB": 2. getUser(Integer id)
"Administrator" -> "EZShopData": 3. wants to modify access rights
"EZShopData" -> "EZShopDB": 4. updateUserRights(Integer id, String role)
```

Scenario 3-1: Order of product type X issued

```plantuml
"ShopManager" -> "EZShopData": 1. wants to order a new Product
"EZShopData" -> "EZShopDB" : 2. issueReorder(String productCode, int quantity, double pricePerUnit)

```

Scenario 3-2: Order of product type X payed

```plantuml
"ShopManager" -> "EZShopData": 1. wants to pay the Order O
"EZShopData" -> "EZShopDB" : 2. payOrder(Integer orderId)
"EZShopData" -> "EZShopDB" : 3. computeBalace()
```

Scenario 3-3: Record order of product type X arrival

```plantuml
"ShopManager" -> "EZShopData": 1. wants to record Order O's arrival
"EZShopData" -> "EZShopDB" : 2. recordOrderArrival(Integer orderId)
"EZShopData" -> "EZShopDB" : 2. setStatus(String status)
"EZShopData" -> "EZShopDB" : 3. updateQuantity(Integer productId, int toBeAdded)
```

Scenario 4-1: Create customer record

```plantuml
"User" -> "Customer": 1. User asks Customer personal data
"User" -> "EZShopData" : 2. User wants to create a new User account
"EZShopData" -> "EZShopDB" : 3. defineCustomer(String customerName)

```

Scenario 4-2: Attach Loyalty card to customer record

```plantuml
"User" -> "EZShopData" : 1. User wants to attach Loyalty Card to Customer record
"EZShopData" -> "EZShopDB" : 2. createCard()
"EZShopData" -> "EZShopDB" : 3. attachCardToCustomer(String customerCard, Integer customerId)

```

Scenario 4-3: Update customer record

```plantuml
"User" -> "EZShopData" : 1. User wants to update customer record
"EZShopData" -> "EZShopDB" : 2. modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)

```

Scenario 5-1: Login

```plantuml
"User" -> "EZShopData" : 1. User inserts his surname and password
"EZShopData" -> "EZShopDB" : 2. login(String username, String password)

```

Scenario 5-2: Logout

```plantuml
"User" -> "EZShopData" : 1. User wants to log out
"EZShopData" -> "EZShopDB" : 2. logout()

```

Scenario 6-1: Sale of product type X completed

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 7.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "EZShopDB" : 8.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "EZShopDB" : 9.   Update balance (see Scenarios 7.*)

```

Scenario 6-2: Sale of product type X with product discount

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier applies a product discount rate
"EZShopData" -> "EZShopDB" : 7.   applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
"Cashier" -> "EZShopData" : 8. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 9.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "EZShopDB" : 10.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "EZShopDB" : 11.   Update balance (see Scenarios 7.*)

```

Scenario 6-3: Sale of product type X with sale discount

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier applies a sale discount rate
"EZShopData" -> "EZShopDB" : 7.   applyDiscountRateToSale(Integer transactionId, double discountRate)
"Cashier" -> "EZShopData" : 8. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 9.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "EZShopDB" : 10.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "EZShopDB" : 11.   Update balance (see Scenarios 7.*)

```

Scenario 6-4: Sale of product type X with Loyalty Card update

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 7.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "EZShopDB" : 8.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "EZShopDB" : 9. modifyPointsOnCard(String customerCard, int pointsToBeAdded)
"EZShopData" -> "EZShopDB" : 10.   Update balance (see Scenarios 7.*)

```

Scenario 6-5: Sale of product type X cancelled

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 7.   endSaleTransaction(Integer transactionId)
"Customer "-> "Cashier" : 8. asks Cashier to cancels the payment
"EZShopData" -> "EZShopDB" : 9. deleteSaleTransaction(Integer transactionId)

```

Scenario 6-6: Sale of product type X with product discount

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "EZShopDB" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "EZShopDB" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "EZShopDB" : 7.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "EZShopDB" : 8. Manage cash payment(see Scenarios 7.*)
"EZShopData" -> "EZShopDB" : 9.   Update balance (see Scenarios 7.*)

```

Scenario 7-1: Manage payment by valid credit card

```plantuml
"Employee" -> "EZShopData" : 1. Validates Credit Card number with Luhn algorithm
"EZShopData" -> "EZShopDB" : 2. receiveCreditCardPayment(Integer transactionId, String creditCard)
"EZShopData" -> "EZShopDB" : 3. recordBalanceUpdate(double toBeAdded)
"EZShopData" -> "Employee" : 4. Success Message is displayed
```

Scenario 7-2: Manage payment by invalid credit card

```plantuml
"Employee" -> "EZShopData" : 1. Validates Credit Card number with Luhn algorithm
"EZShopData" -> "Employee" : 2. Error message is displayed, Invalid Card
```

Scenario 7-3: Manage credit card payment with not enough credit

```plantuml
"Employee" -> "EZShopData" : 1. Validates Credit Card number with Luhn algorithm
"EZShopData" -> "Employee" : 2. Error message is displayed, not Enough Credit
```

Scenario 7-4: Manage cash payment

```plantuml
"Employee" -> "EZShopData" : 1. Collects banknotes and coins
"EZShopData" -> "EZShopDB" : 2. returnCashPayment(Integer returnId)
"EZShopData" -> "EZShopDB" : 3. recordBalanceUpdate(double toBeAdded)
"Employee" -> "Customer" : 4. Returns change
```

Scenario 8-1: Return transaction of product type X completed, credit card

```plantuml
"Cashier" -> "EZShopData" : 1. Start Return Transaction

"EZShopData" -> "EZShopDB": 2. startReturnTransaction(Integer transactionId)
"Cashier"->  "EZShopData": 3. reads Bar Code of X
"EZShopData" -> "EZShopDB": 4. returnProduct(Integer returnId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5. updateQuantity(Integer productId, int toBeAdded)
"EZShopData" -> "EZShopDB": 7. endReturnTransaction(Integer returnId, boolean commit)
"EZShopData" -> "Employee" : 8. End Return Transaction

```

Scenario 8-2: Return transaction of product type X completed, cash

```plantuml
"Cashier" -> "EZShopData" : 1. Start Return Transaction

"EZShopData" -> "EZShopDB": 2. startReturnTransaction(Integer transactionId)
"Cashier"->  "EZShopData": 3. reads Bar Code of X
"EZShopData" -> "EZShopDB":4. returnProduct(Integer returnId, String productCode, int amount)
"EZShopData" -> "EZShopDB" : 5. updateQuantity(Integer productId, int toBeAdded)
"EZShopData" -> "EZShopDB":6. Manage cash return
"EZShopData"-> "EZShopDB" : 7. endReturnTransaction(Integer returnId, boolean commit)
"EZShopData" -> "Cashier" : 8. End Return Transaction

```

Scenario 9-1: List credits and debits

```plantuml
"Manager" -> "EZShopData" : 1. selects Start Date and End Date   
"EZShopData" -> "EZShopDB" : 2. getCreditsAndDebits(LocalDate from, LocalDate to)
"EZShopData" -> "Manager" : 3. list of transactions is returned


```

Scenario 10-1: Return payment by credit card

```plantuml
"Employee" -> "EZShopData" : 1. Start Payment Return
"EZShopData" -> "EZShopDB": 2. returnCreditCardPayment(Integer returnId, String creditCard)
"EZShopData" -> "Employee" : 3. End Return Transaction

```

Scenario 10-2: Return payment by cash

```plantuml
"Employee" -> "EZShopData" : 1. Start Payment Return
"EZShopData" -> "EZShopDB": 2. returnCashPayment(Integer returnId)
"EZShopData" -> "Employee" :  3. End Return Transaction and Rest is emitted

```

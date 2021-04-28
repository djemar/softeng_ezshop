# Design Document

Authors: Diego Marino, Michele Massetti, Mohamed Shehab, Elisa Tedde

Date: 30/04/2021

Version: 1.0

# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements
#AGGIUNGERE EXCEPTIONS in high level?
#AGGIUNGERE diritti di ciascun ruolo?
# High level design

<discuss architectural styles used, if any>
<report package diagram>


```plantuml
package EZShop <<Folder>>{

}
package EZShopGUI <<Folder>>{

}
package EZShopData <<Folder>>{

}
EZShop <|-- EZShopData
EZShop <|-- EZShopGUI
```

# Low level design

<for each package, report class diagram>

```plantuml

class EZShop{
    +currentUser: User
    +login(String username, String password)
    +logout()

}
class User{
    +username : String
    +password : String
    +role : String
    +Id : Integer
    +updateUserRights(String role)
}


class DataImplementation{
    +UserMap : Map<Integer,User>
    +CustomerMap : Map<Integer,Customer>
    +EZShop : EZShop
    +ProductMap : Map<String,ProductType>
    +PositionSet : Set <Position>
    +CardMap : Map <String,FidelityCard>
    +SaleMap : <Integer,Ticket>
    +ReturnMap: <Integer,Return>
    +CreditMap:<Integer,Credit>
    +DebitMap:<Integer,Debit>
    +BalanceOperationMap: <Integer,BalanceOperation>
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
    +issueReorder(String productCode, int quantity, double pricePerUnit)
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
    +deleteSaleTicket(Integer ticketNumber)
    +getSaleTicket(Integer transactionId)
    +getTicketByNumber(Integer ticketNumber)
    +startReturnTransaction(Integer ticketNumber)
    +returnProduct(Integer returnId, String productCode, int amount)
    +endReturnTransaction(Integer returnId, boolean commit)
    +deleteReturnTransaction(Integer returnId)
    +receiveCashPayment(Integer ticketNumber, double cash)
    +receiveCreditCardPayment(Integer ticketNumber, String creditCard)
    +returnCashPayment(Integer returnId)
    +returnCreditCardPayment(Integer returnId, String creditCard)
    +recordBalanceUpdate(double toBeAdded)
    +getCreditsAndDebits(LocalDate from, LocalDate to)
    +computeBalance()
}
class ProductType{
    +description : String
    +productCode : String
    +pricePerUnit : Double
    +note : String
    +Id : Integer
    +quantity : Integer
    +position : Position
    +updateQuantity(toBeAdded)
}
class Position{
    +aisleNumber : Integer
    +rackAlphabeticIdentifier : String
    +levelNumber : Integer
}
class FidelityCard{
    +cardCode : String
    +points : Integer
    +customerAttached : Customer
}
class Customer{
    +Id : Integer
    +name : String
    +card : FidelityCard
    
}
class Order{
    +pricePerUnit : Double
    +quantity : Integer
    +productType : String
    +status : String
}

class Debit{

}
class Credit{

}

class SaleTransaction{
    +SaleArray : ArrayList <SaleItem>
    +discountRate : Double
    +total : Double
    +status : String
    +addProductToSale(String productCode, int amount) 
    +deleteProductFromSale(String productCode, int amount)
    +applyDiscountRateToProduct(String productCode, double discountRate)
    +computePointsForSale()
}
class SaleItem{
    +product : String
    +discount : Double
    +amount: int
}
class BalanceOperation{
    +description : String
    +amount : Double
    +date : Date
    +Id : Integer
}

class ReturnTransaction{
    +quantiy : Integer
    +returnedValue: Integer
    +productcode: String
}

class AccountBook{

    
    +BalanceOperationMap : Map<Integer,BalanceOperation>
    +issueOrder(String productCode, int quantity, double pricePerUnit)
    +payOrderFor(String productCode, int quantity, double pricePerUnit)
    +payOrder(Integer orderId)
    
    +recordOrderArrival(Integer orderId)
    +getAllOrders()
    +computeBalance()
    +startSaleTransaction()
    +addProductToSale(Integer transactionId, String productCode, int amount) 
    +deleteProductFromSale(Integer transactionId, String productCode, int amount) 
    +applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    + applyDiscountRateToSale(Integer transactionId, double discountRate)
    +computePointsForSale(Integer transactionId)
    +endSaleTransaction(Integer transactionId)
    + deleteSaleTransaction(Integer transactionId)
    +startReturnTransaction(Integer transactionId)
    +returnProduct(Integer returnId, String productCode, int amount)
    +endReturnTransaction(Integer returnId, boolean commit) 
    +deleteReturnTransaction(Integer returnId)
    +receiveCashPayment(Integer transactionId, double cash)
    +receiveCreditCardPayment(Integer transactionId, String creditCard)
    +creditCardValidity(String creditCard)
    +recordBalanceUpdate(double toBeAdded) 

}

DataImplementation --"*" Customer
DataImplementation --"*" FidelityCard
ReturnTransaction "*" -- SaleTransaction
EZShop -- User
User "*"-- DataImplementation
DataImplementation -- ProductType
ProductType -- Position
FidelityCard -- Customer
FidelityCard -- SaleTransaction
SaleTransaction-- SaleItem
AccountBook -- DataImplementation
BalanceOperation <|-- Credit
Debit <|-- ReturnTransaction
Debit <|-- Order
BalanceOperation <|-- Debit
AccountBook -- BalanceOperation
Credit <|-- SaleTransaction
SaleItem "*" --   ProductType
ReturnTransaction "*"  --"*" ProductType

```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

| FR  | EZShop | User | DataImpl | ProdT | Posit | FidelityC | Customer | Order | Return | Debit | Credit | ReturnTrans | SaleTran | SaleIt | BalanOp | AccBook |
| --- | :----: | :--: | :------: | :---: | :---: | :-------: | :------: | :---: | :----: | :---: | :----: | :---------: | :------: | :----: | :-----: | :-----: |
| FR1 |   x    |  x   |    x     |       |       |           |          |       |        |       |        |             |          |        |         |         |
| FR2 |        |      |          |       |       |           |          |       |        |       |        |             |          |        |         |         |
| FR3 |   x    |  x   |    x     |   x   |       |           |          |       |        |       |        |             |          |        |         |         |
| FR4 |   x    |  x   |    x     |   x   |   x   |           |          |   x   |   x    |   x   |        |             |          |        |    x    |         |
| FR5 |   x    |  x   |    x     |       |       |     x     |    x     |       |        |       |        |             |    x     |        |         |    x    |
| FR6 |   x    |  x   |    x     |   x   |       |     x     |          |       |   x    |   x   |   x    |      x      |    x     |   x    |    x    |    x    |
| FR7 |   x    |  x   |    x     |   x   |       |           |          |       |        |   x   |   x    |             |          |        |         |    x    |
| FR8 |   x    |  x   |    x     |       |       |           |          |       |        |   x   |   x    |             |          |        |    x    |    x    |

# Verification sequence diagrams

\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

Scenario 1-1 : Create product type X

```plantuml
"User" -> "DataImpl": 1. wants to create a new Product
"DataImpl" -> "ProductType": 2. createProductType(String description, String productCode, double pricePerUnit, String note)
"User" -> "DataImpl": 3. wants to set the Position
"DataImpl" -> "ProductType": 4. updatePosition(Integer productId, String newPos)
```

Scenario 1-2 : Modify product type location

```plantuml
"Employee" -> "DataImpl": 1. scans the Product
"DataImpl" -> "ProductType" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "DataImpl": 3. wants to set a new free Position
"DataImpl" -> "ProductType": 4. updatePosition(Integer productId, String newPos)
```
Scenario 1-3 : Modify product type price per unit

```plantuml
"Employee" -> "DataImpl": 1. scans the Product
"DataImpl" -> "ProductType" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "DataImpl": 3. wants to set a new Price
"DataImpl" -> "ProductType": 4. updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
"DataImpl" -> "Employee": 5. Confirmation message is displayed
```

Scenario 2-1 : Create user and define rights

```plantuml
"Administrator" -> "DataImpl": 1. wants to create a new Account
"DataImpl" -> "User": 2. createUser(String username, String password, String role)
"Administrator" -> "DataImpl": 3. wants to set access rights 
"DataImpl" -> "User": 4. updateUserRights(Integer id, String role)
```

Scenario 2-2: Delete user

```plantuml
"Administrator" -> "DataImpl": 1. wants to delete user account
"DataImpl" -> "User": 2. deleteUser(Integer id)

```
Scenario 2-3: Modify user rights

```plantuml
"Administrator" -> "DataImpl": 1. wants to select user account A
"DataImpl" -> "User": 2. getUser(Integer id)
"Administrator" -> "DataImpl": 3. wants to modify access rights 
"DataImpl" -> "User": 4. updateUserRights(Integer id, String role)
```
Scenario 3-1: Order of product type X issued

```plantuml
"ShopManager" -> "DataImpl": 1. wants to order a new Product
"DataImpl" -> "Order" : 2. issueReorder(String productCode, int quantity, double pricePerUnit)
"Order" -> "Order" : 3. setStatus(String status)
```
Scenario 3-2: Order of product type X payed

```plantuml
"ShopManager" -> "DataImpl": 1. wants to pay the Order O
"DataImpl" -> "Order" : 2. payOrder(Integer orderId)
"Order" -> "Order" : 3. setStatus(String status)
"DataImpl" -> "AccountBook" : 4. computeBalace()
```
Scenario 3-3: Record order of product type X arrival

```plantuml
"DataImpl" -> "Order" : 1. recordOrderArrival(Integer orderId) 
"Order" -> "Order" : 2. setStatus(String status)
"DataImpl" -> "ProductType" : 3. updateQuantity(Integer productId, int toBeAdded)
```

Scenario 4-1: Create customer record

```plantuml
"User" -> "Customer": 1. User asks Customer personal data
"User" -> "DataImpl" : 2. User wants to create a new User account
"DataImpl" -> "Customer" : 3. defineCustomer(String customerName)

```
Scenario 4-2: Attach Loyalty card to customer record

```plantuml
"User" -> "DataImpl" : 1. User wants to attach Loyalty Card to Customer record
"DataImpl" -> "FidelityCard" : 2. createCard()
"DataImpl" -> "FidelityCard" : 3. attachCardToCustomer(String customerCard, Integer customerId)

```
Scenario 4-3: Update customer record

```plantuml
"User" -> "DataImpl" : 1. User wants to update customer record
"DataImpl" -> "Customer" : 2. modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)

```
Scenario 5-1: Login

```plantuml
"User" -> "DataImpl" : 1. User inserts his surname and password
"DataImpl" -> "EZShop" : 2. login(String username, String password)
"EZShop" -> "DataImpl": 3. shows the functionalities offered by the access priviledges of User

```
Scenario 5-1: Logout

```plantuml
"User" -> "DataImpl" : 1. User wants to log out
"DataImpl" -> "EZShop" : 2. logout()

```
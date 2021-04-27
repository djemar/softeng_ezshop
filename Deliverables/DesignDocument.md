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

# High level design

<discuss architectural styles used, if any>
<report package diagram>
#AGGIUNGERE EXCEPTIONS?

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

}
class User{
    +username : String
    +password : String
    +role : String
    +Id : Integer
}


class DataImplementation{
    +UserAccountMap : User
    +CustomerAccountMap : Customer
    +EZShop
    +ProductMap : Product
    +PositionArray : Position
    +OrderMap : <Integer,Order>
    +CardMap : <String,FidelityCard>
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
    +SaleArray : SaleItem
    +discountRate : Double
    +total : Double
    +time : Time
    +date : Date
    +status : String
}
class SaleItem{
    +product : String
    +discount : Double
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
    
    +computeBalance()

}

ReturnTransaction "*" -- SaleTransaction
EZShop -- User
User -- DataImplementation
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
"User" -> "EZShop": 1. createProductType(String description, String productCode, double pricePerUnit, String note)
"EZShop" -> "ProductType" : 2. new ProductType(String description, String productCode, double pricePerUnit, String note)
"User" -> "EZShop": 3. updatePosition(Integer productId, String newPos)
"EZShop" -> "ProductType" : 4. setPosition(String newPos)
```

Scenario 2-1 : Create user and define rights

```plantuml
"Administrator" -> "EZShop": 1. createUser(String username, String password, String role)
"EZShop" -> "User": 2. new User(String username, String password, String role)
```

Scenario 2-3: Delete user

```plantuml
"EZShop" -> "Administrator": 1. getUser(Integer id)
"Administrator" -> "EZShop": 2. updateUserRights(Integer id, String role)
"EZShop" -> "User": 3. setUserRights(String role)
```

Scenario 3-1: Order of product type X issued

```plantuml
"ShopManager" -> "EZShop" : issueReorder(String productCode, int quantity, double pricePerUnit)
"EZShop" -> "Order" : new Order(String productCode, int quantity, double pricePerUnit)
"EZShop" -> "Order" : setStatus(String status)
```

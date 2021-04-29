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

<discuss architectural styles used, if any>
<report package diagram>

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

# Low level design

## EZShopModel

```plantuml
scale 1600 width
note top of User : Instances are persistent in the db
note top of ProductType : Instances are persistent in the db
note top of SaleTransaction : Instances are persistent in the db once closed
note top of ReturnTransaction : Instances are persistent in the db once closed
note top of Order : Instances are persistent in the db
note top of Customer : Instances are persistent in the db
note top of FidelityCard : Instances are persistent in the db
note top of Position : Instances are persistent in the db

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

class EZShopData{
    +UserMap : Map<Integer,User>
    +CustomerMap : Map<Integer,Customer>
    +ProductMap : Map<String,ProductType>
    +PositionSet : Set <Position>
    +CardMap : Map <String,FidelityCard>
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

class EZShopData implements EZShopDataInterface

class ProductType{
    +description : String
    +productCode : String
    +pricePerUnit : Double
    +note : String
    +Id : Integer
    +quantity : Integer
    +position : Position
    +updateQuantity(int toBeAdded)
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
    +computeAmount()
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
    +numberOfTransactions: Integer
    +balanceOperationMap : Map<Integer,BalanceOperation>
    +balance: double
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


EZShopData --"*" Customer
EZShopData --"*" FidelityCard
ReturnTransaction "*" -- SaleTransaction
EZShop -- EZShopData
User "*"-- EZShopData
EZShopData -- ProductType
ProductType -- Position
FidelityCard "0..1"-- Customer
FidelityCard "0..1"--"*" SaleTransaction
SaleTransaction --"*" SaleItem
AccountBook -- EZShopData
BalanceOperation <|-- Credit
Debit <|-- ReturnTransaction
Debit <|-- Order
BalanceOperation <|-- Debit
AccountBook -- BalanceOperation
Credit <|-- SaleTransaction
SaleItem "*" --   ProductType
ReturnTransaction "*"  --"*" ProductType
Order "*"--"*" ProductType
```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

| FR  | EZShop | User | EZShopData | ProdT | Posit | FidelityC | Customer | Order | Return | Debit | Credit | ReturnTrans | SaleTran | SaleIt | BalanOp | AccBook |
| --- | :----: | :--: | :--------: | :---: | :---: | :-------: | :------: | :---: | :----: | :---: | :----: | :---------: | :------: | :----: | :-----: | :-----: |
| FR1 |   x    |  x   |     x      |       |       |           |          |       |        |       |        |             |          |        |         |         |
| FR2 |        |      |            |       |       |           |          |       |        |       |        |             |          |        |         |         |
| FR3 |   x    |  x   |     x      |   x   |       |           |          |       |        |       |        |             |          |        |         |         |
| FR4 |   x    |  x   |     x      |   x   |   x   |           |          |   x   |   x    |   x   |        |             |          |        |    x    |         |
| FR5 |   x    |  x   |     x      |       |       |     x     |    x     |       |        |       |        |             |    x     |        |         |    x    |
| FR6 |   x    |  x   |     x      |   x   |       |     x     |          |       |   x    |   x   |   x    |      x      |    x     |   x    |    x    |    x    |
| FR7 |   x    |  x   |     x      |   x   |       |           |          |       |        |   x   |   x    |             |          |        |         |    x    |
| FR8 |   x    |  x   |     x      |       |       |           |          |       |        |   x   |   x    |             |          |        |    x    |    x    |

# Verification sequence diagrams

\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

Scenario 1-1 : Create product type X

```plantuml
"User" -> "EZShopData": 1. wants to create a new Product
"EZShopData" -> "ProductType": 2. createProductType(String description, String productCode, double pricePerUnit, String note)
"User" -> "EZShopData": 3. wants to set the Position
"EZShopData" -> "ProductType": 4. updatePosition(Integer productId, String newPos)
```

Scenario 1-2 : Modify product type location

```plantuml
"Employee" -> "EZShopData": 1. scans the Product
"EZShopData" -> "ProductType" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "EZShopData": 3. wants to set a new free Position
"EZShopData" -> "ProductType": 4. updatePosition(Integer productId, String newPos)
```

Scenario 1-3 : Modify product type price per unit

```plantuml
"Employee" -> "EZShopData": 1. scans the Product
"EZShopData" -> "ProductType" : 2. getProductTypeByBarCode(String barCode)
"Employee" -> "EZShopData": 3. wants to set a new Price
"EZShopData" -> "ProductType": 4. updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
"EZShopData" -> "Employee": 5. Confirmation message is displayed
```

Scenario 2-1 : Create user and define rights

```plantuml
"Administrator" -> "EZShopData": 1. wants to create a new Account
"EZShopData" -> "User": 2. createUser(String username, String password, String role)
"Administrator" -> "EZShopData": 3. wants to set access rights
"EZShopData" -> "User": 4. updateUserRights(Integer id, String role)
```

Scenario 2-2: Delete user

```plantuml
"Administrator" -> "EZShopData": 1. wants to delete user account
"EZShopData" -> "User": 2. deleteUser(Integer id)

```

Scenario 2-3: Modify user rights

```plantuml
"Administrator" -> "EZShopData": 1. wants to select user account A
"EZShopData" -> "User": 2. getUser(Integer id)
"Administrator" -> "EZShopData": 3. wants to modify access rights
"EZShopData" -> "User": 4. updateUserRights(Integer id, String role)
```

Scenario 3-1: Order of product type X issued

```plantuml
"ShopManager" -> "EZShopData": 1. wants to order a new Product
"EZShopData" -> "Order" : 2. issueReorder(String productCode, int quantity, double pricePerUnit)
"Order" -> "Order" : 3. setStatus(String status)
```

Scenario 3-2: Order of product type X payed

```plantuml
"ShopManager" -> "EZShopData": 1. wants to pay the Order O
"EZShopData" -> "Order" : 2. payOrder(Integer orderId)
"Order" -> "Order" : 3. setStatus(String status)
"EZShopData" -> "AccountBook" : 4. computeBalace()
```

Scenario 3-3: Record order of product type X arrival

```plantuml
"ShopManager" -> "EZShopData": 1. wants to record Order O's arrival
"EZShopData" -> "Order" : 1. recordOrderArrival(Integer orderId)
"Order" -> "Order" : 2. setStatus(String status)
"EZShopData" -> "ProductType" : 3. updateQuantity(Integer productId, int toBeAdded)
```

Scenario 4-1: Create customer record

```plantuml
"User" -> "Customer": 1. User asks Customer personal data
"User" -> "EZShopData" : 2. User wants to create a new User account
"EZShopData" -> "Customer" : 3. defineCustomer(String customerName)

```

Scenario 4-2: Attach Loyalty card to customer record

```plantuml
"User" -> "EZShopData" : 1. User wants to attach Loyalty Card to Customer record
"EZShopData" -> "FidelityCard" : 2. createCard()
"EZShopData" -> "FidelityCard" : 3. attachCardToCustomer(String customerCard, Integer customerId)

```

Scenario 4-3: Update customer record

```plantuml
"User" -> "EZShopData" : 1. User wants to update customer record
"EZShopData" -> "Customer" : 2. modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)

```

Scenario 5-1: Login

```plantuml
"User" -> "EZShopData" : 1. User inserts his surname and password
"EZShopData" -> "EZShop" : 2. login(String username, String password)
"EZShop" -> "EZShopData": 3. shows the functionalities offered by the access priviledges of User

```

Scenario 5-2: Logout

```plantuml
"User" -> "EZShopData" : 1. User wants to log out
"EZShopData" -> "EZShop" : 2. logout()

```

Scenario 6-1: Sale of product type X completed

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "AccountBook" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "AccountBook" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "ProductType" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "AccountBook" : 7.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "AccountBook" : 8.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "AccountBook" : 9.   Update balance (see Scenarios 7.*)

```

Scenario 6-2: Sale of product type X with product discount

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "AccountBook" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "AccountBook" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "ProductType" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier applies a discount rate
"EZShopData" -> "ProductType" : 7.   applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
"Cashier" -> "EZShopData" : 8. Cashier closes sale transaction
"EZShopData" -> "AccountBook" : 9.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "AccountBook" : 10.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "AccountBook" : 11.   Update balance (see Scenarios 7.*)

```

Scenario 6-3: Logout

```plantuml
"User" -> "EZShopData" : 1. User wants to log out
"EZShopData" -> "EZShop" : 2. logout()

```

Scenario 6-1: Sale of product type X with Loyalty Card update

```plantuml
"Cashier" -> "EZShopData" : 1. Cashier starts a new sale transaction
"EZShopData" -> "AccountBook" : 2.  startSaleTransaction()
"Cashier" -> "EZShopData" : 3. Cashier adds a product to sale
"EZShopData" -> "AccountBook" : 4.   addProductToSale(Integer transactionId, String productCode, int amount)
"EZShopData" -> "EZShopData" : 5.   updateQuantity(Integer productId, int toBeAdded)
"Cashier" -> "EZShopData" : 6. Cashier closes sale transaction
"EZShopData" -> "AccountBook" : 7.   endSaleTransaction(Integer transactionId)
"EZShopData" -> "AccountBook" : 8.   Manage payment (see Scenarios 7.*)
"EZShopData" -> "AccountBook" : 9.   Update balance (see Scenarios 7.*)

```

Scenario 6-5: Logout

```plantuml
"User" -> "EZShopData" : 1. User wants to log out
"EZShopData" -> "EZShop" : 2. logout()

```

Scenario 6-6: Logout

```plantuml
"User" -> "EZShopData" : 1. User wants to log out
"EZShopData" -> "EZShop" : 2. logout()

```

Scenario 7-1: Manage payment by valid credit card

```plantuml
"Employee" -> "EZShopData" : 1. Validates Credit Card number with Luhn algorithm
"EZShopData" -> "AccountBook" : 2. receiveCreditCardPayment(Integer transactionId, String creditCard)
"EZShopData" -> "AccountBook" : 3. recordBalanceUpdate(double toBeAdded)
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
"EZShopData" -> "Employee" : 2. returnCashPayment(Integer returnId)
"EZShopData" -> "AccountBook" : 3. recordBalanceUpdate(double toBeAdded)
"Employee" -> "Customer" : 4. Returns change
```

Scenario 8-1: Return transaction of product type X completed, credit card

```plantuml
"Cashier" -> "EZShopData" : start return transact

"EZShopData" -> "AccountBook": 1.startReturnTransaction(Integer transactionId)
"Cashier"->  "EZShopData": reads bar code of X
"EZShopData" -> "AccountBook":2 returnProduct(Integer returnId, String productCode, int amount)
"EZShopData" -> "AccountBook":3 Manage cash return
"EZShopData" -> "AccountBook": 3  endReturnTransaction(Integer returnId, boolean commit)
"EZShopData" -> "Employee" : end return transaction

```

Scenario 8-2: Return transaction of product type X completed, cash

```plantuml
"Cashier" -> "EZShopData" : start return transact

"EZShopData" -> "AccountBook": 1.startReturnTransaction(Integer transactionId)
"Cashier"->  "EZShopData": reads bar code of X
"EZShopData" -> "AccountBook":2 returnProduct(Integer returnId, String productCode, int amount)
"EZShopData" -> "AccountBook":3 Manage cash return
"AccountBook"-> "EZShopData" : 4  endReturnTransaction(Integer returnId, boolean commit)
"EZShopData" -> "Cashier" : end return transaction

```

Scenario 9-1: List credits and debits

```plantuml
"" -> "" : 1.
"" -> "" : 2.
"" -> "" : 3.
"" -> "" : 4.

```

Scenario 10-1: Return payment by credit card

```plantuml
"Employee" -> "EZShopData" : start payment return


"EZShopData" -> "AccountBook": 1. returnCreditCardPayment(Integer returnId, String creditCard)

"EZShopData" -> "Employee" : end return transaction

```

Scenario 10-2: Return payment by cash

```plantuml
"Employee" -> "EZShopData" : start payment return
"EZShopData" -> "AccountBook": 1. returnCashPayment(Integer returnId)
"EZShopData" -> "Employee" : end payment return and rest is given

```

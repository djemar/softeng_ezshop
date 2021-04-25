# Design Document 


Authors: 

Date:

Version:


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
    Id : Integer
    +setRole()
    +getRole()
}
class DataImplementation{
    +UserAccountMap : User
    +CustomerAccountMap : Customer
    +EZShop
    +ProductMap : Product
    +PositionArray : Position
    +OrderMap : Order
    +CardMap : FidelityCard
    +SaleMap : Ticket
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
class Return{

}
class Debit{

}
class Credit{

}
class Ticket{
    +ticketNumber : Integer
    +transactionId : Integer
}
class SaleTransaction{
    +Id : Integer
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
class FinancialTransaction{
    +description : String
    +amount : Double
    +date : Date
    +Id : Integer

}
EZShop -- User
User -- DataImplementation
DataImplementation -- ProductType
ProductType -- Position
FidelityCard -- Customer
FidelityCard -- SaleTransaction
SaleTransaction-- SaleItem
FinancialTransaction -- DataImplementation
SaleTransaction -- Ticket
Credit <|-- Ticket
FinancialTransaction <|-- Credit
Debit <|-- Return
Debit <|-- Order
FinancialTransaction <|-- Debit

```
# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

|FR|EZShop|User|DataImpl|ProdT|Posit|FidelityC|Customer|Order|Return|Debit|Credit|Ticket|SaleTran|SaleIt|FinanTr|
|-----|:--|---:|-------:|----:|----:|--------:|-------:|----:|-----:|----:|-----:|-----:|-------:|-----:|------:|
| FR1 |  x| x  |  x     |     |     |         |        |     |      |     |      |      |        |      |       |
| FR2 |   |    |        |     |     |         |        |     |      |     |      |      |        |      |       |
| FR3 |  x| x  |  x     |  x  |     |         |        |     |      |     |      |      |        |      |       |
| FR4 |  x| x  |  x     |  x  |  x  |         |        |  x  |  x   |  x  |      |      |        |      |   x   |
| FR5 |  x| x  |  x     |     |     |   x     |   x    |     |      |     |      |      |   x?   |      |       |
| FR6 |  x| x  |  x     |  x  |     |   x?    |        |     |  x   |  x  |   x  |   x  |   x    |   x  |   x   |
| FR7 |  x|    |  x     |     |     |         |        |     |      |     |      |      |        |      |       |
| FR8 |  x|    |  x     |     |     |         |        |     |      |     |      |      |        |      |       |







# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>


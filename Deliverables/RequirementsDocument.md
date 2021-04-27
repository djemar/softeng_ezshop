# Requirements Document

Authors: Diego Marino, Michele Massetti, Mohamed Shehab, Elisa Tedde

Date: 21/04/2021

Version: 1.0

<!-- # Contents -->

- [Requirements Document](#requirements-document)
- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
  - [Context Diagram](#context-diagram)
  - [Interfaces](#interfaces)
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
  - [Functional Requirements](#functional-requirements)
    - [Access right, actor vs function](#access-right-actor-vs-function)
  - [Non Functional Requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
  - [Use case diagram](#use-case-diagram)
    - [Use case 1, UC1 - Create customer account](#use-case-1-uc1---create-customer-account)
    - [Use case 2, UC2 - Modify customer account](#use-case-2-uc2---modify-customer-account)
    - [Use case 3, UC3 - Delete Customer account](#use-case-3-uc3---delete-customer-account)
    - [Use case 4, UC4 - Search Customer account](#use-case-4-uc4---search-customer-account)
    - [Use case 5, UC5 - Manage Customer points](#use-case-5-uc5---manage-customer-points)
    - [Use case 6, UC6 - Add item to inventory](#use-case-6-uc6---add-item-to-inventory)
    - [Use case 7, UC7 - Modify item in inventory](#use-case-7-uc7---modify-item-in-inventory)
    - [Use case 8, UC8 - Delete item from inventory](#use-case-8-uc8---delete-item-from-inventory)
    - [Use case 9, UC9 - Search Items](#use-case-9-uc9---search-items)
    - [Use case 11, UC11 - Alert if stock is below a threshold](#use-case-11-uc11---alert-if-stock-is-below-a-threshold)
    - [Use case 12, UC12 - Manage shop expenses](#use-case-12-uc12---manage-shop-expenses)
    - [Use case 13, UC13 - Manage shop revenue](#use-case-13-uc13---manage-shop-revenue)
    - [Use case 14, UC14 - Generate report about accounting-related data](#use-case-14-uc14---generate-report-about-accounting-related-data)
    - [Use case 15, UC15 - Export expenses data to an spreadsheet file](#use-case-15-uc15---export-expenses-data-to-an-spreadsheet-file)
    - [Use case 16, UC16 - Manage sale transaction](#use-case-16-uc16---manage-sale-transaction)
    - [Use case 17, UC17 - Manage loyalty program](#use-case-17-uc17---manage-loyalty-program)
- [Glossary](#glossary)
- [System Design](#system-design)
- [Deployment Diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers
EZShop is a software application to:

- manage sales
- manage inventory
- manage customers
- support accounting

# Stakeholders

| Stakeholder name |                                                        Description                                                        |
| ---------------- | :-----------------------------------------------------------------------------------------------------------------------: |
| Customer         |                             Buys items and can require the fidelity card to obtain discounts                              |
| Developer        |           Develops and tests software to meet clients needs and monitors quality and performance of application           |
| Cashier          |                                  Manages cash register, PoS and interacts with customers                                  |
| Owner            | Uses the application to manage employees, customers and items. He manages his employees' access rights to the application |
| Accountant       |                                                  Manages shop accounting                                                  |
| Employee         |                                           Uses the application to manage items                                            |
| Fidelity Card    |                               Collects points every time that a customer buys some products                               |
| Cash Register    |   A machine in a business that indicates the amounts of individual cash sales and has a money drawer for making change.   |
| PoS              |                     Is where a customer executes the payment for goods or services with payment cards                     |
| Payment Card     |                                                    Is a payment method                                                    |
| Barcode Reader   |                                                  Scans an items' barcode                                                  |
| Item             |                          Product sold by EZShop that is identified by a barcode                            |
# Context Diagram and interfaces

## Context Diagram

```plantuml
scale 400 width
top to bottom direction
actor Owner as o
actor Employee as e
actor CashRegister as cr
actor Accountant as acc
actor Cashier as cas
actor PoS as pos
actor BarcodeReader as br

o -up-|> acc
cas -up-|> e
acc -up-|> cas
e -> (EZShop)
cr -> (EZShop)
pos -> (EZShop)
br -> (EZShop)
```

## Interfaces

| Actor         | Logical Interface |                                   Physical Interface |
| ------------- | :---------------: | ---------------------------------------------------: |
| Employee      |      Web GUI      | Screen, keyboard, mouse on PC, touchscreen on tablet |
| Cash Register |      Web GUI      |                                   Local network link |
| PoS           |     Visa API      |                                        Internet link |
| BarcodeReader |   USB protocol    |                                            USB cable |

# Stories and personas

The following personas and stories are meant to cover different profiles of the Owner actors.

David is 34, he played tennis for many years and now he is the owner of a sports shop in a little city. He loves speaking with his customers and giving precious advice about the best tennis rackets that he has in his inventory. He would like to be grateful to his customers for trusting him with some special discounts every 100 euros spent. Additionally, he would like to know exactly how many rackets are available in his warehouse in order to immediately advertise to his customers that a certain item is being sold quickly.

Elisabeth is 25, she is a young entrepreneur and she studied market strategies at Bocconi. She opened her first clothes shop in the middle of Milan 2 years ago and she advertises her season collection on Instagram. She has found some difficulties because Milan is the capital of fashion where there is a great number of rich Chinese and Russian tourists that require a high level of service: on many occasion clothes sizes had finished quickly, she forgot to order them and her customers were unsatisfied. She would like to have an alarm that notifies her when some clothes sizes are low on stock.

Tom is 40, he is the accountant of an Irish shop called "Dubliners". This shop is very famous for the quality of its whisky that is the best in the city and it also offers a variety of cheap craft beers. Unfortunately, four years ago Tom had some serious economical issues because the prices of the products were too low and he didn't have enough money to pay his employees salary. He would like to manage well his receipts and the prices of the products to satisfy both customers and employees.

Katia is 50, she is the owner of a fish market. 2 years ago she employed her son John as a cashier because he was jobless. He doesn't have a good relationship with technology and he prefers using the calculator to obtain the total of each order. Additionally, Katia discovered that he didn't make receipts and one day she had to pay an expensive fine. John would like to use an easy application that shows the cost of each product, calculates autonomously the total cost and shows it on the screen. Additionally, he would like an error message when the payment was not successful and so the receipt was not issued.

# Functional and non functional requirements

## Functional Requirements

| ID    |                                        Description                                         |
| ----- | :----------------------------------------------------------------------------------------: |
| FR1   |                                      Manage customer                                       |
| FR1.1 |                   Define a new customer, or modify an existing customer                    |
| FR1.2 |                                     Delete a customer                                      |
| FR1.3 |                                     Search a customer                                      |
| FR1.4 |                                   Manage Customer points                                   |
| FR2   | Manage rights. Authorize access to functions to specific actors according to access rights |
| FR3   |                                        Manage items                                        |
| FR3.1 |                       Define a new item, or modify an existing item                        |
| FR3.2 |                                       Delete an item                                       |
| FR3.3 |                                        Search items                                        |
| FR3.4 |                            Alert if stock is below a threshold                             |
| FR4   |                                     Manage accounting                                      |
| FR4.1 |                                    Manage shop revenue                                     |
| FR4.2 |                                    Manage shop expenses                                    |
| FR4.3 |                                    Manage sales history                                    |
| FR4.4 |                       Generate report about accounting-related data                        |
| FR4.5 |                        Export expenses data to an spreadsheet file                         |
| FR5   |                                  Manage sale transaction                                   |
| FR5.1 |                              Exchange data with cash register                              |
| FR5.2 |                                   Exchange data with PoS                                   |
| FR5.3 |                                Manage payment confirmation                                 |
| FR5.4 |                             Rollback in case of failed payment                             |
| FR6   |                                   Manage loyalty program                                   |
| FR6.1 |                        Customize threshold needed for the discount                         |
| FR6.2 |                                 Customize discount amount                                  |

### Access right, actor vs function

| Function | Owner | Accountant | Employee | Cashier |
| -------- | ----- | ---------- | -------- | ------- |
| FR1.1    | yes   | yes        | no       | yes     |
| FR1.2    | yes   | yes        | no       | yes     |
| FR1.3    | yes   | yes        | no       | yes     |
| FR1.4    | yes   | yes        | no       | yes     |
| FR2      | yes   | no         | no       | no      |
| FR3      | yes   | yes        | yes      | yes     |
| FR4      | yes   | yes        | no       | no      |
| FR6      | yes   | no         | no       | no      |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID   | Type (efficiency, reliability, ..) |                                                                                                                                                                                 Description                                                                                                                                                                                 | Refers to |
| ---- | :--------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | --------: |
| NFR1 |             Usability              |                                                                                                                                                   Application should be used with no specific training for the employees                                                                                                                                                    |    All FR |
| NFR2 |            Performance             |                                                                                                                                                                 All functions should complete in < 0.5 sec                                                                                                                                                                  |    All FR |
| NFR3 |            Portability             | The application should be accessed by Chrome (version 81 and more recent), and Safari (version 13 and more recent) (this covers around 80% of installed browsers); and from the operating systems where these browsers are available (Android, IoS, Windows, MacOS, Linux). As for devices, the application should be usable on smartphones (portrait) and PCs (landscape). |    All FR |
| NFR4 |              Privacy               |                                                                                                                                                         Customer and employee data is treated with respect of GDPR.                                                                                                                                                         |    All FR |
| NFR5 |            Localization            |                                                                                                                                                                   Decimal numbers use . (dot) as decimal                                                                                                                                                                    |    All FR |
| NFR6 |          Interoperability          |                                                                                                                                              The API should be compatible with the majority of cash registers and PoS brands.                                                                                                                                               |    All FR |
| NFR7 |            Reliability             |                                                                                                                                             The application must be reliable for shops with 1-2 cash registers, 5-10 employees.                                                                                                                                             |    All FR |

# Use case diagram and use cases

## Use case diagram

```plantuml
scale 600 width
actor Owner as o
actor Employee as e
actor CashRegister as ca
actor Accountant as acc
actor BarcodeReader as bc
actor Cashier as c
actor PoS as pos

acc -> (manage accounting)
e -> (manage items)
(manage items) <- bc
(manage sales transaction) --> ca
(manage sales transaction) <-- bc
(manage sales transaction) --> pos
c --> (manage customers)
(manage customers) <-- bc
o -> (manage access rights)
o -> (manage loyalty program)
(manage access rights) -> e
o -up-|> acc
acc -up-|> c
c -up-|> e
```

```plantuml
scale 600 width
top to bottom direction
(manage customers) .> (define/modify customer) : include
(manage customers) .> (delete a customer) : include
(manage customers) .> (search customer) : include
(manage customers) .> (manage customer points) : include
```

```plantuml
scale 600 width
top to bottom direction
(manage items) .> (define/modify item) : include
(manage items) .> (delete an item ) : include
(manage items) .> (search items) : include
(manage items) .> (alert if stock is below a threshold) : include

```

```plantuml
scale 600 width
top to bottom direction
(manage accounting) .> (manage shop revenue) : include
(manage accounting) .> (manage shop expenses) : include
(manage accounting) .> (manage sales history) : include
(manage accounting) .> (generate report) : include
(manage accounting) .> (export expenses data to an excel file):include
```

```plantuml
scale 600 width
(manage sales transaction) .> (Exchange data with cash register) : include
(manage sales transaction) .> (Exchange data with PoS) : include
(manage sales transaction) .> (Manage payment confirmation) : include
(manage sales transaction) .> (Rollback in case of failed payment) : include

```

### Use case 1, UC1 - Create customer account

| Actors Involved  |                                            Cashier, Customer, BarcodeReader                                            |
| ---------------- | :--------------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                                Account C doesn't exist                                                 |
| Post condition   |                                                   Account C is added                                                   |
| Nominal Scenario | Cashier creates account C with Customer data and links it with a fidelity card that is activated by the Barcode Reader |
| Variants         |                                                                                                                        |

| Scenario 1.1   |        Cashier, Customer, BarcodeReader         |
| -------------- | :---------------------------------------------: |
| Precondition   |             Account C doesn't exist             |
| Post condition |               Account C is added                |
| Step#          |                   Description                   |
| 1              |    Cashier creates a new Customer account C     |
| 2              | Cashier fills the field of C with Customer info |
| 3              |        Cashier scans a new fidelity card        |
| 4              |   Fidelity card is activated and linked to C    |

### Use case 2, UC2 - Modify customer account

| Actors Involved  |                                                         Cashier, Customer                                                          |
| ---------------- | :--------------------------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                                          Account C exists                                                          |
| Post condition   |                                                                 -                                                                  |
| Nominal Scenario | The Customer communicates some changes to his data that was previously collected, Cashier modifies one or more fields of account C |
| Variants         |                                                                                                                                    |

### Use case 3, UC3 - Delete Customer account

| Actors Involved  |                    Cashier                     |
| ---------------- | :--------------------------------------------: |
| Precondition     |                Account C exists                |
| Post condition   |       Account C deleted from the system        |
| Nominal Scenario | Cashier selects a customer account C to delete |
| Variants         |                                                |

### Use case 4, UC4 - Search Customer account

| Actors Involved  |                        Cashier                         |
| ---------------- | :----------------------------------------------------: |
| Precondition     |                           -                            |
| Post condition   |          Account C retrieved from the system           |
| Nominal Scenario |          Cashier search a Customer account C           |
| Variants         | If there are no results, an error message is displayed |

### Use case 5, UC5 - Manage Customer points

| Actors Involved  |                                                 Cashier, BarcodeReader                                                  |
| ---------------- | :---------------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                     Customer collects enough points for a discount                                      |
| Post condition   |                                     Discount is applied and points are set to zero                                      |
| Nominal Scenario |              Cashier scans the fidelity card through Barcode Reader and discount is automatically applied               |
| Variants         | If fidelity points exceeds threshold, the new value is set as the difference between fidelity points and the threshold. |
| Variants         |                  If the customer doesn't qualify for the discount, new points are added to his balance                  |

| Scenario 5.1   |                                       Cashier, BarcodeReader                                        |
| -------------- | :-------------------------------------------------------------------------------------------------: |
| Precondition   |                          Customer C collects enough points for a discount                           |
| Post condition |                                         Discount is applied                                         |
| Step#          |                                             Description                                             |
| 1              |                       Cashier scans the fidelity card with the Barcode Reader                       |
| 2              |                        C has more points than needed for obtaining discount                         |
| 3              |                                         Discount is applied                                         |
| 4              | New value of C's fidelity points is set as the difference between fidelity points and the threshold |

| Scenario 5.2   |                  Cashier,BarcodeReader                  |
| -------------- | :-----------------------------------------------------: |
| Precondition   |               Customer C collects points                |
| Post condition |                 Discount is not applied                 |
| Step#          |                       Description                       |
| 1              | Cashier scans the fidelity card with the Barcode Reader |
| 2              |        C has not enough points for the discount         |
| 3              |          Points are added to C's fidelity card          |

### Use case 6, UC6 - Add item to inventory

| Actors Involved  |                               Employee, BarcodeReader                                |
| ---------------- | :----------------------------------------------------------------------------------: |
| Precondition     |                            Item I doesn't exist in the DB                            |
| Post condition   |                            Item I is added to the system                             |
| Nominal Scenario | Employee enters item ID scanning it with the Barcode Reader and populates its fields |
| Variants         |                           The item I is already in the DB                            |

| Scenario 6.1   |          Employee, BarcodeReader           |
| -------------- | :----------------------------------------: |
| Precondition   |       Item I doesn't exist in the DB       |
| Post condition |       Item I is added to the system        |
| Step#          |                Description                 |
| 1              | Employee E scans I with the Barcode Reader |
| 2              |       I is created in the inventory        |
| 3              |          E fills the fields of I           |

| Scenario 6.2   |                          Employee, BarcodeReader                          |
| -------------- | :-----------------------------------------------------------------------: |
| Precondition   |                          Item I exists in the DB                          |
| Post condition |                     Item I is not added to the system                     |
| Step#          |                                Description                                |
| 1              |                Employee E scans I with the Barcode Reader                 |
| 2              | An error dialog tells the employee that the item SKU is already in the DB |

### Use case 7, UC7 - Modify item in inventory

| Actors Involved  |                    Employee                    |
| ---------------- | :--------------------------------------------: |
| Precondition     |                 Item I exists                  |
| Post condition   |                 Updated Item I                 |
| Nominal Scenario | Employee modifies one or more fields of item I |
| Variants         |                                                |

| Scenario 7.1   |          Employee, BarcodeReader           |
| -------------- | :----------------------------------------: |
| Precondition   |               Item I exists                |
| Post condition |               Updated Item I               |
| Step#          |                Description                 |
| 1              | Employee E scans I with the Barcode Reader |
| 2              |          I is found and displayed          |
| 3              |     E modifies one or more fields of I     |

### Use case 8, UC8 - Delete item from inventory

| Actors Involved  |                  Employee                  |
| ---------------- | :----------------------------------------: |
| Precondition     |               Item I exists                |
| Post condition   |     Item I is deleted from the system      |
| Nominal Scenario | Employee deletes item I from the inventory |
| Variants         |                                            |

| Scenario 8.1   |          Employee, BarcodeReader           |
| -------------- | :----------------------------------------: |
| Precondition   |               Item I exists                |
| Post condition |     Item I is deleted from the system      |
| Step#          |                Description                 |
| 1              | Employee E scans I with the Barcode Reader |
| 2              |          I is found and displayed          |
| 3              |         E deletes I from inventory         |

### Use case 9, UC9 - Search Items

| Actors Involved  |                        Employee                        |
| ---------------- | :----------------------------------------------------: |
| Precondition     |                           -                            |
| Post condition   |            Item I retrieved from the system            |
| Nominal Scenario |              Employee searches an item I               |
| Variants         | If there are no results, an error message is displayed |

| Scenario 9.1   |             Employee             |
| -------------- | :------------------------------: |
| Precondition   |          Item I exists           |
| Post condition | Item I retrieved from the system |
| Step#          |           Description            |
| 1              |      Employee E searches I       |
| 2              |     I is found and displayed     |

| Scenario 9.2   |           Employee            |
| -------------- | :---------------------------: |
| Precondition   |     Item I doesn't exist      |
| Post condition |  Error message is displayed   |
| Step#          |          Description          |
| 1              |     Employee E searches I     |
| 2              |        I is not found         |
| 3              | An error message is displayed |

### Use case 11, UC11 - Alert if stock is below a threshold

| Actors Involved  |                         Accountant                          |
| ---------------- | :---------------------------------------------------------: |
| Precondition     |               Item stock is below a threshold               |
| Post condition   |                An alert message is displayed                |
| Nominal Scenario | Employee checks the inventory section and notices the alert |
| Variants         |                                                             |

### Use case 12, UC12 - Manage shop expenses

| Actors Involved  |                  Accountant                   |
| ---------------- | :-------------------------------------------: |
| Precondition     |                       -                       |
| Post condition   |     A new expense is added to the system      |
| Nominal Scenario | Accountant adds the new expense to the system |
| Variants         |                                               |

### Use case 13, UC13 - Manage shop revenue

| Actors Involved  |                  Accountant                   |
| ---------------- | :-------------------------------------------: |
| Precondition     |                       -                       |
| Post condition   |   A new shop earning is added to the system   |
| Nominal Scenario | Accountant adds the new earning to the system |
| Variants         |                                               |

### Use case 14, UC14 - Generate report about accounting-related data

| Actors Involved  |                          Accountant                           |
| ---------------- | :-----------------------------------------------------------: |
| Precondition     |                               -                               |
| Post condition   |               Report is provided by the system                |
| Nominal Scenario | The accountant can check the report in the Accounting section |
| Variants         |     The accountant can set the time window for the report     |

| Scenario 14.1  |                                     Accountant                                      |
| -------------- | :---------------------------------------------------------------------------------: |
| Precondition   |                                          -                                          |
| Post condition |                          Report is provided by the system                           |
| Step#          |                                     Description                                     |
| 1              |                       Accountant navigates to the report tab                        |
| 2              | No time window is set, all data related to the accounting is provided in the report |

| Scenario 14.2  |                         Accountant                         |
| -------------- | :--------------------------------------------------------: |
| Precondition   |                             -                              |
| Post condition |              Report is provided by the system              |
| Step#          |                        Description                         |
| 1              |           Accountant navigates to the report tab           |
| 2              | A sets a time window, time filter is applied to the report |

### Use case 15, UC15 - Export expenses data to an spreadsheet file

| Actors Involved  |                                   Accountant                                   |
| ---------------- | :----------------------------------------------------------------------------: |
| Precondition     |                                       -                                        |
| Post condition   |                   Spreadsheet file is provided by the system                   |
| Nominal Scenario | The accountant can consult expenses data exporting them to an spreadsheet file |
| Variants         |                                                                                |

### Use case 16, UC16 - Manage sale transaction

| Actors Involved  |                                             Cashier, BarcodeReader                                             |
| ---------------- | :------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                                       -                                                        |
| Post condition   |                                         Sale transaction is concluded                                          |
| Nominal Scenario | Cashier scans the items through Barcode Reader and their prices are added to the total and manages the checkout |
| Variants         |                                                                                                                |

| Scenario 16.1  |           Cashier, PoS, Cash Register           |
| -------------- | :---------------------------------------------: |
| Precondition   |                        -                        |
| Post condition |               Payment is accepted               |
| Step#          |                   Description                   |
| 1              | The customer chooses to pay with a payment card |
| 2              |              The card is accepted               |
| 3              |                Receipt is issued                |

| Scenario 16.2  |                                           Cashier, PoS                                            |
| -------------- | :-----------------------------------------------------------------------------------------------: |
| Precondition   |                                                 -                                                 |
| Post condition |                                 System rollbacks the transaction                                  |
| Step#          |                                            Description                                            |
| 1              |                              The customer chooses to pay with a card                              |
| 2              |                                        The card is refused                                        |
| 3              | The cashier voids the transaction and provides the possibility to pay with another payment option |

| Scenario 16.3  |                             Cashier                              |
| -------------- | :--------------------------------------------------------------: |
| Precondition   |      Customer can't proceed with the transaction right away      |
| Post condition |                       Order is put on hold                       |
| Step#          |                           Description                            |
| 1              |      Customer can't proceed with the transaction right away      |
| 2              | Cashier puts the order on hold and proceeds with the next client |

### Use case 17, UC17 - Manage loyalty program

| Actors Involved  |                         Owner                          |
| ---------------- | :----------------------------------------------------: |
| Precondition     |                           -                            |
| Post condition   |     A discount is set for a given amount of points     |
| Nominal Scenario | The owner sets a discount for a given amount of points |
| Variants         |                                                        |

# Glossary

```plantuml
scale 800 width
class Person{
 name;
 surname;
 email;
 identitycardnumber;
}

class ItemDescriptor{
 id;
 description;
 price;
 unit;
}
class Item{
 id;
 discount%;
 thresholdalarm;
 quantity;
}
class FidelityCard{
 id;
 creationdate;
 points;
}
class Owner{
 license;
 VATnumber;
}
class Cashier{

}
class Accountant{

}
class Employee{

}

class Subscriber{

}
class EZshop{

}
class Inventory{

}

class Expense{
 type;
 cost;
 description;
 id;
 category;
 date;
}
class VisaAPI{

}
class Transaction{
 date;
 time;
 totalcost;
 paymentcard;
}

Accountant <|-- Owner
Cashier <|-- Accountant
Employee <|--  Cashier
Person <|-- Employee
Person <|-- Subscriber

Employee "*" -- EZshop
VisaAPI -- EZshop
Item "*" -- Inventory
Item "0..*" - ItemDescriptor : is described by >
Inventory - EZshop
Subscriber - FidelityCard : own >
Accountant -  "1..*" Expense : manages >
FidelityCard "0..*" -- EZshop
Transaction -- "1..*" Item : includes >
Transaction "0..*" -- FidelityCard
Transaction "0..*" -- VisaAPI

```

# System Design

Not really meaningful in this case. Only software components are needed.

# Deployment Diagram

```plantuml
node LocalServer
node PCEmployee
node TabletEmployee
artifact EZShop{
  artifact ManageSales
  artifact ManageInventory
  artifact ManageCustomers
  artifact SupportAccounting
  database DB
}
PCEmployee "*" -- LocalServer : local network
TabletEmployee "*" -- LocalServer : local network
EZShop -- LocalServer
```

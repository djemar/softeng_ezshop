# Requirements Document

Authors: Diego Marino, Michele Massetti, Mohamed Shehab, Elisa Tedde

Date: 21/04/2021

Version: 1.0

# Contents

- [Requirements Document](#requirements-document)
- [Contents](#contents)
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
      - [Scenario 1.1](#scenario-11)
      - [Scenario 1.2](#scenario-12)
      - [Scenario 1.x](#scenario-1x)
    - [Use case 2, UC2 - Modify customer account](#use-case-2-uc2---modify-customer-account)
    - [Use case 3, UC3 - Delete Customer account](#use-case-3-uc3---delete-customer-account)
    - [Use case 4, UC4 - Create employee account](#use-case-4-uc4---create-employee-account)
    - [Use case 5, UC5 - Modify employee account](#use-case-5-uc5---modify-employee-account)
    - [Use case 6, UC6 - Delete employee account](#use-case-6-uc6---delete-employee-account)
    - [Use case 7, UC7 - Add item to inventory](#use-case-7-uc7---add-item-to-inventory)
    - [Use case 8, UC8 - Modify item in inventory](#use-case-8-uc8---modify-item-in-inventory)
    - [Use case 9, UC9 - Delete item from inventory](#use-case-9-uc9---delete-item-from-inventory)
    - [Use case x, UCx](#use-case-x-ucx)
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

| Stakeholder name |                                                               Description                                                                |
| ---------------- | :--------------------------------------------------------------------------------------------------------------------------------------: |
| Customer         |                      Uses the application to manage his profile and to check his fidelity points and related offers                      |
| Administrator    |                                                  Manages the application and its rights                                                  |
| Developer        |                  Develops and tests software to meet clients needs and monitors quality and performance of application                   |
| Cashier          |                                            Manages cash register and interacts with customers                                            |
| Owner            |                                      Uses the application to manage employees, customers and items                                       |
| Accountant       |                                        Manages employees salaries, shop income and shop expenses                                         |
| Employee         | Uses the application to find information about his work shifts and, depending on his access rights, managing items and customer profiles |

# Context Diagram and interfaces

## Context Diagram

\<Define here Context diagram using UML use case diagram>

\<actors are a subset of stakeholders>

```plantuml
top to bottom direction
actor Owner as o
actor Employee as e
actor CashRegister as ca
actor Item as i
actor Accountant as acc
actor Cashier as cas
actor Customer as cu

o -up-|> acc
cas -up-|> e
acc -up-|> cas
i -> (EZShop)
e -> (EZShop)
ca -> (EZShop)
cu -> (EZShop)
```

## Interfaces

\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

\<Maybe employee is enough, owner can be removed?>

| Actor         | Logical Interface |                                                      Physical Interface |
| ------------- | :---------------: | ----------------------------------------------------------------------: |
| Item          |      Barcode      |                                                         Barcode scanner |
| Employee      |      Web GUI      |                Screen, keyboard, mouse on PC, touchscreen on smartphone |
| Owner         |      Web GUI      |                Screen, keyboard, mouse on PC, touchscreen on smartphone |
| Customer      | Web GUI, Barcode  | Screen, keyboard, mouse on PC, touchscreen on smartphone, Fidelity card |
| Cash Register |        API        |                                                      Local network link |

# Stories and personas

\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

The following personas and stories are meant to cover different profiles of the Owner actors. 

Davis is the owner of a sport shop, he opened it 2 years before. He started with only 2 employees, so it was easy to organize workshifts, now he has 6 employees and it becomes more complicated.

Elisabeth has a grocery store. Unfotunately more than one time customers found expired items on the shelves; she would like to recognize the food in expiration and apply a discount to it.

Sophie manages a clothes shop, she noticed that her employees lost much time in searching for the right sizes of the clothes in the warehouse if not present in the shop.


# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID      |                                        Description                                         |
| ------- | :----------------------------------------------------------------------------------------: |
| FR1     |                                      Manage customer                                       |
| FR1.1   |                   Define a new customer, or modify an existing customer                    |
| FR1.2   |                                     Delete a customer                                      |
| FR1.3   |                                     List all customer                                      |
| FR1.4   |                                     Search a customer                                      |
| FR1.5   |                                   Manage customer points                                   |
| FR2     | Manage rights. Authorize access to functions to specific actors according to access rights |
| FR3     |                                        Manage items                                        |
| FR3.1   |                       Define a new item, or modify an existing item                        |
| FR3.2   |                                       Delete a item                                        |
| FR3.3   |                                       List all items                                       |
| FR3.4   |                                        Search items                                        |
| FR3.5   |                            Alert if stock is below a threshold                             |
| FR3.6   |                            Define, update prices for each item                             |
| FR3.7   |                           Define, update discounts for each item                           |
| FR4     |                                      Manage employees                                      |
| FR4.1   |                                     Define work shifts                                     |
| FR4.1.1 |                                      Define holidays                                       |
| FR4.2   |                   Define a new employee, or modify an existing employee                    |
| FR4.3   |                                     Delete a employee                                      |
| FR4.4   |                                     List all employee                                      |
| FR4.5   |                                     Search a employee                                      |
| FR5     |                                     Manage accounting                                      |
| FR5.1   |                                 Manage employees salaries                                  |
| FR5.2   |                                     Manage shop income                                     |
| FR5.3   |                                    Manage shop expenses                                    |
| FR5.4   |                                    Manage sales history                                    |
| FR5.5   |                 Generate report about accounting-related data with filters                 |
| FR6     |                                    Manage cash register                                    |
| FR6.1   |                              Retrieve data from cash register                              |
| FR6.2   |                                Expose data to cash register                                |

### Access right, actor vs function

| Function | Owner | Accountant | Employee | Cashier | Customer |
| -------- | ----- | ---------- | -------- | ------- | -------- |
| FR1.1    | yes   | no         | no       | no      | yes      |
| FR1.2    | yes   | no         | no       | no      | yes      |
| FR1.3    | yes   | no         | no       | no      | no       |
| FR1.4    | yes   | no         | no       | no      | no       |
| FR1.5    | yes   | no         | no       | yes     | yes      |
| FR3.1    | yes   | no         | no       | no      | no       |
| FR3.2    | yes   | no         | no       | no      | no       |
| FR3.3    | yes   | no         | yes      | no      | no       |
| FR3.4    | yes   | no         | yes      | no      | no       |
| FR3.5    | yes   | no         | yes      | no      | no       |
| FR3.6    | yes   | no         | no       | no      | no       |
| FR3.7    | yes   | no         | no       | no      | no       |
| FR3.8    | yes   | no         | yes      | no      | no       |
| FR4.1    | yes   | no         | no       | no      | no       |
| FR4.2    | yes   | no         | no       | no      | no       |
| FR4.3    | yes   | no         | no       | no      | no       |
| FR4.4    | yes   | yes        | no       | no      | no       |
| FR4.5    | yes   | yes        | no       | no      | no       |
| FR5      | yes   | yes        | no       | no      | no       |
| FR6      | yes   | no         | no       | yes     | no       |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID   | Type (efficiency, reliability, ..) |                                                                                                                                                                                Description                                                                                                                                                                                 | Refers to |
| ---- | :--------------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | --------: |
| NFR1 |             Usability              |                                                                                                                                                   Application should be used with no specific training for the employees                                                                                                                                                   |    All FR |
| NFR2 |            Performance             |                                                                                                                                                                 All functions should complete in < 0.5 sec                                                                                                                                                                 |    All FR |
| NFR3 |            Portability             | The application should be accessed by Chrome (version 81 and more recent), and Safari (version 13 and more recent) (this covers around 80% of installed browsers); and from the operating systems where these browsers are available (Android, IoS, Windows, MacOS, Unix). As for devices, the application should be usable on smartphones (portrait) and PCs (landscape). |    All FR |
| NFR4 |              Privacy               |                                                                                                                                                        Customer and employee data is treated with respect of GDPR.                                                                                                                                                         |    All FR |
| NFR5 |            Localisation            |                                                                                                                                                                   Decimal numbers use . (dot) as decimal                                                                                                                                                                   |    All FR |
| NFR6 |          Interoperability          |                                                                                                                                                  The API should be compatible with the majority of cash registers brands.                                                                                                                                                  |    All FR |
| NFR7 |            Reliability             |                                                                                                                                            The application must be reliable for shops with 1-2 cash registers, 5-10 employees.                                                                                                                                             |    All FR |

# Use case diagram and use cases

## Use case diagram

\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>

\<next describe here each use case in the UCD>

### Use case 1, UC1 - Create customer account

| Actors Involved  |                                              Employee, **Customer**                                               |
| ---------------- | :---------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                             Account C does not exists                                             |
| Post condition   |                                           Account C added to the system                                           |
| Nominal Scenario | Employee collects customer data and creates the account **!! Are customers allowed to create their own account?** |
| Variants         |                                     \<other executions, ex in case of errors>                                     |

##### Scenario 1.1

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1   |                                                                            |
| -------------- | :------------------------------------------------------------------------: |
| Precondition   | \<Boolean expression, must evaluate to true before the scenario can start> |
| Post condition |  \<Boolean expression, must evaluate to true after scenario is finished>   |
| Step#          |                                Description                                 |
| 1              |                                                                            |
| 2              |                                                                            |
| ...            |                                                                            |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2 - Modify customer account

| Actors Involved  |                Employee, Customer                 |
| ---------------- | :-----------------------------------------------: |
| Precondition     |                 Account C exists                  |
| Post condition   |                         -                         |
| Nominal Scenario | Employee modifies one or more fields of account C |
| Variants         |                                                   |

### Use case 3, UC3 - Delete Customer account

| Actors Involved  |               Employee, Customer                |
| ---------------- | :---------------------------------------------: |
| Precondition     |                Account C exists                 |
| Post condition   |        Account C deleted from the system        |
| Nominal Scenario | Employee selects a customer account C to delete |
| Variants         |                                                 |

### Use case 4, UC4 - Create employee account

| Actors Involved  |                                                                  Employee                                                                  |
| ---------------- | :----------------------------------------------------------------------------------------------------------------------------------------: |
| Precondition     |                                                         Account E does not exists                                                          |
| Post condition   |                                                       Account E added to the system                                                        |
| Nominal Scenario | Owner/Employee with access rights collects employee data and creates the account **!! Are customers allowed to create their own account?** |
| Variants         |                                                 \<other executions, ex in case of errors>                                                  |

### Use case 5, UC5 - Modify employee account

| Actors Involved  |                                  Employee                                  |
| ---------------- | :------------------------------------------------------------------------: |
| Precondition     |                              Account E exists                              |
| Post condition   |                                     -                                      |
| Nominal Scenario | Owner/Employee with access rights modifies one or more fields of account E |
| Variants         |                                                                            |

### Use case 6, UC6 - Delete employee account

| Actors Involved  |                                 Employee                                  |
| ---------------- | :-----------------------------------------------------------------------: |
| Precondition     |                             Account E exists                              |
| Post condition   |                     Account E deleted from the system                     |
| Nominal Scenario | Owner/Employee with access rights selects an employee account E to delete |
| Variants         |                                                                           |

### Use case 7, UC7 - Add item to inventory

| Actors Involved  |                     Employee                     |
| ---------------- | :----------------------------------------------: |
| Precondition     |                  Item I exists                   |
| Post condition   |            Item I added to the system            |
| Nominal Scenario | Employee creates item I and populate its fields  |
| Variants         | Each item has a unique SKU based on it's barcode |

### Use case 8, UC8 - Modify item in inventory

| Actors Involved  |                    Employee                    |
| ---------------- | :--------------------------------------------: |
| Precondition     |                 Item I exists                  |
| Post condition   |                       -                        |
| Nominal Scenario | Employee modifies one or more fields of item I |
| Variants         |                                                |

### Use case 9, UC9 - Delete item from inventory

| Actors Involved  |               Employee               |
| ---------------- | :----------------------------------: |
| Precondition     |            Item I exists             |
| Post condition   |    Item I deleted from the system    |
| Nominal Scenario | Employee selects an item I to delete |
| Variants         |                                      |

..

### Use case x, UCx

..

# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships>

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design

\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram

\<describe here deployment diagram >

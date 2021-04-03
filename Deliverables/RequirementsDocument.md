# Requirements Document

Authors: Diego Marino, Michele Massetti, Mohamed Shehab, Elisa Tedde

Date: 21/04/2021

Version: 1.0

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders

| Stakeholder name | Description |
| ---------------- | :---------: |
| Customer         |             |
| Administrator    |             |
| Developer        |             |
| Supplier         |             |
| Cashier          |             |
| Owner            |             |
| Accountant       |             |
| Employee         |             |

# Context Diagram and interfaces

## Context Diagram

\<Define here Context diagram using UML use case diagram>

\<actors are a subset of stakeholders>

```plantuml
top to bottom direction
actor Owner as o
actor Employee as e
actor CashRegister as ca
actor Inventory as in
actor Accountant as acc

o -up-|> acc
acc -up-|> e
in -> (EZShop)
e -> (EZShop)
ca -> (EZShop)
```

## Interfaces

\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

\<Maybe employee is enough, owner can be removed?>

| Actor         | Logical Interface |                                       Physical Interface |
| ------------- | :---------------: | -------------------------------------------------------: |
| Inventory     |      Barcode      |                                          Barcode scanner |
| Employee      |      Web GUI      | Screen, keyboard, mouse on PC, touchscreen on smartphone |
| Owner         |      Web GUI      | Screen, keyboard, mouse on PC, touchscreen on smartphone |
| Cash Register |        API        |                                       local network link |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>


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
| FR3.8   |                         Alert if item is expiring **(FOOD SHOP)**                          |
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

| Function | Admin | Actor                  | Actor |
| -------- | :---- | ---------------------- | ----- |
| FR1.1    | yes   | only user X for user X | no    |
| FR1.2    | yes   | only user X for user X | no    |
| FR1.3    | yes   | no                     | no    |
| FR2      | yes   | no                     | no    |
| FR3      | yes   | no                     | no    |
| FR4      | yes   | yes                    | yes   |
| FR5.1    | yes   | yes                    | no    |
| FR5.2    | no    | no                     | no    |
| FR5.3    | yes   | yes                    | no    |

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
### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |  
|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     | \<other executions, ex in case of errors> |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the scenario can start> |
|  Post condition     | \<Boolean expression, must evaluate to true after scenario is finished> |
| Step#        | Description  |
|  1     |  |  
|  2     |  |
|  ...     |  |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2
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


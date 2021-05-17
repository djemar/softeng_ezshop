# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)

- [White Box Unit Tests](#white-box-unit-tests)

# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed
    to start tests
    >

### **Class _Utils_ - method _validateBarcode_**

**Criteria for method _validateBarcode_:**

- Validity of string parameter
- Length of code
- Format of code
- Check digit algorithm successful

**Predicates for method _validateBarcode_:**

| Criteria                         | Predicate               |
| -------------------------------- | ----------------------- |
| Validity of string parameter     | Valid                   |
|                                  | NULL                    |
| length of code                   | (0,11)                  |
|                                  | [12,14]                 |
|                                  | [15,maxstring)          |
| format of code                   | true(in case of digits) |
|                                  | false(others)           |
| check digit algorithm successful | true                    |
|                                  | false                   |

**Boundaries**:

Boundaries for method validateBarcode:

| Criteria       | Boundary values |
| -------------- | --------------- |
| length of code | 11,12,14,15     |

**Combination of predicates**:

Combination of predicates for method validateBarcode

| length of code | format of code | check digit algorithm successful | Valid / Invalid | Description of the test case   | JUnit test case |
| -------------- | -------------- | -------------------------------- | --------------- | ------------------------------ | --------------- |
| (null,11)      | \*             | \*                               | Invalid         | T1(542424) -> false            |                 |
|                |                |                                  |                 | Tb1(null) -> false             |                 |
|                |                |                                  |                 |                                |                 |
| [14,maxstring) | \*             | \*                               | Invalid         | T2(4521864284293182) -> false  |                 |
|                |                |                                  |                 |                                |                 |
| \*             | not digit      | \*                               | Invalid         | T3(dfjn87154) -> false         |                 |
|                |                |                                  |                 | Tb3(skvidnsfffdnjsfd) -> false |                 |
| \*             | \*             | false                            | Invalid         |                                |                 |
| [12,14]        | digits         | false                            | Invalid         | T4(12345678954164) -> false    |                 |
| [12,14]        | digits         | true                             | Valid           | T4(8032817681723) -> true      |                 |

### **Class _Utils _ - method _isOnlyDigit_**

**Criteria for method _isOnlyDigit_:**

-
-
-
-
- **Predicates for method _isOnlyDigit_:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |

**Boundaries**:

Boundaries for method isOnlyDigit:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |

**Combination of predicates**:

Combination of predicates for method isOnlyDigit

| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
| ---------- | ---------- | --- | --------------- | ---------------------------- | --------------- |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |

### **Class _Utils_ - method _validateCreditCard_**

**Criteria for method _validateCreditCard_:**

- Validity of string parameter
- Number follows the Luhn algorithm

- **Predicates for method _validateCreditCard_:**

| Criteria                          | Predicate |
| --------------------------------- | --------- |
| Validity of string parameter      | Valid     |
|                                   | NULL      |
| Number follows the Luhn algorithm | true      |
|                                   | false     |

**Boundaries**:

Boundaries for method validateCreditCard:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

Combination of predicates for method validateCreditCard

| Number follows the Luhn algorithm | Valid / Invalid | Description of the test case  | JUnit test case |
| --------------------------------- | --------------- | ----------------------------- | --------------- |
| false                             | Invalid         | T1(4218632014875236) -> false |                 |
| true                              | Valid           | T2(4716258050958645) -> true  |                 |

### **Class _Utils _ - method _containsProduct_**

**Criteria for method _containsProduct_:**

- Validity of string parameter
- existence of the product in the list

**Predicates for method _containsProduct_:**

| Criteria                             | Predicate |
| ------------------------------------ | --------- |
| Validity of string parameter         | Valid     |
|                                      | NULL      |
| existence of the product in the list | yes       |
|                                      | no        |

**Boundaries**:

Boundaries for method containsProduct:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

Combination of predicates for method containsProduct

| existence of the product in the list | Valid / Invalid | Description of the test case        | JUnit test case |
| ------------------------------------ | --------------- | ----------------------------------- | --------------- |
| yes                                  | valid           | t1("item19,item20,item21","item20") |                 |
| no                                   | invalid         | t2("item35,item33,item34","item29") |                 |

### **Class _Utils _ - method _getProductFromEntries_**

**Criteria for method _getProductFromEntries_:**

- Validity of string parameter
- Validity of object parameter
- Existence of the product in the list

**Predicates for method _getProductFromEntries_:**

| Criteria                             | Predicate |
| ------------------------------------ | --------- |
| Validity of string parameter         | Valid     |
|                                      | NULL      |
| Validity of object parameter         | Valid     |
|                                      | NULL      |
| existence of the product in the list | yes       |
|                                      | no        |

**Boundaries**:

Boundaries for method getProductFromEntries:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |

**Combination of predicates**:

Combination of predicates for method getProductFromEntries

| length of product code | existence of the product in the list | Ticketentry is not null | Valid / Invalid | Description of the test case        | JUnit test case |
| ---------------------- | ------------------------------------ | ----------------------- | --------------- | ----------------------------------- | --------------- |
| >0                     | yes                                  | yes                     | valid           | t1("item19,item20,item21","item20") |                 |
| >0                     | yes                                  | no                      | invalid         | t1("item19,item20,item21","item20") |                 |
| ''                     | no                                   | no                      | invalid         | t2("item35,item33,item34","item29") |                 |
| =0                     | \*                                   | \*                      | invalid         | t3("item14,item15,item16","")       |                 |

### **Class _Utils _ - method _readData_**

**Criteria for method _readData_:**

-
-
-
-
- **Predicates for method _readData_:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |

**Boundaries**:

Boundaries for method readData:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |

**Combination of predicates**:

Combination of predicates for method readData

| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
| ---------- | ---------- | --- | --------------- | ---------------------------- | --------------- |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |

### **Class _Utils _ - method _fromFile_**

**Criteria for method _fromFile_:**

-
-
-
-
- **Predicates for method _fromFile_:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |

**Boundaries**:

Boundaries for method fromFile:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |

**Combination of predicates**:

Combination of predicates for method fromFile

| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
| ---------- | ---------- | --- | --------------- | ---------------------------- | --------------- |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |
|            |            |     |                 |                              |                 |

### **Class _Utils _ - method _updateFile_**

**Criteria for method _updateFile_:**

- Validity of the String parameter
- Length of the String   
- Sign of the updated amount parameter
- Existence of the Credit Card 
- File opened correctly 

- **Predicates for method _updateFile_:**

| Criterion                             | Predicate |
| ------------------------------------- | --------- |
| Validity of the String parameter      | Valid     |
|                                       | NULL      |
| Length of the String                  | > 0       |
|                                       | = 0 ("")  |
| Existence of the Credit Card          | Yes       |
|                                       | No        |
| Sign of the updated amount parameter  | >=0       |
|                                       | <0        |
| File opened correctly                 | Yes       |
|                                       | No        |

**Boundaries**:

Boundaries for method updateFile:

| Criteria                              | Boundary values |
| ------------------------------------- | --------------- |
| Sign of the updated amount parameter  | -1,0            |

**Combination of predicates**:

Combination of predicates for method updateFile

| Validity of the String parameter | Length of the String | Existence of the Credit Card | Sign of the updated amount parameter |File opened correctly | Valid/Invalid | Description of the test case: example of input and output | JUnit test case |
| -------------------------------- | -------------------- | ------------------------ | -------------------------------- | ------------------------------ | ------------- | --------------------------------------------------------- | --------------- |
| Valid                            | >0                   | Yes                      | <0                               | Yes                              | Valid       | T0 -> False                                               |testValidUpdateFile()                  |
| ''                               | ''                   | ''                       | >=0                              | *                             | Valid       | T1 -> True                             |testInvalidUpdateFile()                 |
| ''                               | ''                   | ''                       | ''                               | No                            | Invalid         | T2 -> throw IO Exception                             |testInvalidUpdateFile()                 |
| ''                               | ''                   | No                       | ''                                | -                              | Valid       | T3 -> False                                |testInvalidUpdateFile()                 |
| *                                | 0                    | -                        | -                                | -                              | Valid       | T4(""; error)                                             |testInvalidUpdateFile()                 |
| NULL                             | -                    | -                        | -                                | -                              | Valid       | T5(NULL; error)                                           |testInvalidUpdateFile()                 |


# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>

| Unit name               | JUnit test case                 |Java Class     |
| -----------------------:| -------------------------------:| ---:|
|Order             |testGetSetProductCode()           |TestR2_Order |
|Order             |testGetSetPrice()                 |TestR2_Order |
|Order             |testGetSetQuantity()              |TestR2_Order |
|Order             |testGetSetStatus()                |TestR2_Order |
|Order             |testGetSetBalanceID()             |TestR2_Order |
|Order             |testGetSetOrderID()               |TestR2_Order |
|BalanceOperation  |testGetSetBalanceID()             |TestR4_BalanceOperation |
|BalanceOperation  |testGetSetDate()                  |TestR4_BalanceOperation |
|BalanceOperation  |testGetSetMoney()                 |TestR4_BalanceOperation |
|BalanceOperation  |testGetSetType()                  |TestR4_BalanceOperation |
|TicketEntry       |testGetSetBarCode()               |TestR5_TicketEntry      |
|TicketEntry       |testGetSetPrice()                 |TestR5_TicketEntry      |
|TicketEntry       |testGetSetQuantity()              |TestR5_TicketEntry      |
|TicketEntry       |testGetSetProductDescription()    |TestR5_TicketEntry      |
|TicketEntry       |testGetSetDiscountRate()          |TestR5_TicketEntry      |
### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >

### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- | --- |
|           |           |                      |                 |
|           |           |                      |                 |
|           |           |                      |                 |     |

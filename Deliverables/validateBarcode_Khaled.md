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

 ### **Class *Utils * - method *validateBarcode***



**Criteria for method *validateBarcode*:**
	

 - length of code 
  - format of code
  - correct result after the equation


**Predicates for method *validateBarcode*:**

| Criteria | Predicate |
| -------- | --------- |
|length of code|(null,11)|
|          |[12,14]|
|          |[15,maxstring)|
|format of code|true(in case of digits)|
|              |false(others)|
|correct result after the equation|true|
|                                 |false|





**Boundaries**:

Boundaries for method validateBarcode:


| Criteria | Boundary values |
| -------- | --------------- |
|length of code|11,12,14,15|



**Combination of predicates**:

Combination of predicates for method validateBarcode


|  length of code | format of code |correct result after the equation | Valid / Invalid |Description of the test case|JUnit test case|
|-----------------|----------------|-----------------------|-----------------|------------------------------|-----------------|
|(null,11)        |       *        |         *             |Invalid          |T1(542424) -> false           |                 |
|                 |                |                       |                 |Tb1(null) -> false            |                 |
|                 |                |                       |                 |                              |                 |
|[14,maxstring)   |       *        |          *            |Invalid          |T2(4521864284293182) -> false |                 |
|                 |                |                       |                 |                              |                 |
|        *        |   not digit    |          *            |Invalid          |T3(dfjn87154) -> false        |                 |
|                 |                |                       |                 |Tb3(skvidnsfffdnjsfd) -> false|                 |
|        *        |       *        |        false          |Invalid          |                              |                 |
|    [12,14]      |     digits     |        false          |Invalid          |T4(12345678954164) -> false   |                 |
|    [12,14]      |     digits     |        true           |Valid            |T4(8032817681723)    -> true  |                 |



### **Class *Utils * - method *isOnlyDigit***



**Criteria for method *isOnlyDigit*:**
	

 - 
 - 
 -
 -
 -


**Predicates for method *isOnlyDigit*:**

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
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *validateCreditCard***



**Criteria for method *validateCreditCard*:**
	

 - 
 - 
 -
 -
 -


**Predicates for method *validateBvalidateCreditCardarcode*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

Boundaries for method validateCreditCard:


| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |



**Combination of predicates**:

Combination of predicates for method validateCreditCard


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *containsProduct***



**Criteria for method *containsProduct*:**
	

 - 
 - 
 -
 -
 -


**Predicates for method *containsProduct*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

Boundaries for method containsProduct:


| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 | 
|          |                 |
|          |                 |
|          |                 |
|          |                 |



**Combination of predicates**:

Combination of predicates for method containsProduct


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *getProductFromEntries***



**Criteria for method *getProductFromEntries*:**
	

 - 
 -  
 -
 -
 -


**Predicates for method *getProductFromEntries*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |





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


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *readData***



**Criteria for method *readData*:**
	

 - 
 -  
 -
 -
 -


**Predicates for method *readData*:**

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
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *fromFile***



**Criteria for method *fromFile*:**
	

 - 
 -  
 -
 -
 -


**Predicates for method *fromFile*:**

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
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||

### **Class *Utils * - method *updateFile***



**Criteria for method *updateFile*:**
	

 - 
 -  
 -
 -
 -


**Predicates for method *updateFile*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

Boundaries for method updateFile:


| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |
|          |                 |



**Combination of predicates**:

Combination of predicates for method updateFile


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||





# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||




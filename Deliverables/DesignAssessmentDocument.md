# Design assessment


```
<The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved>
```

# Levelized structure map

  <img src="res/structure101/Levelizedstructuremap.png" alt="Levelized structure map"/>

# Structural over complexity chart

  <img src="res/structure101/Structuralovecomplexitychart.png" alt="Structural over complexity chart"/>


# Size metrics



| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  | 5       |
| Classes (outer)                           | 39      |
| Classes (all)                             | 40      |
| NI (number of bytecode instructions)      | 7942    |
| LOC (non comment non blank lines of code) | 3415    |



# Items with XS

```
Our code doesn't contain any fat methods
```

<img src="res/structure101/XS.png" alt="XS"/>



# Package level tangles

```
Our project doesn't contain any recursive dependencies
```

  <img src="res/structure101/Packaglevetangles.png" alt="Package level tangles"/>

# Summary analysis

We have moved Utils class from it.polito.ezshop.utils to it.polito.ezshop.data in order to avoid any recursive dependencies; in the previous case, levelized structure map showed 97% of tangles.

The main differences of the current structure of our project and the design that we delivered on April 30 are:

- The actual design doesn't use any maps because we created DB class "EZShopDB" to manage data persistence.

- EZShopData class is renamed as EZShop and Sale Item is renamed as TicketEntry.

- In the current version we excluded some classes such as AccountBook, Credit, Debit etc. because they were not necessary for our aim.

- Position and fidelity card are not anymore classes because we just stored them as string(in the previous version we had Position and Fidelity card classes).



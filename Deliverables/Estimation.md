# Project Estimation

Authors: Diego Marino, Michele Massetti, Mohamed Shebab, Elisa Tedde

Date: 30/04/2021

Version: 1.0

# Contents

- [Estimate by product decomposition]
- [Estimate by activity decomposition ]

# Estimation approach

# Estimate by product decomposition

###

|                                                                                                         | Estimate |
| ------------------------------------------------------------------------------------------------------- | :------: |
| NC = Estimated number of classes to be developed                                                        |    14    |
| A = Estimated average size per class, in LOC                                                            |   187    |
| S = Estimated size of project, in LOC (= NC \* A)                                                       |   2620   |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)                    |   262    |
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro)                                     |  7860€   |
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) | 2 weeks  |

# Estimate by activity decomposition

###

| Activity name                     | Estimated effort (person hours) |
| --------------------------------- | :-----------------------------: |
| **Requirements**                  |               56                |
| Review existing systems           |                8                |
| Perform work analysis             |                7                |
| Model process                     |                1                |
| Identify user requirements        |               32                |
| Identify performance requirements |                8                |
| **Design**                        |               40                |
| Low-level                         |                3                |
| High-level                        |               21                |
| Traceability Matrix               |                1                |
| Sequence Diagram                  |               15                |
| **Coding**                        |               80                |
| Methods implementation            |               56                |
| Exceptions management             |               24                |
| **Testing**                       |               86                |
| Tests implementation              |               48                |
| Bug fixing                        |               38                |

###

```plantuml
[Requirements] lasts 7 days
[Requirements] is colored in Fuchsia/FireBrick
[Review existing systems] lasts 1 days
 [Perform work analysis] lasts  1 days
 [Model process] lasts 1 days
 [Identify user requirements] lasts 4 days
 [Identify performance requirements] lasts 1 days
[Perform work analysis] starts at [Review existing systems]'s end
[Model process] starts at [Review existing systems]'s end
[Identify user requirements] starts at [Model process]'s end
[Identify performance requirements] starts at [Identify user requirements]'s end

[Design] lasts 5 days
[Design] is colored in Lime/Green
[Low-level] lasts 1 days
[High-level] lasts 3 days
[Traceability Matrix] lasts 1 days
[Sequence Diagram] lasts 2 days
[Design] starts at [Requirements]'s end
[Low-level] starts at [Identify performance requirements]'s end
[High-level] starts at [Identify performance requirements]'s end
[Traceability Matrix] starts at [High-level]'s end
[Sequence Diagram] starts at [High-level]'s end

[Coding] lasts 10 days
[Coding] is colored in Cyan/Blue
[Methods implementation] lasts 7 days
[Exceptions management] lasts 3 days
[Coding] starts at [Design]'s end
[Methods implementation] starts at [Design]'s end
[Exceptions management] starts at [Methods implementation]'s end

[Testing] lasts 11 days
[Testing] is colored in OrangeRed/Red
[Tests implementation] lasts 6 days
[Bug fixing] lasts 5 days
[Testing] starts at [Coding]'s end
[Tests implementation] starts at [Coding]'s end
[Bug fixing] starts at [Tests implementation]'s end


```

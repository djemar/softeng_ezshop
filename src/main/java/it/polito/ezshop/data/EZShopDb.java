package it.polito.ezshop.data;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteUpdateListener;

public class EZShopDb {
    private SQLiteConnection connection = null;
    private String dbUrl = "jdbc:sqlite:ezshop.db";

    public boolean createConnection() {
        try {
            // create a database connection
            this.connection = (SQLiteConnection) DriverManager.getConnection(dbUrl);
            connection.addUpdateListener(new SQLiteUpdateListener() {
                @Override
                public void onUpdate(Type type, String db, String table, long rowId) {
                    System.out.println("OnUpdate: " + type + " " + db + " " + table + " " + rowId);
                    // update data here
                }

            });

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Integer insertUser(UserImpl user) {
        Integer userId = -1;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into users values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, user.getUsername()); // the index refers to the ? in the statement
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getId());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
            userId = (int) pstmt.getGeneratedKeys().getLong(1);

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");

            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("username") + ", id = "
                        + rs.getInt("id") + ", role = " + rs.getString("role"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return userId;
    }

    public void deleteUser(Integer id) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("delete from users where ID = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");

            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("username") + ", id = "
                        + rs.getInt("id") + ", role = " + rs.getString("role"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public List<UserImpl> getAllUsers() {
        List<UserImpl> users = new ArrayList<UserImpl>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                users.add(new UserImpl(rs.getString("username"), rs.getString("password"),
                        rs.getString("role"), rs.getInt("id")));
            }


            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("username") + ", id = "
                        + rs.getInt("id") + ", role = " + rs.getString("role"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return users;
    }

    public UserImpl getUser(Integer id) {
        UserImpl u = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from users where ID = ?");
            ResultSet rs;
            pstmt.setInt(1, id); // Set the Bind Value
            rs = pstmt.executeQuery();
            u = new UserImpl(rs.getString("username"), rs.getString("password"),
                    rs.getString("role"), rs.getInt("id"));
            System.out.println(u.getUsername());

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return u;
    }

    public void updateUserRights(Integer id, String role) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("update users set role = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, role); // the index refers to the ? in the statement
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");

            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("username") + ", id = "
                        + rs.getInt("id") + ", role = " + rs.getString("role"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }

    public void insertProductType(ProductTypeImpl product) {
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("insert into producttypes values(?, ?, ?, ?, ?, ?, ?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setString(1, product.getProductDescription());
            pstmt.setString(2, product.getBarCode());
            pstmt.setInt(3, product.getId());
            pstmt.setString(4, product.getNote());
            pstmt.setString(5, "");
            pstmt.setInt(6, 0);
            pstmt.setDouble(7, product.getPricePerUnit());

            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from  producttypes");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public void updateProductType(Integer id, String newDescription, String newCode,
            double newPrice, String newNote) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "update producttypes set description = (?), productcode = (?),"
                            + " priceperunit = (?), note = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, newDescription); // the index refers to the ? in the statement
            pstmt.setString(2, newCode);
            pstmt.setDouble(3, newPrice);
            pstmt.setString(4, newNote);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }

    public void deleteProductType(Integer id) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("delete from producttypes where ID = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public List<ProductTypeImpl> getAllProductTypes() {
        List<ProductTypeImpl> products = new ArrayList<ProductTypeImpl>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");
            while (rs.next()) {
                products.add(new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                        rs.getString("productCode"), rs.getDouble("priceperunit"),
                        rs.getString("note")));
            }
            products.forEach(x -> System.out.println(x.getId()));
            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;

    }

    public ProductTypeImpl getProductTypeByBarCode(String barCode) {
        ProductTypeImpl p = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from producttypes where ProductCode = ?");
            ResultSet rs;
            pstmt.setString(1, barCode);
            rs = pstmt.executeQuery();
            p = new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                    rs.getString("productCode"), rs.getDouble("priceperunit"),
                    rs.getString("note"));
            System.out.println(p.getProductDescription());

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return p;
    }

    public List<ProductTypeImpl> getProductTypesByDescription(String description) {
        List<ProductTypeImpl> products = new ArrayList<ProductTypeImpl>();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from producttypes where Description = ?");
            ResultSet rs;
            pstmt.setString(1, description);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                        rs.getString("productCode"), rs.getDouble("priceperunit"),
                        rs.getString("note")));
            }
            products.forEach(x -> System.out.println(x.getId()));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;
    }

    public void updateQuantity(Integer productId, int toBeAdded) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "update producttypes set quantity= quantity + (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, toBeAdded); // the index refers to the ? in the statement
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }

    public void updatePosition(Integer productId, String newPos) {
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("update producttypes set location = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, newPos); // the index refers to the ? in the statement
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }

    public boolean insertSaleTransaction(SaleTransactionImpl saleTransaction) {
        // to be called by endSaleTransaction
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into saletransactions values(?, ?, ?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, saleTransaction.getTicketNumber());
            pstmt.setDouble(2, saleTransaction.getDiscountRate());
            pstmt.setDouble(3, saleTransaction.getPrice());

            pstmt.executeUpdate();

            // TODO insert sold products (ticket entry) in ticketentries table

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from saletransactions");

            while (rs.next()) {
                // read the result set
                System.out
                        .println("id = " + rs.getInt("id") + ", price = " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void updateSaleTransaction(Integer transactionId,
            HashMap<String, Integer> returnedProducts) {
        try {
            SaleTransactionImpl saleTransaction = getSaleTransaction(transactionId);
            // get sales
            // update amount
            // update price
            // update db
            PreparedStatement pstmt = connection.prepareStatement(
                    "update ticketentries set amount = (amount-(?)) where transactionid = (?) and productcode = (?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            // TODO wtf
            pstmt.setDouble(2, saleTransaction.getDiscountRate());
            pstmt.setDouble(3, saleTransaction.getPrice());
            pstmt.setInt(3, saleTransaction.getTicketNumber());

            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from saletransactions");

            while (rs.next()) {
                // read the result set
                System.out
                        .println("id = " + rs.getInt("id") + ", price = " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public boolean deleteSaleTransaction(Integer transactionId) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("delete from saletransactions where id=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, transactionId);

            pstmt.executeUpdate();

            // TODO delete sold products in sales table

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from saletransactions");

            while (rs.next()) {
                // read the result set
                System.out
                        .println("id = " + rs.getInt("id") + ", price = " + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }


    public SaleTransactionImpl getSaleTransaction(Integer transactionId) {
        SaleTransactionImpl s = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from saletransactions where id=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, transactionId);

            ResultSet rs;
            rs = pstmt.executeQuery();

            s = new SaleTransactionImpl(transactionId, rs.getDouble("discountrate"),
                    rs.getDouble("price"), rs.getString("status"));

            pstmt = connection.prepareStatement("select * from ticketentries where id=?");
            rs = pstmt.executeQuery();
            List<TicketEntry> entries = new ArrayList<>();
            while (rs.next()) {
                entries.add(new TicketEntryImpl(rs.getString("barcode"),
                        rs.getString("productString"), rs.getInt("amount"),
                        rs.getDouble("priceperunit"), rs.getDouble("discountRate")));
            }
            s.setEntries(entries);


        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return null;
        }
        return s;
    }

    public Integer newReturnTransactionId() {
        Integer id = 1;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select MAX(ID) as ID from returntransactions");
            if (rs.next() == true)
                id = rs.getInt("ID") + 1;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        System.out.println(id);
        return id;
    }


    public boolean insertReturnTransaction(ReturnTransaction returnTransaction) {
        // to be called by endReturnTransaction
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into returntransactions values(?, ?, ?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, returnTransaction.getReturnId());
            pstmt.setInt(2, returnTransaction.getTransactionId());
            pstmt.setBoolean(3, false); // not payed

            int count = pstmt.executeUpdate();
            if (count < 0)
                return false;

            // TODO add returned products to returns table
            // returnTransaction.getReturnedProductsMap()

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from returntransactions");

            while (rs.next()) {
                // read the result set
                System.out.println("id = " + rs.getInt("id") + ", transactionId = "
                        + rs.getInt("transactionid"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteReturnTransaction(Integer returnId) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("delete from returntransactions where id=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, returnId);

            pstmt.executeUpdate();

            // TODO delete returned products in returns table

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from returntransactions");

            while (rs.next()) {
                // read the result set
                System.out.println("id = " + rs.getInt("id") + ", transactionId = "
                        + rs.getInt("transactionid"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public ReturnTransaction getReturnTransaction(Integer returnId) {
        ReturnTransaction r = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from returntransactions where id=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, returnId);

            ResultSet rs;
            rs = pstmt.executeQuery();

            r = new ReturnTransaction(returnId, rs.getInt("transactionid"));

            pstmt = connection.prepareStatement("select * from returnentries where id=?");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                r.addProductToReturn(rs.getString("barcode"), rs.getInt("amount"));
            }


        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return null;
        }
        return r;
    }


    public void insertCreditCard(String creditCard) {
        // to be called by receiveCreditCardPayment
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into creditcards values(?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, creditCard); // the index refers to the ? in the statement
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from creditcards");

            while (rs.next()) {
                // read the result set
                System.out.println("cc id = " + rs.getString("creditcard"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

    }


    public String getCreditCard(String creditCard) {
        // to be called by receiveCreditCardPayment
        String cc = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from creditcards where creditcard=(?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, creditCard); // the index refers to the ? in the statement


            ResultSet rs;
            rs = pstmt.executeQuery();
            cc = rs.getString("creditcard");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return cc;

    }

    public boolean recordBalanceUpdate(double toBeAdded) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into balanceoperation(date, money, type) values=(?,?,?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.

            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setDouble(2, toBeAdded);
            String type = toBeAdded < 0 ? "debit" : "credit";
            pstmt.setString(3, type);
            int count = pstmt.executeUpdate();
            if (count < 0)
                return false;

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from balanceoperations");

            while (rs.next()) {
                // read the result set
                System.out.println("date = " + rs.getDate("date") + ", money = "
                        + rs.getDouble("money") + ", type = " + rs.getString("type"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    public List<BalanceOperation> getAllBalanceOperations(LocalDate from, LocalDate to) {
        // to be called by receiveCreditCardPayment
        List<BalanceOperation> list = new ArrayList<BalanceOperation>();
        try {
            // TODO date column name may cause issues?
            PreparedStatement pstmt;
            if (from == null && to == null) {
                pstmt = connection.prepareStatement("select * from balanceoperation");
            } else if (from == null && to != null) {
                pstmt = connection
                        .prepareStatement("select * from balanceoperation where date<=(?)");
            } else if (from != null && to == null) {
                pstmt = connection
                        .prepareStatement("select * from balanceoperation where date>=(?)");
            } else {
                pstmt = connection.prepareStatement(
                        "select * from balanceoperation where date>=(?) and date<=(?)");
            }
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // date format?
            pstmt.setDate(1, java.sql.Date.valueOf(from)); // the index refers to the ? in the
                                                           // statement
            pstmt.setDate(2, java.sql.Date.valueOf(to)); // the index refers to the ? in the
                                                         // statement

            ResultSet rs;
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // read the result set
                list.add(new BalanceOperationImpl(rs.getInt("id"), rs.getDate("date").toLocalDate(),
                        rs.getDouble("money"), rs.getString("type")));
                return new ArrayList<BalanceOperation>();
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return list;

    }
}

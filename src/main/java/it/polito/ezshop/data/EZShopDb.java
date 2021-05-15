package it.polito.ezshop.data;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
                    "insert into users(username, password, role) values(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, user.getUsername()); // the index refers to the ? in the statement
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
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

    public boolean deleteUser(Integer id) {
        boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                User u = new UserImpl(rs.getString("username"), rs.getString("password"),
                        rs.getString("role"), rs.getInt("id"));
                System.out.println("name = " + rs.getString("username") + ", id = "
                        + rs.getInt("id") + ", role = " + rs.getString("role"));
                users.add(u);
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

    // method to find if a user exists
    public boolean getUserbyName(String username) {
        boolean user = true;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from users where username = ?");
            pstmt.setString(1, username); // Set the Bind Value
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == false)
                user = false;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        System.out.print(user);
        return user;
    }

    public boolean updateUserRights(Integer id, String role) {
        boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;

    }

    public UserImpl checkCredentials(String username, String password) {
        UserImpl u = null;
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("select * from users where username = ? AND password = ?");
            ResultSet rs;
            pstmt.setString(1, username); // Set the Bind Value
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next() == true)
                u = new UserImpl(rs.getString("username"), rs.getString("password"),
                        rs.getString("role"), rs.getInt("id"));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        System.out.print(u);
        return u;

    }

    public Integer insertProductType(ProductTypeImpl product) {
        Integer id = -1;
        try {

            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into producttypes values(null,?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setString(1, product.getProductDescription());
            pstmt.setString(2, product.getBarCode());
            pstmt.setString(3, product.getNote());
            pstmt.setString(4, "");
            pstmt.setInt(5, 0);
            pstmt.setDouble(6, product.getPricePerUnit());

            pstmt.executeUpdate();
            id = (int) pstmt.getGeneratedKeys().getLong(1);

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
        return id;
    }

    public boolean updateProductType(Integer id, String newDescription, String newCode,
            double newPrice, String newNote) {
        boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;
    }

    public ProductTypeImpl getProductTypeById(Integer id) {
        ProductTypeImpl product = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from producttypes where id = ?");
            pstmt.setInt(1, id); // Set the Bind Value
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == true) {
                product = new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                        rs.getString("productCode"), rs.getDouble("priceperunit"),
                        rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
                System.out.print("prodotto con id trovato" + product.getProductDescription());
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return product;
    }

    public boolean deleteProductType(Integer id) {
        boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;
    }

    public List<ProductType> getAllProductTypes() {
        List<ProductType> products = new ArrayList<ProductType>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");
            while (rs.next()) {
                ProductType p = new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                        rs.getString("productCode"), rs.getDouble("priceperunit"),
                        rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
                products.add(p);
            }
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
                    rs.getString("productCode"), rs.getDouble("priceperunit"), rs.getString("note"),
                    rs.getString("location"), rs.getInt("quantity"));
            System.out.println(p.getProductDescription());

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return p;
    }

    // check
    public List<ProductType> getProductTypesByDescription(String descr) {
        List<ProductType> products = new ArrayList<ProductType>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet ris;
            ris = stmt.executeQuery("select description as description from producttypes");
            while (ris.next()) {
                if (ris.getString("description").toLowerCase().contains(descr.toLowerCase()))
                    descr = ris.getString("description");
            }
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from producttypes where Description = ?");
            ResultSet rs;
            pstmt.setString(1, descr);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ProductType p = new ProductTypeImpl(rs.getInt("id"), rs.getString("description"),
                        rs.getString("productCode"), rs.getDouble("priceperunit"),
                        rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
                products.add(p);
            }
            products.forEach(x -> System.out.println("ricerca per descrizione" + x.getId()));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;
    }

    // check
    public boolean updateQuantity(Integer productId, int toBeAdded) {
        boolean negative = true;
        try {
            PreparedStatement ris = connection.prepareStatement(
                    "select quantity as quantity from producttypes where id = (?)");
            ris.setQueryTimeout(30); // set timeout to 30 sec.
            ris.setInt(1, productId);
            ResultSet rs = ris.executeQuery();
            int quant = toBeAdded + rs.getInt("quantity");
            if (quant >= 0) {
                PreparedStatement pstmt = connection
                        .prepareStatement("update producttypes set quantity= (?) where id = (?)");
                pstmt.setQueryTimeout(30); // set timeout to 30 sec.
                pstmt.setInt(1, quant); // the index refers to the ? in the statement
                pstmt.setInt(2, productId);
                pstmt.executeUpdate();
                negative = false;
                System.out.print("maggiore di zero" + quant);
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return negative;
    }

    public boolean updatePosition(Integer productId, String newPos) {
        boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;
    }

    public boolean checkExistingPosition(String position) {
        boolean pos = true;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from producttypes where location = (?)");
            pstmt.setString(1, position); // Set the Bind Value
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == false)
                pos = false;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return pos;
    }

    public Integer insertOrder(OrderImpl order) {
        int id = -1;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into orders values(null, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setString(1, order.getProductCode());
            pstmt.setDouble(2, order.getPricePerUnit());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setString(4, order.getStatus());
            pstmt.setInt(5, order.getBalanceId());

            pstmt.executeUpdate();
            id = (int) pstmt.getGeneratedKeys().getLong(1);
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from orders");

            while (rs.next()) {
                // read the result set
                System.out.println("product Code = " + rs.getString("productCode") + ", id = "
                        + rs.getInt("ID"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return id;
    }

    public boolean resetTables() {
        boolean done = false;
        try {
            PreparedStatement pstmt = connection.prepareStatement("delete from producttypes");
            pstmt.setQueryTimeout(30);
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("delete from saletransactions");
            pstmt.setQueryTimeout(30);
            pstmt.executeUpdate();
            pstmt = connection.prepareStatement("delete from returntransactions");
            pstmt.setQueryTimeout(30);
            pstmt.executeUpdate();
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return done;
    }

    public boolean updateOrder(int orderId, String status, int balanceid) {
        boolean b = false;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "update orders set status = (?), balanceid = (?) where id = (?)");
            // pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, status);
            pstmt.setInt(2, balanceid);
            pstmt.setInt(3, orderId); // the index refers to the ? in the statement
            pstmt.executeUpdate();
            b = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return b;
    }

    public OrderImpl getOrder(Integer orderId) {

        OrderImpl order = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from orders where ID = ?");
            ResultSet rs;
            pstmt.setInt(1, orderId); // Set the Bind Value
            rs = pstmt.executeQuery();
            OrderImpl o = new OrderImpl(rs.getInt("id"), rs.getString("productCode"),
                    rs.getDouble("pricePerUnit"), rs.getInt("quantity"), rs.getString("status"),
                    rs.getInt("balanceId"));
            System.out.println(o.getOrderId());
            order = o;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return order;
    }

    public Double getBalance() {
        Double balance = 0.0;

        List<BalanceOperation> list = getAllBalanceOperations(null, null);
        if (!list.isEmpty())
            balance = list.stream().mapToDouble(item -> item.getMoney()).sum();

        return balance;
    }

    public int getOrderNumber() {
        int i = 0;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select max(id) as number from orders ");
            ResultSet rs;
            rs = pstmt.executeQuery();
            i = rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());

        }
        return i;

    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<Order>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from orders");
            while (rs.next()) {
                orders.add(new OrderImpl(rs.getInt("ID"), rs.getString("ProductCode"),
                        rs.getDouble("PricePerUnit"), rs.getInt("Quantity"), rs.getString("Status"),
                        rs.getInt("balanceID")));
            }
            while (rs.next()) {
                // read the result set
                System.out.println("id = " + rs.getString("ID"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return orders;
    }

    public int getBalanceOperationsNumber() {
        int i = 0;
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("select count(*) as number from balanceOperations ");
            ResultSet rs;
            rs = pstmt.executeQuery();
            i = rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return i;
        }
        return -1;
    }

    public int insertBalanceOperation(BalanceOperationImpl b) {
        int id = -1;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into balanceOperation values(null, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setDate(1, java.sql.Date.valueOf(b.getDate()));
            pstmt.setDouble(2, b.getMoney());
            pstmt.setString(3, b.getType());
            pstmt.executeUpdate();
            id = (int) pstmt.getGeneratedKeys().getLong(1);

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from balanceOperation");

            while (rs.next()) {
                // read the result set
                System.out.println("balance ID = " + rs.getInt("ID"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        return id;
    }

    public int getCustomerNumber() {
        int i = -1;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select count(*) as number from balanceOperation ");
            ResultSet rs;
            rs = pstmt.executeQuery();
            i = rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return i;
    }

    public Integer insertCustomer(Customer c) {
        Integer customerId = -1;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into customers values((?),NULL,0,(?))",
                            Statement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setString(1, c.getCustomerName());
            pstmt.setString(2, "");
            pstmt.executeUpdate();
            customerId = (int) pstmt.getGeneratedKeys().getLong(1);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return customerId;

    }

    public boolean updateCustomer(int id, String name, String fidelityCard, Integer points) {
        boolean boo = false;
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("update customers set name = (?), customerCard = (?),"
                            + " points = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, name); // the index refers to the ? in the statement
            pstmt.setString(2, fidelityCard);
            pstmt.setInt(3, points);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            boo = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return boo;

    }

    public int getCustomerCardNumber() {
        int i = 0;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select count(*) as number from customerCards ");
            ResultSet rs;
            rs = pstmt.executeQuery();
            i = rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return i;
    }

    public boolean insertCustomerCard(String customerCard) {
        boolean boo = false;
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("insert into customercards values((?), null, null)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.s
            pstmt.setString(1, customerCard); // the index refers to the ? in the statement
            pstmt.executeUpdate();


            boo = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return boo;
    }

    // datestare
    public CustomerImpl getCustomer(Integer id) {
        CustomerImpl c = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from customers where id=(?)");
            pstmt.setInt(1, id);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery();
            System.out.println("Customer ID" + rs.getInt("id"));
            if (rs.next() == true)
                c = new CustomerImpl(rs.getInt("id"), rs.getString("name"),
                        rs.getString("customercard"), rs.getInt("points"));
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return c;

    }

    String getCustomerCard(String customerCard) {
        String c = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "select count(*) as number from customerCards where card=(?)");
            pstmt.setString(1, customerCard);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery();
            if (rs.getInt("number") == 1)
                c = customerCard;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return c;
    }

    public boolean attachCardToCustomer(String customerCard, Integer customerId) {
        boolean b = false;
        try {
            Customer c = this.getCustomer(customerId);
            String card = this.getCustomerCard(customerCard);
            if (c == null || card == null)
                return b;
            PreparedStatement pstmt = connection.prepareStatement(
                    "select count(*) as number from customers where customercard=(?)");
            pstmt.setString(1, customerCard);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery();
            if (rs.getInt("number") == 0) {
                b = this.updateCustomer(c.getId(), c.getCustomerName(), customerCard,
                        c.getPoints());

            } else
                b = false;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return b;
    }

    public List<Customer> getAllCustomers() {

        List<Customer> l = new ArrayList<Customer>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from customers");
            while (rs.next()) {
                l.add(new CustomerImpl(rs.getInt("id"), rs.getString("name"),
                        rs.getString("customercard"), rs.getInt("points")));
            }
            l.forEach(x -> System.out.println(x.getId()));
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return l;

    }

    public boolean deleteCustomer(CustomerImpl c) {
        boolean boo = false;
        try {

            PreparedStatement pstmt =
                    connection.prepareStatement("delete from customers where ID = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, c.getId());
            pstmt.executeUpdate();

            boo = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return boo;
    }


    public CustomerImpl getCustomerByCard(String customerCard) {
        CustomerImpl c = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from customers where customercard=(?)");
            pstmt.setString(1, customerCard);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery();
            if (rs.next() == true)
                c = new CustomerImpl(rs.getInt("id"), rs.getString("name"),
                        rs.getString("customercard"), rs.getInt("points"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return c;
    }


    public int SaleTransactionNumber() {
        Integer i = 0;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select max(id) as number from saletransactions ");
            ResultSet rs;
            rs = pstmt.executeQuery();
            i = rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return i;

    }

    public boolean insertSaleTransaction(SaleTransactionImpl saleTransaction) {
        // to be called by endSaleTransaction
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into saletransactions values(?, ?, ?, ?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, saleTransaction.getTicketNumber());
            pstmt.setDouble(2, saleTransaction.getDiscountRate());
            pstmt.setDouble(3, saleTransaction.getPrice());
            pstmt.setString(4, "CLOSED");

            pstmt.executeUpdate();

            saleTransaction.getEntries().stream().forEach(t -> {
                try {
                    PreparedStatement pstm = connection
                            .prepareStatement("insert into ticketentries values (?,?,?,?,?,?)");

                    pstm.setQueryTimeout(30); // set timeout to 30 sec.
                    pstm.setInt(1, saleTransaction.getTicketNumber());
                    pstm.setInt(2, t.getAmount());
                    pstm.setString(3, t.getBarCode());
                    pstm.setDouble(4, t.getPricePerUnit());
                    pstm.setDouble(5, t.getDiscountRate());
                    pstm.setString(6, t.getProductDescription());

                    pstm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            });

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

    public boolean payForSaleTransaction(Integer transactionID) {
        boolean done = false;
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("update saletransactions set status = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, "PAYED"); // the index refers to the ? in the statement
            pstmt.setInt(2, transactionID);
            pstmt.executeUpdate();

            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return done;
    }

    public boolean setSaleDiscount(Integer transactionId, Double discountRate) {
        // method that update discountrate and sale trans price
        boolean done = false;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "update saletransactions set discountrate = (?), price = price * (1 - (?)) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setDouble(1, discountRate); // the index refers to the ? in the statement
            pstmt.setDouble(2, discountRate);
            pstmt.setInt(3, transactionId);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from saletransactions");

            while (rs.next()) {
                // read the result set
                System.out.println("description = " + rs.getString("description") + ", id = "
                        + rs.getInt("ID") + ", price = " + rs.getDouble("priceperunit"));
            }
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return done;
    }

    public boolean updateSaleTransaction(Integer transactionId,
            HashMap<String, Integer> returnedProducts, double diffPrice, boolean added) {
        try {
            // SaleTransactionImpl saleTransaction = getSaleTransaction(transactionId);
            // get sales
            // update amount
            // update price
            // update db

            // query to update sale transaction price
            PreparedStatement pstmt = connection.prepareStatement(
                    "update saletransactions set price = price - (?) where id = (?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setDouble(1, diffPrice);
            pstmt.setDouble(2, transactionId);
            pstmt.executeUpdate();

            // query to update amount ticket
            returnedProducts.entrySet().stream().forEach(x -> {
                try {
                    PreparedStatement pstm = connection.prepareStatement(
                            "update ticketentries set amount = (amount-(?)) where transactionid = (?) and productcode = (?)");

                    pstm.setQueryTimeout(30); // set timeout to 30 sec.
                    // the index refers to the ? in the statement
                    // TODO wtf
                    // nel caso di deletereturn devo riaggiungere amount mentre in endreturn devo
                    // togliere amount
                    pstm.setInt(1, added ? -x.getValue() : x.getValue());
                    pstm.setInt(2, transactionId);
                    pstm.setString(3, x.getKey());

                    pstm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

            });
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

    public boolean deleteSaleTransaction(Integer transactionId) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("delete from saletransactions where id=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, transactionId);

            pstmt.executeUpdate();

            // delete sold products in sales table
            pstmt = connection
                    .prepareStatement("delete from ticketentries where transactionid = (?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, transactionId);


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
            return false;
        }
        return true;
    }


    public SaleTransactionImpl getSaleTransaction(Integer transactionId) {
        SaleTransactionImpl s = null;
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("select * from saletransactions where ID=?");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, transactionId);

            ResultSet rs;
            rs = pstmt.executeQuery();

            s = new SaleTransactionImpl(transactionId, rs.getDouble("discountrate"),
                    rs.getDouble("price"), rs.getString("status"));

            pstmt = connection
                    .prepareStatement("select * from ticketentries where transactionid=?");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // the index refers to the ? in the statement
            pstmt.setInt(1, transactionId);
            rs = pstmt.executeQuery();
            List<TicketEntry> entries = new ArrayList<>();
            while (rs.next()) {
                entries.add(new TicketEntryImpl(rs.getString("productCode"),
                        rs.getString("productDescription"), rs.getInt("amount"),
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
        Integer id = -1;
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
            PreparedStatement pstmt = connection
                    .prepareStatement("insert into returntransactions values(?, ?, ?, ?)");

            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1, returnTransaction.getReturnId());
            pstmt.setInt(2, returnTransaction.getTransactionId());
            pstmt.setString(3, returnTransaction.getStatus());
            pstmt.setDouble(4, returnTransaction.getTotal());

            int count = pstmt.executeUpdate();
            if (count < 0)
                return false;

            // TODO add returned products to returns table
            // returnTransaction.getReturnedProductsMap()
            returnTransaction.getReturnedProductsMap().entrySet().stream().forEach(x -> {
                PreparedStatement pstm;
                try {
                    pstm = connection.prepareStatement("insert into returnentries values(?, ?, ?)");

                    pstm.setQueryTimeout(30); // set timeout to 30 sec.
                    pstm.setInt(1, returnTransaction.getReturnId());
                    pstm.setString(2, x.getKey());
                    pstm.setInt(3, x.getValue());
                    pstm.executeUpdate();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
            pstmt.setInt(1, returnId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                r.addProductToReturn(rs.getString("productcode"), rs.getInt("amount"));
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
            PreparedStatement pstmt = connection
                    .prepareStatement("insert into balanceoperation values (null, ?,?,?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.

            pstmt.setDate(1, Date.valueOf(LocalDate.now().toString()));
            pstmt.setDouble(2, toBeAdded);
            String type = toBeAdded < 0 ? "debit" : "credit";
            pstmt.setString(3, type);
            int count = pstmt.executeUpdate();
            /*
             * if (count < 0) return false;
             */

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from balanceoperation");

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
                pstmt.setDate(1, java.sql.Date.valueOf(to.toString()));
            } else if (from != null && to == null) {
                pstmt = connection
                        .prepareStatement("select * from balanceoperation where date>=(?)");
                pstmt.setDate(1, java.sql.Date.valueOf(from.toString()));
            } else {
                pstmt = connection.prepareStatement(
                        "select * from balanceoperation where data>=(?) and date<=(?)");
                pstmt.setDate(1, java.sql.Date.valueOf(from.toString()));
                pstmt.setDate(2, java.sql.Date.valueOf(to.toString()));
            }
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            // date format?

            ResultSet rs;
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // read the result set
                list.add(new BalanceOperationImpl(rs.getInt("id"), rs.getDate("date").toLocalDate(),
                        rs.getDouble("money"), rs.getString("type")));
            }

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return list;

    }



}


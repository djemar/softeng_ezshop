package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteUpdateListener;

public class EZShopDb {
    private SQLiteConnection connection = null;
    private String dbUrl = "jdbc:sqlite:ezshop.db";

    public void createConnection() {
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
        }
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    public void insertUser(UserImpl user) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into users values(?, ?, ?, ?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, user.getUsername()); // the index refers to the ? in the statement
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getId());
            pstmt.setString(4, user.getRole());
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
    	List<UserImpl> users= new ArrayList<UserImpl>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
            	users.add(new UserImpl(rs.getString("username"),rs.getString("password"),rs.getString("role"),rs.getInt("id")));
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
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users where ID = " + id);
            u= new UserImpl(rs.getString("username"),rs.getString("password"),rs.getString("role"),rs.getInt("id"));
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
            PreparedStatement pstmt = connection.prepareStatement("update users set role = (?) where id = (?)");
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
            PreparedStatement pstmt =
                    connection.prepareStatement("insert into producttypes values(?, ?, ?, ?, ?, ?, ?)");
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
    public void updateProductType(Integer id, String newDescription, String newCode, double newPrice, String newNote) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("update producttypes set description = (?), productcode = (?),"
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

    public List<ProductTypeImpl> getAllProductTypes(){
    	List<ProductTypeImpl> products= new ArrayList<ProductTypeImpl>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");
            while (rs.next()) {
            	products.add(new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note")));
            }
            products.forEach(x->System.out.println(x.getId()));
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
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes where ProductCode = " + barCode);
            p= new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note"));
            System.out.println(p.getProductDescription());
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return p;
    }
    
    /*da rivedere*/
    public List<ProductTypeImpl> getProductTypesByDescription(String description){
    	List<ProductTypeImpl> products= new ArrayList<ProductTypeImpl>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes where Description = " + description);
            while (rs.next()) {
            	products.add(new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note")));
            }
            products.forEach(x->System.out.println(x.getId()));
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;
    }
}

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

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    public Integer insertUser(UserImpl user) {
        Integer userId = -1;
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into users(username, password, role) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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
            PreparedStatement pstmt = connection.prepareStatement("delete from users where ID = (?)");
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
    	List<User> users= new ArrayList<User>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
            	User u = new UserImpl(rs.getString("username"),rs.getString("password"),rs.getString("role"),rs.getInt("id"));
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
        	PreparedStatement pstmt = connection.prepareStatement("select * from users where ID = ?");
        	ResultSet rs;
        	pstmt.setInt(1, id);  // Set the Bind Value
        	rs = pstmt.executeQuery(); 
            u= new UserImpl(rs.getString("username"),rs.getString("password"),rs.getString("role"),rs.getInt("id"));
            System.out.println(u.getUsername());
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return u;
    }
    //method to find if a user exists
    public boolean getUserbyName(String username) {
    	boolean user = true;
        try {
        	PreparedStatement pstmt = connection.prepareStatement("select * from users where username = ?");
        	pstmt.setString(1, username);  // Set the Bind Value
        	ResultSet rs = pstmt.executeQuery();
        	if(rs.next() == false)
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
        	PreparedStatement pstmt = connection.prepareStatement("select * from users where username = ? AND password = ?");
        	ResultSet rs;
        	pstmt.setString(1, username);  // Set the Bind Value
        	pstmt.setString(2, password); 
        	rs = pstmt.executeQuery(); 
        	if(rs.next() == true) 
        		u= new UserImpl(rs.getString("username"),rs.getString("password"),rs.getString("role"),rs.getInt("id"));
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        System.out.print(u);
        return u;
    	
    }
    public Integer getUserId() {
    	Integer id = 1;
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select MAX(ID) as ID from users");
            if(rs.next() == true) 
                id = rs.getInt("ID") + 1;
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	System.out.println("prossimo id" +id);
        return id;
    	
    }
    
    public Integer insertProductType(ProductTypeImpl product) {
    	Integer id = -1;
        try {
        	
            PreparedStatement pstmt = connection.prepareStatement("insert into producttypes values(null,?, ?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
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
    public boolean updateProductType(Integer id, String newDescription, String newCode, double newPrice, String newNote) {
    	boolean done = false;
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
            done = true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	return done;
    }
    public Integer getProductId() {
    	Integer id = 1;
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select MAX(ID) as ID from producttypes");
            if(rs.next() == true) 
                id = rs.getInt("ID") + 1;
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	System.out.println(id);
        return id;
    }
    //check
    public ProductTypeImpl getProductTypeById(Integer id) {
    	ProductTypeImpl product = null;
        try {
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where id = ?");
        	pstmt.setInt(1, id);  // Set the Bind Value
        	ResultSet rs = pstmt.executeQuery();
        	if(rs.next() == true) {
        		product = new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),
        				                rs.getDouble("priceperunit"),rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
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
            PreparedStatement pstmt = connection.prepareStatement("delete from producttypes where ID = (?)");
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
    public List<ProductType> getAllProductTypes(){
    	List<ProductType> products= new ArrayList<ProductType>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from producttypes");
            while (rs.next()) {
        		ProductType p = new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),
        				rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
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
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where ProductCode = ?");
        	ResultSet rs;
        	pstmt.setString (1, barCode);  
        	rs = pstmt.executeQuery(); 
            p= new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),
            		rs.getDouble("priceperunit"),rs.getString("note"), rs.getString("location"), rs.getInt("quantity"));
            System.out.println(p.getProductDescription());
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return p;
    }
    //check
    public List<ProductType> getProductTypesByDescription(String descr){
    	List<ProductType> products= new ArrayList<ProductType>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet ris;
            ris = stmt.executeQuery("select description as description from producttypes");
            while (ris.next()) {
            	if(ris.getString("description").toLowerCase().contains(descr.toLowerCase()))
            		descr = ris.getString("description");
            }
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where Description = ?");
        	ResultSet rs;
        	pstmt.setString(1, descr); 
        	rs = pstmt.executeQuery(); 
        	while (rs.next()) {
        		ProductType p = new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),
        				rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note"),rs.getString("location"), rs.getInt("quantity"));
            	products.add(p);
            }
            products.forEach(x->System.out.println("ricerca per descrizione" + x.getId()));
            
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;
    }
    //check
    public boolean updateQuantity(Integer productId, int toBeAdded) {
    	boolean negative = true;
        try {
			PreparedStatement ris = connection.prepareStatement("select quantity as quantity from producttypes where id = (?)");
			ris.setQueryTimeout(30); // set timeout to 30 sec.
			ris.setInt(1, productId);
			ResultSet rs = ris.executeQuery(); 
			int quant = toBeAdded + rs.getInt("quantity");
			if(quant > 0) {
				PreparedStatement pstmt = connection.prepareStatement("update producttypes set quantity= (?) where id = (?)");
				pstmt.setQueryTimeout(30); // set timeout to 30 sec.
				pstmt.setInt(1, quant); // the index refers to the ? in the statement
				pstmt.setInt(2, productId);
				pstmt.executeUpdate();
				negative = false;
				System.out.print("maggiore di zero"+ quant);
			}
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	return negative;
    }
    public boolean updatePosition(Integer productId, String newPos) {
    	boolean done = false;
        try {
			PreparedStatement pstmt = connection.prepareStatement("update producttypes set location = (?) where id = (?)");
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
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where location = (?)");
        	pstmt.setString(1, position);  // Set the Bind Value
        	ResultSet rs = pstmt.executeQuery();
        	if(rs.next() == false)
        		pos = false;
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return pos;
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

    
    
    
    
    
    
    
    
    
    
    
}

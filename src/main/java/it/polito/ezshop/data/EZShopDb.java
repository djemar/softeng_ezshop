package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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
            PreparedStatement pstmt = connection.prepareStatement("insert into users values(?, ?, ?, ?)");
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
            PreparedStatement pstmt = connection.prepareStatement("insert into producttypes values(?, ?, ?, ?, ?, ?, ?)");
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
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where ProductCode = ?");
        	ResultSet rs;
        	pstmt.setString (1, barCode);  
        	rs = pstmt.executeQuery(); 
            p= new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note"));
            System.out.println(p.getProductDescription());
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return p;
    }
    
    public List<ProductTypeImpl> getProductTypesByDescription(String description){
    	List<ProductTypeImpl> products= new ArrayList<ProductTypeImpl>();
        try {
        	PreparedStatement pstmt = connection.prepareStatement("select * from producttypes where Description = ?");
        	ResultSet rs;
        	pstmt.setString(1, description); 
        	rs = pstmt.executeQuery(); 
        	while (rs.next()) {
            	products.add(new ProductTypeImpl(rs.getInt("id"),rs.getString("description"),rs.getString("productCode"),rs.getDouble("priceperunit"),rs.getString("note")));
            }
            products.forEach(x->System.out.println(x.getId()));
            
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return products;
    }
    public void updateQuantity(Integer productId, int toBeAdded) {
        try {
			PreparedStatement pstmt = connection.prepareStatement("update producttypes set quantity= quantity + (?) where id = (?)");
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

        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	
    }
    public void updatePosition(Integer productId, String newPos) {
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

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	
    }
    public void insertOrder(OrderImpl order) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into orders values(?, ?, ?, ?, ?, ?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
             // the index refers to the ? in the statement
            pstmt.setInt(1, order.getOrderId());
            pstmt.setString(2, order.getProductCode());
            pstmt.setDouble(3, order.getPricePerUnit());
            pstmt.setInt(4, order.getQuantity());
            pstmt.setString(5, order.getStatus());
            pstmt.setInt(6, order.getBalanceId());
            
            pstmt.executeUpdate();

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
    }
    public void updateOrder(int orderId, String productCode, Double pricePerUnit, int quantity, String status, Integer balanceId) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("update orders set productCode = (?), pricePerUnit = (?),"
            													+ " quantity = (?), status = (?),balanceId= (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1,productCode); // the index refers to the ? in the statement
            pstmt.setDouble(2, pricePerUnit);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, status);
            pstmt.setInt(5, balanceId);
            pstmt.setInt(6, orderId);
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from orderds");

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
    	
    }
    public OrderImpl getOrder(Integer orderId){

        OrderImpl order=null;
        try {
        	PreparedStatement pstmt = connection.prepareStatement("select * from users where ID = ?");
        	ResultSet rs;
        	pstmt.setInt(1, orderId);  // Set the Bind Value
        	rs = pstmt.executeQuery(); 
            OrderImpl o = new OrderImpl(rs.getInt("orderId"), rs.getString("productCode"),rs.getDouble("pricePerUnit"),rs.getInt("quantity"),rs.getString("status"),rs.getInt("balanceId"));
            System.out.println(o.getOrderId());
            order=o;
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return order;
    }
    public Double getBalance(){
        Double balance=0.0;
        try {
        	PreparedStatement pstmt = connection.prepareStatement("select * from balanceOperations ");
        	ResultSet rs;
        	rs = pstmt.executeQuery(); 
            while (rs.next()) {
                balance+=rs.getDouble("money");
            }
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return balance;
    }

    public int getOrderNumber() {
        int i=0;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select count(*) as number from orders ");
        	ResultSet rs;
        	rs = pstmt.executeQuery(); 
            i=rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return i;
        }
        return -1;

    }
    public List<Order> getAllOrders() {
        List<Order> orders= new ArrayList<Order>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from orders");
            while (rs.next()) {
            	orders.add(new OrderImpl(rs.getInt("ID"),rs.getString("ProductCode"),rs.getDouble("PricePerUnit"),rs.getInt("Quantity"),rs.getString("Status"),rs.getInt("balanceID")));
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

    public int getBalnceOperationsNumber() {
        int i=0;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select count(*) as number from balanceOperations ");
        	ResultSet rs;
        	rs = pstmt.executeQuery(); 
            i=rs.getInt("number");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return i;
        }
        return -1;
    }

    public void insertBalanceOperation(BalanceOperationImpl b) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into balance values(?, ?, ?, ?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
             // the index refers to the ? in the statement
            pstmt.setInt(1, b.getBalanceId());
            pstmt.setDate(2,java.sql.Date.valueOf(b.getDate()) );
            pstmt.setDouble(3, b.getMoney());
            pstmt.setString(4, b.getType());        
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from balanceOperations");

            while (rs.next()) {
                // read the result set
                System.out.println("balance ID = " 
                        + rs.getInt("ID"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }
    public int getCustomerNumber(){
        int i=-1;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select count(*) as number from balanceOperations ");
        	ResultSet rs;
        	rs = pstmt.executeQuery(); 
            i=rs.getInt("number");
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return i;
    }
    public void insertCustomer(String name,int id,String card,int points) {
        
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into customers values(?, ?, ?, ?, ?, ?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
             // the index refers to the ? in the statement
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            pstmt.setString(3, card);
            pstmt.setInt(4, points);
            
            
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from customers");

            while (rs.next()) {
                // read the result set
                System.out.println("customer name = " + rs.getString("Name") + ", id = "
                        + rs.getInt("ID"));
            }
            
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        
    }
    public void updateCustomer(int id,String name,String fidelityCard, int points) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("update customers set name = (?), FidelityCard = (?),"
            													+ " points = (?) where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1,name); // the index refers to the ? in the statement
            pstmt.setString(2, fidelityCard);
            pstmt.setInt(3, points); pstmt.setInt(3, points);
            pstmt.setInt(4, id);
            
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from customers");

            while (rs.next()) {
                // read the result set
                System.out.println("product Code = " + rs.getString("name") + ", id = "
                        + rs.getInt("ID"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	
    }

    public int getCustomerCardNumber() {
        int i=-1;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select count(*) as number from customerCards ");
        	ResultSet rs;
        	rs = pstmt.executeQuery(); 
            i=rs.getInt("number");
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return i;
    }

    public void insertCustomerCard(String customerCard) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into customercards values(?, NULL, NULL)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setString(1, customerCard); // the index refers to the ? in the statement
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from users");

            while (rs.next()) {
                // read the result set
                System.out.println("card number  = " + rs.getString("card"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public Customer getCustomer(Integer id){
        Customer c=null;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select * from customers where id=?");
            pstmt.setInt(1, id);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery(); 
            System.out.println("Customer ID"+rs.getInt("card"));
            if(!rs.getString("name").equals(null))
                c=new CustomerImpl(rs.getString("name"), rs.getInt("id"),rs.getString("card"));
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return c;

    }
    CustomerCard getCustomerCard(String customerCard){
        CustomerCard c=null;
        try{
            PreparedStatement pstmt = connection.prepareStatement("select * from customerCards where card=?");
            pstmt.setString(1, customerCard);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery(); 
            System.out.println("Customer Card"+rs.getInt("card"));
            if(!rs.getString("name").equals(null))
                c= new CustomerCard(customerCard);
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return c;
    }
    public boolean attachCardToCustomer(String customerCard, Integer customerId) {
        boolean b=false;
        try{
            Customer c=this.getCustomer(customerId);
            CustomerCard card=this.getCustomerCard(customerCard);
            if(c==null || card ==null)
                return b;
            PreparedStatement pstmt = connection.prepareStatement("select card from customers where card=?");
            pstmt.setString(1, customerCard);
            pstmt.setQueryTimeout(30);
            ResultSet rs;
            rs = pstmt.executeQuery(); 
            if(rs.getString("CustomerCard")==null)  {  
                this.updateCustomer(c.getId(), c.getCustomerName(),customerCard, c.getPoints());
                this.updateCard(customerId,customerCard,card.getPoints());
                b=true;
            }
            else
                b=false;
            
        }catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return b;
    }

    public void updateCard(Integer customerId, String customerCard,int points) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("update customercards set customer (?), points = (?),where id = (?)");
            pstmt.setQueryTimeout(30); // set timeout to 30 sec.
            pstmt.setInt(1,customerId); // the index refers to the ? in the statement
            pstmt.setInt(2, points);
            pstmt.setString(3, customerCard);
            
            
            pstmt.executeUpdate();

            Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from customers");

            while (rs.next()) {
                // read the result set
                System.out.println("product Code = " + rs.getString("name") + ", id = "
                        + rs.getInt("ID"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    	

    }

    public List<Customer> getAllCustomer() {

        List<Customer> l= new ArrayList<Customer>();
        try {
        	Statement stmt = connection.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select * from customers");
            while (rs.next()) {
            	l.add(new CustomerImpl(rs.getString("name"),rs.getInt("id"),rs.getString("card")));
            }
            l.forEach(x->System.out.println(x.getId()));
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
    
}
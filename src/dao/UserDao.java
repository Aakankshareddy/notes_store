package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import util.DBUtil;

public class UserDao {
	
	Connection connection;
	
	public UserDao() {		
        try {
			connection = DBUtil.getMySqlConnection();
			//System.out.println("connection made in userdao/userdao object made");
		} catch (Exception e) {			
			e.printStackTrace();
		}
    }	
	
	public boolean authenticate(String email, String password) {				
		try {				
			String query = "SELECT * FROM user WHERE email=? AND password_hash=?";
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, email);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			//System.out.println("Query executed in dao authenticate");
			if(rs.next()) {
				//System.out.println("rs.next in dao authenticate");
				return true;
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
				
		return false;
	}
	
	public User getInfo(String email) {		
		String query = "SELECT user_id, first_name, last_name, branch, current_year, password_hash FROM user WHERE email = ?";		
		try {
			PreparedStatement pst = connection.prepareStatement(query);			
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();			
			if(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getInt("user_id"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setEmail(email);
				user.setBranch(rs.getString("branch"));
				user.setCurrent_year(rs.getInt("current_year"));
				user.setPassword_hash(rs.getString("password_hash"));
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;		
	}
	
	//returns true if email already registered
	public boolean checkUser(String email) {				
		String query = "SELECT * FROM user WHERE email=?";
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) 
				return true;				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean registerUser(String first_name, String last_name, String email, String branch, int current_year, String password_hash) {
		String query = "INSERT INTO user(first_name, last_name, email, branch, current_year, password_hash) VALUES(?,?,?,?,?,?)";
		PreparedStatement pst;
		try {
			pst = connection.prepareStatement(query);
			pst.setString(1, first_name);
			pst.setString(2, last_name);
			pst.setString(3, email);
			pst.setString(4, branch);
			pst.setInt(5, current_year);
			pst.setString(6, password_hash);
			
			int num = pst.executeUpdate();
			if(num>0)
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
		
}

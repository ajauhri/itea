/* Component: Users
 * Adds new user records in users table, and updates the username and password field.
 * Functions available:
 * 		insert(gr_no, user_type, (DateBean)dob)
 * 		Adds a record. Username and password are set to Gr no. and DoB by default. DoB must be a DateBean.
 * 
 * 		updateGrNo(oldgr_no,newgr_no)
 * 		Updates old gr_no to new gr_no. Even in username, if it hasn't been changed.
 * 
 * 		updateDoB(gr_no, datefield, new value)
 * 		Updates the DoB in the password, if it hasn't been changed. Complex, involving encryption of new
 * 		dob. Datefield is either "dd", "mm" or "yyyy" depending on which field is to be updated.
 */

package components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.DBConnector;
import beans.DateBean;

public class Users {
	public static void insert(String gr_no, String user_type, DateBean db) {
		/* Convert password to md5 hash */
		String password = db.toString();
		password = MD5Hash.generateMD(password);
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Insert */
		String query = "insert into pietons.users(username, password, user_type, gr_no, theme, changed) values(?,?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, gr_no);
			ps.setString(2, password);
			ps.setString(3, user_type);
			ps.setString(4, gr_no);
			ps.setInt(5, 0);
			ps.setInt(6, 0);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}

	}

	public static void updateDoB(String gr_no, String dateField, String value) {
		String user_type = "";
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Check if user has changed the username & password */
		int changed = 0;
		String query = "select changed, user_type from pietons.users where gr_no=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, gr_no);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				changed = rs.getInt("changed");
				user_type = rs.getString("user_type");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return;
		}
		if (changed == 0) {
			/* If not changed, update the password */
			/* Get original dob */
			DateBean db = new DateBean();
			if (user_type.equals("student")) {
				String query1 = "select x.dd as dd, x.mm as mm, x.yyyy as yyyy from pietons.student s,"
						+ " xmltable('$p/personal_details/dob' passing s.personal_details as \"p\""
						+ " columns dd varchar(2) path 'dd/text()',"
						+ "		mm varchar(2) path 'mm/text()',"
						+ "		yyyy varchar(4) path 'yyyy/text()') as x"
						+ " where s.gr_no=?";
				try {
					PreparedStatement ps1 = conn.prepareStatement(query1);
					ps1.setString(1, gr_no);
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						db.setDd(rs1.getString("dd"));
						db.setMm(rs1.getString("mm"));
						db.setYyyy(rs1.getString("yyyy"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			} else if (user_type.equals("faculty") || user_type.equals("hod")
					|| user_type.equals("dean")) {
				String query1 = "select x.dd as dd, x.mm as mm, x.yyyy as yyyy from pietons.faculty f,"
						+ " xmltable('$p/personal_details/dob' passing f.personal_details as \"p\""
						+ " columns dd varchar(2) path 'dd/text()',"
						+ "		mm varchar(2) path 'mm/text()',"
						+ "		yyyy varchar(4) path 'yyyy/text()') as x"
						+ " where f.gr_no=?";
				try {
					PreparedStatement ps1 = conn.prepareStatement(query1);
					ps1.setString(1, gr_no);
					ResultSet rs1 = ps1.executeQuery();
					if (rs1.next()) {
						db.setDd(rs1.getString("dd"));
						db.setMm(rs1.getString("mm"));
						db.setYyyy(rs1.getString("yyyy"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
				}
			}
			/* Make changes in dob */
			if (dateField.equals("dd"))
				db.setDd(value);
			else if (dateField.equals("mm"))
				db.setMm(value);
			else if (dateField.equals("yyyy"))
				db.setYyyy(value);
			/* Get new password */
			String password = db.toString();
			password = MD5Hash.generateMD(password);
			/* Update users table */
			String query2 = "update pietons.users set password=? where gr_no=?";
			try {
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.setString(1, password);
				ps2.setString(2, gr_no);
				ps2.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
	}

	public static void updateGrNo(String oldGr_no, String newGr_no) {
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Check if user has changed the username & password */
		int changed = 0;
		String query = "select changed from pietons.users where gr_no=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, oldGr_no);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				changed = rs.getInt("changed");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		if (changed == 1) {
			/* If changed only update gr_no */
			String query1 = "update pietons.users set gr_no=? where gr_no=?";
			try {
				PreparedStatement ps1 = conn.prepareStatement(query1);
				ps1.setString(1, newGr_no);
				ps1.setString(2, oldGr_no);
				ps1.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		} else {
			/* Else update the username too */
			String query1 = "update pietons.users set gr_no=?, username=? where gr_no=?";
			try {
				PreparedStatement ps1 = conn.prepareStatement(query1);
				ps1.setString(1, newGr_no);
				ps1.setString(2, newGr_no);
				ps1.setString(3, oldGr_no);
				ps1.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
		}
	}
}

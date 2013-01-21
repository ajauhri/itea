/* Component: GrNo
 * Generates Gr no.s and can check whether a gr no. is already used or available.
 * Functions generateStudent(class_id,year_of_admission), generateFaculty(branch_id,year_of_joining) and generateDean(year_of_joining)
 *  generate Gr no.s for student, faculty and dean, respectively.
 * Function isNew(gr_no) returns true if the gr no is not used before, false if it is a duplicate. 
 * 
 * Usage:
 * Since these are static functions, no need to create an instance of the class.
 * 			String gr_no=GrNo.generateStudent(class_id,year_of_admission);
 * 			boolean b=GrNo.isNew(gr_no);
 * 
 * Basic Gr. No. format (10 characters):
 * 			student: SCB2008001
 * 					{S}{Course Initial}{Branch Initial}{Year of Admission}{Three Digit Sequential Number}
 * 			faculty: FCB2005001
 * 					{F}{Course Initial}{Branch Initial}{Year of Joining}{Three Digit Sequential Number}
 * 			dean: D002005001
 * 					{D}{00}{Year of Joining}{Three Digit Sequential Number}
 * 
 */

package components;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.DBConnector;

public class GrNo {
	public static String generateStudent(int class_id, int year_of_admission) {
		String gr_no = "S";
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Get Course & Branch Initials */
		String query = "select c.name as course_name, b.name as branch_name"
				+ " from pietons.class l, pietons.branch b, pietons.course c"
				+ " where l.id=? and b.id=l.branch_id and c.id=b.course_id";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, class_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				gr_no += rs.getString("course_name").toUpperCase().charAt(0);
				gr_no += rs.getString("branch_name").toUpperCase().charAt(0);
			} else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		/* Add Year of Admission */
		if (year_of_admission < 10)
			gr_no += "000" + year_of_admission;
		else if (year_of_admission < 100)
			gr_no += "00" + year_of_admission;
		else if (year_of_admission < 1000)
			gr_no += "0" + year_of_admission;
		else
			gr_no += year_of_admission;
		/* Get last sequential no. */
		int seqNo = 0;
		String grNo="";
		String query1 = "select gr_no from pietons.student order by gr_no";
		try {
			PreparedStatement ps1 = conn.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				grNo=rs1.getString("gr_no");
				if(grNo.length()==10 && grNo.substring(0,7).equals(gr_no)){
					// matching gr no.
					try{ // try to obtain the sequential number (last 3 characters)
						seqNo=Integer.parseInt(grNo.substring(8,10));
					}catch(NumberFormatException e){} // last 3 chars are not a number - retain original seqNo
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		seqNo++;
		if (seqNo < 10)
			gr_no += "00" + seqNo;
		else if (seqNo < 100)
			gr_no += "0" + seqNo;
		else
			gr_no += seqNo;
		return gr_no;
	}
	public static String generateFaculty(int branch_id, int year_of_joining) {
		String gr_no = "F";
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Get Course & Branch Initials */
		String query = "select c.name as course_name, b.name as branch_name"
				+ " from pietons.branch b, pietons.course c"
				+ " where b.id=? and c.id=b.course_id";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, branch_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				gr_no += rs.getString("course_name").toUpperCase().charAt(0);
				gr_no += rs.getString("branch_name").toUpperCase().charAt(0);
			} else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		/* Add Year of Admission */
		if (year_of_joining < 10)
			gr_no += "000" + year_of_joining;
		else if (year_of_joining < 100)
			gr_no += "00" + year_of_joining;
		else if (year_of_joining < 1000)
			gr_no += "0" + year_of_joining;
		else
			gr_no += year_of_joining;
		/* Get last sequential no. */
		int seqNo = 0;
		String grNo="";
		String query1 = "select gr_no from pietons.faculty order by gr_no";
		try {
			PreparedStatement ps1 = conn.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				grNo=rs1.getString("gr_no");
				if(grNo.length()==10 && grNo.substring(0,7).equals(gr_no)){
					// matching gr no.
					try{ // try to obtain the sequential number (last 3 characters)
						seqNo=Integer.parseInt(grNo.substring(8,10));
					}catch(NumberFormatException e){} // last 3 chars are not a number - retain original seqNo
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		seqNo++;
		if (seqNo < 10)
			gr_no += "00" + seqNo;
		else if (seqNo < 100)
			gr_no += "0" + seqNo;
		else
			gr_no += seqNo;
		return gr_no;
	}
	public static String generateDean(int year_of_joining) {
		String gr_no = "F";
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Get Course & Branch Initials */
		gr_no += "00";
		/* Add Year of Admission */
		if (year_of_joining < 10)
			gr_no += "000" + year_of_joining;
		else if (year_of_joining < 100)
			gr_no += "00" + year_of_joining;
		else if (year_of_joining < 1000)
			gr_no += "0" + year_of_joining;
		else
			gr_no += year_of_joining;
		/* Get last sequential no. */
		int seqNo = 0;
		String grNo="";
		String query1 = "select gr_no from pietons.faculty order by gr_no";
		try {
			PreparedStatement ps1 = conn.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				grNo=rs1.getString("gr_no");
				if(grNo.length()==10 && grNo.substring(0,7).equals(gr_no)){
					// matching gr no.
					try{ // try to obtain the sequential number (last 3 characters)
						seqNo=Integer.parseInt(grNo.substring(8,10));
					}catch(NumberFormatException e){} // last 3 chars are not a number - retain original seqNo
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return null;
		}
		seqNo++;
		if (seqNo < 10)
			gr_no += "00" + seqNo;
		else if (seqNo < 100)
			gr_no += "0" + seqNo;
		else
			gr_no += seqNo;
		return gr_no;
	}
	public static boolean isNew(String gr_no) {
		/* Get Connection */
		DBConnector dbcon = new DBConnector();
		Connection conn = dbcon.getConnection();
		/* Check if gr_no exists */
		String query = "select * from pietons.users where gr_no=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, gr_no);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next())
				return false;
			else
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return false;
		}
	}
}

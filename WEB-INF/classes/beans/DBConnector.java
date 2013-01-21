/* Component: DBConnector
 * Connects to the datasource jdbc/DataSource by default.
 * For changing the datasource name, the changes need to be made only on this page for the entire project.
 * 
 * Usage:
// 		/* Get connection */
//		HttpSession session = request.getSession();
//		Connection conn = null;
//		synchronized (session) {
//			DBConnector dbcon = (DBConnector) session.getAttribute("dbcon"); // fetch
//			// an
//			// existing
//			// instance
//			if (dbcon == null) { // if none found, create a new one
//				dbcon = new DBConnector();
//				session.setAttribute("dbcon", dbcon);
//			}
//			conn = dbcon.getConnection();
//		}
/*
 * 
 */

package beans;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnector {
	Connection connection;
	public DBConnector(){
		try {
			connection=((DataSource)(new InitialContext()).lookup("java:comp/env/jdbc/DataSource")).getConnection();
		} catch (SQLException e) {
			connection=null;
		} catch (NamingException e) {
			connection=null;
		}
	}
	public Connection getConnection(){
		return connection;
	}
}

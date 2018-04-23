package instagram.project;

import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InstaDB {

	Connection connection = null;
	String nameTabelFollowers; // Name tabel is  followers_tabel_ + index 
	String nameTabelFollowing;// Name tabel is  following_tabel_ + index 
	String nameTabelStatistic; // Name tabel with statistic
	String userDB = "root_insta";
	String passwordDB = "admin";
	String addresServerDB = "jdbc:mysql://107.180.50.233/u0463036_instagr";
	Statement statement;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	ResultSet rs;
	int idMyPage;
	int curentNumberDays;
	String currentDate;
	List <String> listWithAllPage;
	public  InstaDB (String nameMyPage) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
		statement = connection.createStatement();
		statement.setQueryTimeout(30);

		rs = statement.executeQuery("SELECT idMyPage FROM  tabel_with_id  WHERE nameMyPage LIKE '%" + nameMyPage + "%';");

		// Search My page in table_with _id , if not add
		if(!rs.next()) {

			// Insert new name page in tabel tabel_with_id
			statement.executeUpdate("INSERT INTO tabel_with_id ( nameMyPage )"
					+ "VALUES ('" + nameMyPage +"')" );

			// Get ID my page
			rs = statement.executeQuery("SELECT idMyPage FROM  tabel_with_id "
					+ " WHERE nameMyPage LIKE '%" + nameMyPage + "%';");

			// Get name Tabels
			rs.next();
			idMyPage = rs.getInt("idMyPage");
			nameTabelFollowers = "followers_tabel_" + rs.getInt("idMyPage");
			nameTabelFollowing = "following_tabel_" + rs.getInt("idMyPage");
			nameTabelStatistic = "statistic_tabel_" + rs.getInt("idMyPage");

		} else {

			// Get Name Tabels

			rs = statement.executeQuery("SELECT idMyPage FROM  tabel_with_id  WHERE nameMyPage LIKE '%" + nameMyPage + "%';");
			rs.next();
			idMyPage = rs.getInt("idMyPage");
			nameTabelFollowers = "followers_tabel_" + rs.getInt("idMyPage");
			nameTabelFollowing = "following_tabel_" + rs.getInt("idMyPage");
			nameTabelStatistic = "statistic_tabel_" + rs.getInt("idMyPage");
		}
        
		delleteHistory();
		//Histrorry Tabel 
		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
				+ "VALUES ('Start Process' , '" + currentDate + "'," + idMyPage +" )" );

		// Verificate if exist tabel Followers
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS " 
				+ nameTabelFollowers 
				+" ( idPage INTEGER AUTO_INCREMENT,"
				+ " namePage varchar(255) NOT NULL, "
				+ "PRIMARY KEY (idPage))");

		// Verificate if exist tabel Following
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS "
				+ nameTabelFollowing 
				+ " ( idPage INTEGER AUTO_INCREMENT, "
				+ "namePage varchar(255) NOT NULL, "
				+ "countFollowing INTEGER, "
				+ "dateLastFollowing DATETIME, "
				+ "PRIMARY KEY (idPage))");



		// Verificate if exist tabel  Statistic
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS "
				+ nameTabelStatistic
				+ " ( idStatistic INTEGER AUTO_INCREMENT, "
				+ "dateStatisti INTEGER, "
				+ "numberFollowers INTEGER, "
				+ "numberFollowing INTEGER , "
				+ "PRIMARY KEY (idStatistic))");

		rs.close();
		statement.close();
		connection.close();

	}


    public void delleteHistory() throws SQLException {
    	connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
    	String sql = "DELETE FROM history_tabel WHERE idPage = " + idMyPage;
		java.sql.PreparedStatement prepared_statement = connection.prepareStatement(sql);
		prepared_statement.executeUpdate();
		
    }
	public int addFollowersDB(String namePage) {
        
		curentNumberDays = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('Add Follower DB' , '" + currentDate + "'," + idMyPage +" )" );

			rs = statement.executeQuery("SELECT namePage FROM " + nameTabelFollowers + " WHERE namePage LIKE '%" + namePage + "%';");
			if(!rs.next()) {
				statement.executeUpdate("INSERT INTO " + nameTabelFollowers + "( namePage ) "
						+ "VALUES ('" + namePage +"')" );
				System.out.println("Capcelica");
			} else {
				rs.close();
				statement.close();
				connection.close();
				return 1;

			}
			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;

		}


		return 0;


	}

	public int addFollowingDB(String namePage) throws ParseException, InterruptedException {

		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
             
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('Add Following DB' , '" + currentDate + "'," + idMyPage +" )" );
			// Verificat if this person is my follower
			rs = statement.executeQuery("SELECT namePage FROM " + nameTabelFollowers + " WHERE namePage LIKE '%" + namePage + "%';");
			if(rs.next()) {
				rs.close();
				statement.close();
				connection.close();
				return 1;
			}

			rs = statement.executeQuery("SELECT countFollowing, dateLastFollowing  FROM " + nameTabelFollowing + " WHERE namePage LIKE '%" + namePage + "%';");
			if(!rs.next()) {
				statement.executeUpdate("INSERT INTO " + nameTabelFollowing + "( namePage, countFollowing, dateLastFollowing) "
						+ "VALUES ('" + namePage + "', 1 , '" + currentDate + "')" );
				rs.close();
				statement.close();
				connection.close();
				return 0;
			}

			/*int countFollowing = rs.getInt("countFollowing");
			Date dateLastFollowing = simpleDateFormat.parse(rs.getString("dateLastFollowing"));
			Date dateNow = simpleDateFormat.parse(currentDate);
			long diferentTime = (dateNow.getTime() - dateLastFollowing.getTime()) / (3600 * 1000);
			if( (countFollowing < 5 ) &&  ( diferentTime > 30 * 24 ) ) {

				// add new countFollowing and curentNumberDays
				countFollowing ++;
				statement.executeUpdate("UPDATE " + nameTabelFollowing + " SET countFollowing =" + countFollowing 
						+ ", dateLastFollowing ='" + currentDate 
						+ "' WHERE namePage LIKE '%" + namePage + "%';" );
				System.out.println(namePage +" | "+ diferentTime + " | " + countFollowing);
				rs.close();
				statement.close();
				connection.close();
				return 0;	
			} 

			rs.close();
			statement.close();
			connection.close(); */
		} catch (SQLException e) {
			// if erro data base 
			e.printStackTrace();
			return -1;
		}

		return 1;


	}

	public int addStatisticDB( int numberFollowers, int numberFollowing)  {

		curentNumberDays = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('Add Statistic' , '" + currentDate + "'," + idMyPage +" )" );
			statement.executeUpdate("INSERT INTO "
					+ nameTabelStatistic
					+ "(dateStatisti, numberFollowers, numberFollowing ) "
					+ "VALUES (" + curentNumberDays + "," + numberFollowers +"," + numberFollowing + ")" );

			rs.close();
			statement.close();
			connection.close();
			return  0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	public int getNumberFollowers(int date)  {

		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);

			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			rs = statement.executeQuery("SELECT numberFollowers FROM " + nameTabelStatistic + " WHERE dateStatisti=" + date + ";");
			if(rs.next()) {
				rs.close();
				statement.close();
				connection.close();
				return rs.getInt("numberFollower");
			}

			rs.close();
			statement.close();
			connection.close();
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

	public int getNumberFollowing(int date) {
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			rs = statement.executeQuery("SELECT numberFollowing FROM " + nameTabelStatistic + " WHERE dateStatisti=" + date + ";");
			if(rs.next()) {
				rs.close();
				statement.close();
				connection.close();
				return rs.getInt("numberFollowing");
			}

			else {
				rs.close();
				statement.close();
				connection.close();
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	public int getIfReadyUnfollow(String namePage) throws  ParseException {

		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		Date dateNow = simpleDateFormat.parse(currentDate);
		Date dateMinus24h = DateUtils.addHours(dateNow, -24);
		String dateMinus = simpleDateFormat.format(dateMinus24h);
		System.out.println(dateMinus);
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);

			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('If ready UnFollow DB' , '" + currentDate + "'," + idMyPage +" )" );
			rs = statement.executeQuery("SELECT namePage FROM " + nameTabelFollowing + " WHERE namePage LIKE '%" + namePage + "%';");

			if(rs.next()) {
				rs = statement.executeQuery("SELECT namePage FROM " + nameTabelFollowing + " WHERE namePage LIKE '%" + namePage + "%' "
						+ "AND dateLastFollowing >='" + dateMinus + "';");
				if(rs.next()) {
					rs.close();
					statement.close();
					connection.close();
					return 0;
				}
				else{
					rs.close();
					statement.close();
					connection.close();
					return 1;
				}
			}else {
				rs.close();
				statement.close();
				connection.close();
				return 1;

			}

		} catch (SQLException e) {
			e.printStackTrace();

			return -1;
		}



	}

	public int getNumberFollowing24h () throws ParseException {

		int numberFollowing24h;
		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		Date dateNow = simpleDateFormat.parse(currentDate);
		Date dateMinus24h = DateUtils.addHours(dateNow, -24);
		String dateMinus = simpleDateFormat.format(dateMinus24h);
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('Get number following 24h' , '" + currentDate + "'," + idMyPage +" )" );

			System.out.println("Curent date :" + currentDate + "Date minus:" + dateMinus );
			rs = statement.executeQuery("SELECT COUNT(*) FROM " + nameTabelFollowing +
					" WHERE dateLastFollowing <='" +currentDate +"' AND dateLastFollowing >='" + dateMinus+ "';");
			if(rs.next()) {
				numberFollowing24h = rs.getInt(1);
				System.out.println(numberFollowing24h);
				rs.close();
				statement.close();
				connection.close();
				return numberFollowing24h;
			}
			rs.close();
			statement.close();
			connection.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	public int  getListPage(){

		listWithAllPage = new ArrayList<String>();
		try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);

			statement = connection.createStatement();
			statement.setQueryTimeout(30);
			currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
			statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
					+ "VALUES ('Get List Page' , '" + currentDate + "'," + idMyPage +" )" );
			rs = statement.executeQuery("SELECT namePage FROM tabel_with_pages WHERE idMyPage=" +idMyPage + ";");

			while(rs.next())
			{
				listWithAllPage.add(rs.getString("namePage"));

			}
			rs.close();
			statement.close();
			connection.close();
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int verificateAbonament() throws SQLException {

		curentNumberDays = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
		
		statement = connection.createStatement();
		statement.setQueryTimeout(30);
		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
				+ "VALUES ('Verificate Abonament' , '" + currentDate + "'," + idMyPage +" )" );
		rs = statement.executeQuery("SELECT numberDays , dateStart FROM tabel_with_id WHERE idMyPage ="+ idMyPage + ";");
		int numberDays = 0;
		int dateStart = 0;
		if(rs.next()) {
			numberDays = rs.getInt("numberDays");
			dateStart = rs.getInt("dateStart");	
		}
		rs.close();
		statement.close();
		connection.close();
		int number = numberDays + dateStart;
		return number - curentNumberDays;
	}

	public int putNewNamePage(String namePage) throws SQLException {

		connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
		statement = connection.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("INSERT INTO history_tabel(typeAction, dateAction,idPage) "
				+ "VALUES ('Put new name page' , '" + currentDate + "'," + idMyPage +" )" );
		rs = statement.executeQuery("SELECT namePage FROM tabel_with_pages WHERE namePage LIKE '%" + namePage + "%' AND idMyPage=" + idMyPage + ";");
		if(!rs.next()) {
			statement.executeUpdate("INSERT INTO tabel_with_pages(namePage, idMyPage) "
					+ "VALUES ('" + namePage + "'," + idMyPage + ")" );

			rs.close();
			statement.close();
			connection.close();
			return 0;
		}

		rs.close();
		statement.close();
		connection.close();
		return 1;
	}

	public int deleteNamePage(String namePage) throws SQLException {

		//try {
			connection = DriverManager.getConnection(addresServerDB, userDB, passwordDB);
			String sql = "DELETE FROM tabel_with_pages WHERE namePage LIKE '%" + namePage + "%' AND idMyPage = " + idMyPage;
			java.sql.PreparedStatement prepared_statement = connection.prepareStatement(sql);
			prepared_statement.executeUpdate();
			connection.close();
			return 0;
		//} catch (SQLException e) {

		//	return -1;
		//}

	}
}

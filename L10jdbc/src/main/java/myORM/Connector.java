package myORM;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private String dbName = "";
    private String userName = "";
    private String userPassword = "";

    Connector(String dbName, String userName, String userPassword){
        this.dbName = dbName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    Connection getConnection() {
        try {
            Driver driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            String url = "jdbc:mysql://" +       //db type
                    "localhost:" +               //host name
                    "3306/" +                    //port
                    dbName +"?" +               //db name
                    "user="+userName+"&" +              //login
                    "password="+userPassword+"&" +          //password
                    "useSSL=false&" +              //do not use Secure Sockets Layer
                    "useUnicode=true&" +
                    "useJDBCCompliantTimezoneShift=true&" +
                    "useLegacyDatetimeCode=false&" +
                    "serverTimezone=UTC";

            return DriverManager.getConnection(url);
        } catch (SQLException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                IllegalAccessException |
                ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

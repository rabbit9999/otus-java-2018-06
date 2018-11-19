package myORM;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    static Connection getConnection(String dbName, String userName, String userPassword) {
        try {
            Driver driver = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            String url = "jdbc:mysql://" +       //db type
                    "localhost:" +               //host name
                    "3306/" +                    //port
                    dbName +"?" +               //db name
                    "user="+userName+"&" +              //login
                    "password="+userPassword+"&" +          //password
                    "useSSL=false";              //do not use Secure Sockets Layer


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

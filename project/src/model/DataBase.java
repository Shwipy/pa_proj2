package model;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Class used to open and close the access to the DataBase
 */
public class DataBase {

    Connection conn = null;
    Statement st = null;

    /**
     *Initializes the db connection
     * Loads the PostgreSQL driver and connects with the db using the credentials in the db.properties file
     */
    public DataBase() {
        try {
            Class.forName("org.postgresql.Driver");

            // Ler ficheiro properties
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("db.properties");
            props.load(fis);
            
            //https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            this.conn = DriverManager.getConnection(url, user, password);
            this.st = conn.createStatement();

        } catch (Exception e) {
            System.out.println("Erro na ligação à base de dados");
            e.printStackTrace();
        }

        if (st == null) {
            System.out.println("Deu asneira");
        }
    }

    /**
     * Closes the db connection
     * This method when called will proceed to close an active db connection
     */
    public void closeDb(){

        if (st != null) { // statement
            try { st.close(); } catch (Exception e) {
                System.err.println("!! Exception returning statement !!\n"+e);
            }
        }
        if (conn != null) { // connection
            try { conn.close(); } catch (Exception e) {
                System.err.println("!! Exception closing DB connection !!\n"+e);
            }
        }
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getSt() {
        return st;
    }
}

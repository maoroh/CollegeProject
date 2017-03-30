package com.DB;
import java.sql.*;

public class SQLiteJDBC
{
  public static void connect()
  {
    Connection c = null;
    try {
      //Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:handRehabDB.db");
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Opened database successfully");
  }
}
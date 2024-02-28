package com.ppxxd.bankingsystem;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {

    static String DB_URL = "jdbc:sqlite:card.s3db";

    public static void databaseCreation() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS card(" +
                                "id INTEGER NOT NULL," +
                                "number TEXT NOT NULL," +
                                "pin TEXT NOT NULL," +
                                "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void databaseFill(int id, String digits, String pin, long balance) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);
        try (Connection con = dataSource.getConnection()) {
            String sql = "INSERT INTO card VALUES (?,?,?,?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql,
                         Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, String.format("%d", id));
                pstmt.setString(2, digits);
                pstmt.setString(3, pin);
                pstmt.setString(4, String.format("%d", balance));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Card findCard(String digits) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet result = statement.executeQuery(
                        "SELECT * FROM card WHERE number = " + digits);
                return getCard(result);
            } catch (SQLException e) {
                System.out.println("Error executing a query");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from file");
            e.printStackTrace();
        }
        return null;
    }

    private static Card getCard(ResultSet result) throws SQLException {
        if (result.next()) {
            int id_db = result.getInt("id");
            String number_db = result.getString("number");
            String pin_db = result.getString("pin");
            long balance = result.getLong("balance");
            return new Card(id_db, number_db, pin_db, balance);
        } else {
            return null;
        }
    }

    public static Card findCard(String digits, String pin) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet result = statement.executeQuery(
                        "SELECT * FROM card WHERE number = " + digits + " and pin = " + pin);
                return getCard(result);
            } catch (SQLException e) {
                System.out.println("Error executing a query");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from file");
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteRow(Card card) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "DELETE FROM main.card WHERE id = " + card.getID());
            } catch (SQLException e) {
                System.out.println("Error reading from file");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from file");
            e.printStackTrace();
        }
    }

    public static void updateBalance(Card card) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(DB_URL);
        try (Connection con = dataSource.getConnection()) {
            String sql = "UPDATE card SET balance = ? WHERE number = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, String.format("%d", card.getBalance()));
                pstmt.setString(2, card.getDigits());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error executing a query");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from file");
            e.printStackTrace();
        }
    }
}

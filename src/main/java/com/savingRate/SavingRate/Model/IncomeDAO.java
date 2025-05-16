package com.savingRate.SavingRate.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    public static void insertIncome(Income income) {
        String sql = "INSERT INTO income (date, description, value) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, income.getDate());
            stmt.setString(2, income.getDescription());
            stmt.setDouble(3, income.getValue());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Income> getAllIncome() {
        List<Income> list = new ArrayList<>();
        String sql = "SELECT * FROM income";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Income income = new Income();
                income.setId(rs.getInt("id"));
                income.setDate(rs.getDate("date"));
                income.setDescription(rs.getString("description"));
                income.setValue(rs.getDouble("value"));
                list.add(income);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteIncome(int id) {
        String sql = "DELETE FROM income WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateIncome(Income income) {
        String sql = "UPDATE income SET date = ?, description = ?, value = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, income.getDate());
            stmt.setString(2, income.getDescription());
            stmt.setDouble(3, income.getValue());
            stmt.setInt(4, income.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

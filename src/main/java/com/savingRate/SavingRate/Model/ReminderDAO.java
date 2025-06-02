package com.savingRate.SavingRate.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAO {

    public static void saveReminder(Reminder reminder) {
        String sql = "INSERT INTO reminders (type, date, description, cost) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reminder.getType());
            stmt.setDate(2, Date.valueOf(reminder.getDate()));
            stmt.setString(3, reminder.getDescription());
            stmt.setDouble(4, reminder.getCost());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error saving reminder: " + e.getMessage());
        }
    }

    public static List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT * FROM reminders ORDER BY date ASC";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reminders.add(new Reminder(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("description"),
                        rs.getDouble("cost")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error loading reminders: " + e.getMessage());
        }

        return reminders;
    }

    public static void deleteReminder(int id) {
        String sql = "DELETE FROM reminders WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error deleting reminder: " + e.getMessage());
        }
    }
}
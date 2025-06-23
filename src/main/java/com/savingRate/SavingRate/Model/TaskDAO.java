package com.savingRate.SavingRate.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private final Connection conn;

    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

    public void addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, due_date, priority, completed) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getDueDate()));
            stmt.setString(4, task.getPriority());
            stmt.setBoolean(5, false);
            stmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY due_date ASC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getString("priority"),
                        rs.getBoolean("completed")
                );
                tasks.add(task);
            }
        }

        return tasks;
    }

    public void markAsCompleted(int taskId) throws SQLException {
        String sql = "UPDATE tasks SET completed = true WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }
}

package com.savingRate.SavingRate.Controller;

import com.savingRate.SavingRate.Model.Task;
import com.savingRate.SavingRate.Model.TaskDAO;
import com.savingRate.SavingRate.Model.DatabaseConnection;
import com.savingRate.SavingRate.Utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TaskController {

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<String> priorityBox;
    @FXML private ComboBox<String> filterBox;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleCol;
    @FXML private TableColumn<Task, String> descCol;
    @FXML private TableColumn<Task, LocalDate> dueDateCol;
    @FXML private TableColumn<Task, String> priorityCol;
    @FXML private TableColumn<Task, String> remainingCol;
    @FXML private Button addTaskButton;
    @FXML private Button markDoneButton;

    private TaskDAO taskDAO;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Database Error", "Connection failed.");
            return;
        }

        taskDAO = new TaskDAO(conn);
        priorityBox.setItems(FXCollections.observableArrayList("High", "Medium", "Low"));

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        remainingCol.setCellValueFactory(new PropertyValueFactory<>("remaining"));

        priorityCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(priority);
                    getStyleClass().removeAll("priority-high", "priority-medium", "priority-low");

                    switch (priority.toLowerCase()) {
                        case "high" -> getStyleClass().add("priority-high");
                        case "medium" -> getStyleClass().add("priority-medium");
                        case "low" -> getStyleClass().add("priority-low");
                    }
                }
            }
        });

        filterBox.setItems(FXCollections.observableArrayList(
                "All Tasks", "Today", "This Week", "Overdue", "High Priority", "Medium Priority", "Low Priority"
        ));
        filterBox.getSelectionModel().selectFirst();

        loadTasks();
    }

    @FXML
    private void onAddTask() {
        String title = titleField.getText();
        String desc = descField.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        String priority = priorityBox.getValue();

        if (title.isEmpty() || dueDate == null || priority == null) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            Task task = new Task(0, title, desc, dueDate, priority, false);
            taskDAO.addTask(task);
            CustomAlert.showSuccess("Task added successfully.");
            clearForm();
            loadTasks();
        } catch (SQLException e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add task.");
        }
    }

    @FXML
    private void onMarkDone() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CustomAlert.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a task.");
            return;
        }

        boolean confirm = CustomAlert.showConfirmation("Mark as Done", "Are you sure you want to complete this task?");
        if (!confirm) return;

        try {
            taskDAO.markAsCompleted(selected.getId());
            CustomAlert.showSuccess("Task marked as completed.");
            loadTasks();
        } catch (SQLException e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update task.");
        }
    }

    @FXML
    private void onFilterChange() {
        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        try {
            LocalDate today = LocalDate.now();
            String selectedFilter = filterBox.getSelectionModel().getSelectedItem();

            for (Task task : taskDAO.getAllTasks()) {
                LocalDate due = task.getDueDate();
                long daysLeft = ChronoUnit.DAYS.between(today, due);
                String remaining = (daysLeft < 0) ? "Overdue" : (daysLeft == 0) ? "Today" : daysLeft + " days left";
                task.setRemaining(remaining);

                boolean show = switch (selectedFilter) {
                    case "Today" -> due.equals(today);
                    case "This Week" -> !due.isBefore(today) && !due.isAfter(today.plusDays(6));
                    case "Overdue" -> due.isBefore(today);
                    case "High Priority" -> task.getPriority().equalsIgnoreCase("High");
                    case "Medium Priority" -> task.getPriority().equalsIgnoreCase("Medium");
                    case "Low Priority" -> task.getPriority().equalsIgnoreCase("Low");
                    default -> true;
                };

                if (show && !task.isCompleted()) {
                    taskList.add(task);
                }
            }

            taskTable.setItems(taskList);
        } catch (SQLException e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load tasks.");
        }
    }

    private void clearForm() {
        titleField.clear();
        descField.clear();
        dueDatePicker.setValue(null);
        priorityBox.getSelectionModel().clearSelection();
    }
}
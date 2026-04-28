import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManagerAPI extends JFrame {
    private List<Task> tasks = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);
    
    private JTextArea outputArea;
    private JTextField idField;
    private JTextField textField;
    private JCheckBox completedCheckBox;

    public TaskManagerAPI() {
        setTitle("Task Manager REST API");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initUI();
        setVisible(true);
    }

    private void initUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        inputPanel.add(new JLabel("Task ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        inputPanel.add(new JLabel("Task Text:"));
        textField = new JTextField();
        inputPanel.add(textField);
        
        inputPanel.add(new JLabel("Completed:"));
        completedCheckBox = new JCheckBox();
        inputPanel.add(completedCheckBox);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        

        JButton createBtn = new JButton("CREATE (POST)");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTask();
            }
        });

        JButton readBtn = new JButton("READ ALL (GET)");
        readBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readAllTasks();
            }
        });
        
        JButton updateBtn = new JButton("UPDATE (PUT)");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });
        
        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
        
        buttonPanel.add(createBtn);
        buttonPanel.add(readBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        outputArea.append("=== Task Manager REST API Started ===\n");
        outputArea.append("Available commands:\n");
        outputArea.append("1. CREATE (POST) - Add new task\n");
        outputArea.append("2. READ ALL (GET) - Show all tasks\n");
        outputArea.append("3. UPDATE (PUT) - Update task by ID\n");
        outputArea.append("4. DELETE - Delete task by ID\n\n");
    }

    private void createTask() {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            showError("Task text cannot be empty!");
            return;
        }
        
        Task newTask = new Task(idCounter.getAndIncrement(), text, false);
        tasks.add(newTask);
        
        outputArea.append("CREATE SUCCESS: Task created with ID " + newTask.getId() + "\n");
        outputArea.append("   Text: " + newTask.getText() + "\n");
        outputArea.append("   Completed: " + newTask.isCompleted() + "\n\n");
        
        clearFields();
    }

    private void readAllTasks() {
        if (tasks.isEmpty()) {
            outputArea.append(" No tasks found. Create some tasks first!\n\n");
            return;
        }
        
        outputArea.append(" ALL TASKS:\n");
        for (Task task : tasks) {
            outputArea.append("   ID: " + task.getId() + 
                            " | Text: " + task.getText() + 
                            " | Completed: " + task.isCompleted() + "\n");
        }
        outputArea.append("Total tasks: " + tasks.size() + "\n\n");
    }

    private void updateTask() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showError("Please enter Task ID to update!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            Task taskToUpdate = findTaskById(id);
            
            if (taskToUpdate == null) {
                showError("Task with ID " + id + " not found!");
                return;
            }
            
            String newText = textField.getText().trim();
            if (!newText.isEmpty()) {
                taskToUpdate.setText(newText);
            }
            
            taskToUpdate.setCompleted(completedCheckBox.isSelected());
            
            outputArea.append("UPDATE SUCCESS: Task " + id + " updated\n");
            outputArea.append("   New Text: " + taskToUpdate.getText() + "\n");
            outputArea.append("   Completed: " + taskToUpdate.isCompleted() + "\n\n");
            
            clearFields();
            
        } catch (NumberFormatException e) {
            showError("Invalid Task ID format!");
        }
    }

    private void deleteTask() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            showError("Please enter Task ID to delete!");
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            Task taskToDelete = findTaskById(id);
            
            if (taskToDelete == null) {
                showError("Task with ID " + id + " not found!");
                return;
            }
            
            tasks.remove(taskToDelete);
            outputArea.append(" DELETE SUCCESS: Task " + id + " removed\n\n");
            clearFields();
            
        } catch (NumberFormatException e) {
            showError("Invalid Task ID format!");
        }
    }

    private Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    private void clearFields() {
        idField.setText("");
        textField.setText("");
        completedCheckBox.setSelected(false);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Task model class
    private static class Task {
        private int id;
        private String text;
        private boolean completed;

        public Task(int id, String text, boolean completed) {
            this.id = id;
            this.text = text;
            this.completed = completed;
        }

        public int getId() { return id; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskManagerAPI();
            }
        });
    }
}

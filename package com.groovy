package com.example;
import com.example.model.Task;
import com.example.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Spark;
import static spark.Spark.*;

public class TodoApplication {
    private static TodoService todoService = new TodoService();
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        // Настройка порта (по умолчанию 4567)
        port(4567);
        
        // Включаем CORS для тестирования из браузера
        enableCORS();
        
        System.out.println("🚀 REST API сервер запущен на http://localhost:4567");
        
        // 1. CREATE - Добавить задачу (POST /tasks)
        post("/tasks", (request, response) -> {
            response.type("application/json");
            
            try {
                String text = request.queryParams("text");
                if (text == null || text.trim().isEmpty()) {
                    response.status(400);
                    return "{\"error\": \"Текст задачи не может быть пустым\"}";
                }
                
                Task newTask = todoService.addTask(text.trim());
                response.status(201); // Created
                return objectMapper.writeValueAsString(newTask);
                
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"Ошибка сервера: \" + e.getMessage() + \"}";
            }
        });
        
        // 2. READ - Получить все задачи (GET /tasks)
        get("/tasks", (request, response) -> {
            response.type("application/json");
            try {
                return objectMapper.writeValueAsString(todoService.getAllTasks());
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"Ошибка сервера\"}";
            }
        });
        
        // 3. UPDATE - Обновить задачу (PUT /tasks/:id)
        put("/tasks/:id", (request, response) -> {
            response.type("application/json");
            
            try {
                int id = Integer.parseInt(request.params(":id"));
                String newText = request.queryParams("text");
                String completedParam = request.queryParams("completed");
                Boolean completed = completedParam != null ? Boolean.parseBoolean(completedParam) : null;
                
                Task updatedTask = todoService.updateTask(id, newText, completed);
                
                if (updatedTask == null) {
                    response.status(404);
                    return "{\"error\": \"Задача с ID \" + id + \" не найдена\"}";
                }
                
                return objectMapper.writeValueAsString(updatedTask);
                
            } catch (NumberFormatException e) {
                response.status(400);
                return "{\"error\": \"Неверный формат ID\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"Ошибка сервера\"}";
            }
        });
        
        // 4. DELETE - Удалить задачу (DELETE /tasks/:id)
        delete("/tasks/:id", (request, response) -> {
            response.type("application/json");
            
            try {
                int id = Integer.parseInt(request.params(":id"));
                boolean deleted = todoService.deleteTask(id);
                
                if (!deleted) {
                    response.status(404);
                    return "{\"error\": \"Задача с ID \" + id + \" не найдена\"}";
                }
                
                response.status(200);
                return "{\"message\": \"Задача успешно удалена\"}";
                
            } catch (NumberFormatException e) {
                response.status(400);
                return "{\"error\": \"Неверный формат ID\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"error\": \"Ошибка сервера\"}";
            }
        });
        
        // Обработка несуществующих маршрутов
        notFound((request, response) -> {
            response.type("application/json");
            response.status(404);
            return "{\"error\": \"Маршрут не найден\"}";
        });
    }
    

    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            
            return "OK";
        });
        
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        });
    }
}

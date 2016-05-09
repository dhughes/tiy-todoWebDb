
import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.halt;

/**
 * Created by doug on 5/9/16.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        // create a server
        //Server server = Server.createWebServer().start();
        Server server = Server.createTcpServer("-baseDir", "./data").start();

        // create our connection
        //Connection connection = DriverManager.getConnection("jdbc:h2:./data/todo");
        String jdbcUrl = "jdbc:h2:" + server.getURL() + "/main";
        System.out.println("jdbc url: " + jdbcUrl);
        Connection connection = DriverManager.getConnection("jdbc:h2:" + server.getURL() + "/main", "", null);


        // create our service
        TodoService todoService = new TodoService(connection);

        // init database
        todoService.initDatabase();

        Spark.get(
                "/",
                (request, response) -> {
                    HashMap<String, Object> m = new HashMap<>();

                    // list todos
                    ArrayList<Todo> todos = todoService.listTodos();

                    m.put("hasTodos", todos.size() > 0);
                    m.put("todos", todos);

                    return new ModelAndView(m, "todo.mustache");
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-todo",
                (request, response) -> {

                    Todo todo = new Todo(request.queryParams("name"));

                    // insert the todo item
                    todoService.addTodo(todo);

                    response.redirect("/");
                    halt();

                    return null;
                }
        );

        Spark.get(
                "/update-todo",
                (request, response) -> {

                    Todo todo = todoService.getTodo(Integer.valueOf(request.queryParams("id")));
                    todo.done = Boolean.parseBoolean(request.queryParams("done"));

                    // update the todo item
                    todoService.updateTodo(todo);

                    response.redirect("/");
                    halt();

                    return null;
                }
        );

        Spark.get(
                "/delete-todo",
                (request, response) -> {

                    // delete the todo
                    todoService.deleteTodo(Integer.valueOf(request.queryParams("id")));

                    response.redirect("/");
                    halt();

                    return null;
                }
        );

    }

}

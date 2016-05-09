import java.sql.*;
import java.util.ArrayList;

/**
 * Created by doug on 5/9/16.
 */
public class TodoService {

    private final Connection connection;

    public TodoService(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Todo> listTodos() throws SQLException {
        ArrayList<Todo> todos = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM todo");

        while(resultSet.next()){
            Todo todo = new Todo(resultSet.getString("name"));
            todo.id = resultSet.getInt("id");
            todo.done = resultSet.getBoolean("done");

            todos.add(todo);
        }

        return todos;
    }

    public Todo getTodo(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM todo WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        Todo todo = new Todo(resultSet.getString("name"));
        todo.done = resultSet.getBoolean("done");
        todo.id = resultSet.getInt("id");

        return todo;
    }

    public void addTodo(Todo todo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO todo VALUES (NULL, ?, ?)");
        preparedStatement.setString(1, todo.name);
        preparedStatement.setBoolean(2, todo.done);
        preparedStatement.execute();
    }

    public void updateTodo(Todo todo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE todo SET name = ?, done = ? WHERE id = ?");
        preparedStatement.setString(1, todo.name);
        preparedStatement.setBoolean(2, todo.done);
        preparedStatement.setInt(3, todo.id);
        preparedStatement.execute();
    }

    public void deleteTodo(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM todo WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.execute();

    }

    public void initDatabase() throws SQLException {
        // todo create db
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS todo (id IDENTITY, name VARCHAR, done BOOLEAN)");
    }
}

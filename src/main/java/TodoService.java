import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by doug on 5/9/16.
 */
public class TodoService {

    private final Connection connection;

    public TodoService(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Todo> listTodos(){
        // todo do a select
        return null;
    }

    public Todo getTodo(int id){
        // todo select a single item
        return null;
    }

    public void addTodo(Todo todo){
        // todo do an insert
    }

    public void updateTodo(Todo todo){
        // todo do an update
    }

    public void deleteTodo(int id){
        // do a delete
    }
}

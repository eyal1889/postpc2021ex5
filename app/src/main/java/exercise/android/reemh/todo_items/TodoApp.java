package exercise.android.reemh.todo_items;

import android.app.Application;

public class TodoApp extends Application {
    private TodoItemsHolderImpl holder;
    private static TodoApp instance = null;

    public TodoItemsHolder getHolder(){
        return holder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        holder = new TodoItemsHolderImpl(this);
    }
    public static TodoApp getInstance(){
        return instance;
    }
}

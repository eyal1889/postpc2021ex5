package exercise.android.reemh.todo_items

import java.io.Serializable

// TODO: implement!
class TodoItemsHolderImpl : TodoItemsHolder {
    private var todos = ArrayList<TodoItem?>()
    //private var size:Int =0
    override fun getCurrentItems(): ArrayList<TodoItem?> {
        return todos
    }
    override fun addNewInProgressItem(description: String?) {
        val item = TodoItem(description!!, false)
        todos.add(0,item)
       // size+=1
    }
    override fun markItemDone(item: TodoItem?) {
        item!!.setStatus(true)
    }

    override fun markItemInProgress(item: TodoItem?) {
        item!!.setStatus(false)
    }

    override fun deleteItem(item: TodoItem?) {
        //size-=1
        todos.remove(item)
    }

//    override fun getSize():Int {
//        return size
//    }

    override fun saveState(): Serializable? {
        val state = CalculatorState()
        state.todos.addAll(todos)
        //state.size = size
        return state
    }

    override fun loadState(prevState: Serializable?) {
        if (prevState !is CalculatorState) {
            return  // ignore
        }
        todos.clear()
        todos.addAll(prevState.todos)
        //size = prevState.size
    }

    private class CalculatorState : Serializable {
        /*
    all fields must only be from the types:
    - primitives (e.g. int, boolean, etc)
    - String
    - ArrayList<> where the type is a primitive or a String
    - HashMap<> where the types are primitives or a String
     */
        var todos = java.util.ArrayList<TodoItem?>()
        //var size=0
    }
}
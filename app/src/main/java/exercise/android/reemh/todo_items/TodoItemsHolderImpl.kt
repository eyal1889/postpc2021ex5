package exercise.android.reemh.todo_items
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.util.Date;


// TODO: implement!
class TodoItemsHolderImpl: TodoItemsHolder {
    val sp:SharedPreferences;
    private var todos = ArrayList<TodoItem?>()

    constructor(context:Context){
        sp = context.getSharedPreferences("local_db_todo",Context.MODE_PRIVATE)
        initFromSp()
    }
    public fun initFromSp(){
        val storedJson: String? = sp.getString("holder","nope")
        if (storedJson.equals("nope")) return
        val gson = Gson()
        val type = object: TypeToken<ArrayList<TodoItem>>(){}.type
        //arrayType:Type = TypeToken<>
        todos = gson.fromJson(storedJson, type)
    }

    override fun getCurrentItems(): ArrayList<TodoItem?> {
        val copy_arr = ArrayList<TodoItem?>()
        copy_arr.addAll(todos)
        return copy_arr
    }
    override fun addNewInProgressItem(description: String?) {
        val formatter= SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss .SSS");
        val date = Date(System.currentTimeMillis());
        //println(date.time)
        val d:String = formatter.format(date)
        val item = TodoItem(description!!, false,d,d)
        todos.add(0,item);
    }
    override fun markItemDone(item: TodoItem?,position: Int): Int {
        item!!.setStatus(true)
        return reorder_Marked_Done(position)
    }

    override fun markItemInProgress(item: TodoItem?,position: Int):Int {
        item!!.setStatus(false)
        return reorder_Marked_Progress(position)
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

    override fun saveInSp(){
        val gson = Gson()
        val objJson:String = gson.toJson(todos,ArrayList::class.java)
        val editor = sp.edit()
        editor.putString("holder",objJson)
        editor.apply()
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
    override fun reorder_Marked_Done(position: Int):Int {
        var new_pos = position
            for (i in position until todos.size - 1) {
                if (todos.get(i + 1)?.getStatus()!!) {
                    break
                }
                new_pos++
                val temp = todos.get(i)
                todos.set(i, todos.get(i + 1))
                todos.set(i + 1, temp)
        }
        return new_pos
    }
    override fun reorder_Marked_Progress(position: Int):Int {
        if (todos.size == 0){
            return -1
        }
        val temp = todos.get(position)
        todos.removeAt(position)
        todos.add(0,temp)
        return 0
//        var temp= todos[0]
//        todos[0] = todos[position]
//        for (i in 1 until todos.size) {
//            val temp1= todos.get(i)
//            todos.set(i, temp)
//            temp = temp1
//        }
//        return 0
    }
}
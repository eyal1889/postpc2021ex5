package exercise.android.reemh.todo_items

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class TodoItem(private var description: String, private var is_done: Boolean,
               private val creation: String, private var lastModified: String) : Serializable {
    fun setDescription(description: String) {
        this.description = description
    }

    fun getDescription():String{
        return description
    }

    fun getStatus():Boolean{
        return is_done
    }

    fun setStatus(status: Boolean) {
        is_done = status
    }
    fun getCreation():String{
        return creation
    }
    fun getLastModified():String{
        return lastModified
    }
    fun setLastModified(){
        val formatter= SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss .SSS");
        val date = Date(System.currentTimeMillis());
        val d:String = formatter.format(date)
        this.lastModified = d
    }
}
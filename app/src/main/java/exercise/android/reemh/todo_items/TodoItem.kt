package exercise.android.reemh.todo_items

import java.io.Serializable

class TodoItem(private var description: String, private var is_done: Boolean) : Serializable {
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

}
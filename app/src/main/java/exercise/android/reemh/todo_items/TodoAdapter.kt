package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(var itemsHolder: TodoItemsHolder) : RecyclerView.Adapter<TodoHolder>() {
    public var onCheckBoxCallback: ((Int) -> Unit)? = null
    public var onDelCallback: ((Int) -> Unit)? = null
    public var onItemClickCallBack: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.row_todo_item, parent, false)
        val holder = TodoHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return itemsHolder.getCurrentItems()!!.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todoItem = itemsHolder.getCurrentItems()?.get(position)
        if (todoItem != null) {
            holder.description.setText(todoItem.getDescription())
        }
        if (todoItem != null) {
            holder.status.setChecked(todoItem.getStatus())
        }
        if (holder.status.isChecked()){
            holder.description.paintFlags = holder.description.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else if(!holder.status.isChecked()){
            holder.description.paintFlags = holder.description.getPaintFlags() and Paint.LINEAR_TEXT_FLAG
        }
        holder.status.setOnClickListener {
            val callback = onCheckBoxCallback ?: return@setOnClickListener
            if (todoItem != null) {
                callback(holder.adapterPosition)
            }
            if (holder.status.isChecked()){
                holder.description.paintFlags = holder.description.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
            }
            else if(!holder.status.isChecked()){
                holder.description.paintFlags = holder.description.getPaintFlags() and Paint.LINEAR_TEXT_FLAG
            }
        }

        holder.delete.setOnClickListener {
            val callback = onDelCallback ?: return@setOnClickListener
            if (todoItem != null) {
                print("in delete: "+position+"\n")
                callback(holder.adapterPosition)
            }
        }
        holder.itemView.setOnClickListener {
            val callback = onItemClickCallBack ?: return@setOnClickListener
            if (todoItem != null) {
                callback(holder.adapterPosition)
            }
        }

    }
}
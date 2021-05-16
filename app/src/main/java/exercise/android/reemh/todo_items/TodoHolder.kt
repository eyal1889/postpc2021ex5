package exercise.android.reemh.todo_items

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoHolder(view: View): RecyclerView.ViewHolder(view){
    val description: TextView = view.findViewById(R.id.description)
    val status: CheckBox = view.findViewById(R.id.status)
    val delete:ImageButton = view.findViewById(R.id.delete)
}

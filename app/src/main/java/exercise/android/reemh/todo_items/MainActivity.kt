package exercise.android.reemh.todo_items

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var broadcastReceiverForDbChanged: BroadcastReceiver? = null
    private var broadcastReceiverForCheckedEdit: BroadcastReceiver? = null


    @JvmField
    var holder: TodoItemsHolder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (holder == null) {
            holder = TodoApp.getInstance().holder
        }
        val adapter = TodoAdapter(holder!!)
        val todo_recycler: RecyclerView = findViewById(R.id.recyclerTodoItemsList)
        todo_recycler.adapter = adapter
        todo_recycler.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false /*reverseLayout*/)

//        //my code starts here:
        val editTextUserInput = findViewById<EditText>(R.id.editTextInsertTask)
        val buttonAdd = findViewById<FloatingActionButton>(R.id.buttonCreateTodoItem)
        buttonAdd.setOnClickListener { v: View? ->
            val description = editTextUserInput.text.toString()
            if (description == "") {
                return@setOnClickListener
            }
            holder!!.addNewInProgressItem(description)
            adapter.notifyItemInserted(0)
            editTextUserInput.setText("")
        }
        adapter.onCheckBoxCallback = { position: Int ->
            val todoItem = holder!!.getCurrentItems()?.get(position)
            val status = todoItem?.getStatus()
            if (status == true) {
                val new_pos = holder!!.markItemInProgress(todoItem,position)
                //reorder_Marked_Progress(holder!!.getCurrentItems(), adapter, position)
                adapter.notifyItemMoved(position,new_pos)
                println("###"+position+"###"+new_pos)
            } else {
                val new_pos = holder!!.markItemDone(todoItem,position)
                adapter.notifyItemMoved(position,new_pos)
                //reorder_Marked_Done(holder!!.getCurrentItems(), adapter, position)
                println("###"+position+"###"+new_pos)
            }
        }
        adapter.onDelCallback = {position: Int ->
            val todoItem = holder!!.getCurrentItems()?.get(position)
            holder!!.deleteItem(todoItem)
            adapter.notifyItemRemoved(position)
            println(position)
            println(holder!!.getCurrentItems()?.size)
            println("itemToDel: "+ todoItem?.getDescription())
            println("$$$"+adapter.itemCount+"$$$")
        }
        adapter.onItemClickCallBack = {position: Int ->
            val todoItem = holder!!.getCurrentItems()?.get(position)
            val intentToOpenService = Intent(this@MainActivity, EditTodo::class.java)
            intentToOpenService.putExtra("description",todoItem?.getDescription())
            intentToOpenService.putExtra("creation_date",todoItem?.getCreation())
            intentToOpenService.putExtra("last_modified",todoItem?.getLastModified())
            intentToOpenService.putExtra("status",todoItem?.getStatus())
            intentToOpenService.putExtra("pos",position)
            startActivity(intentToOpenService)
        }
        /*
    was a todo
     */broadcastReceiverForDbChanged = object : BroadcastReceiver() {
            override fun onReceive(context: Context, incomingIntent: Intent) {
                if (incomingIntent.action != "db_changed") return
                val new_desc = incomingIntent.getStringExtra("new_desc")
                val pos = incomingIntent.getIntExtra("pos",0)
                if (new_desc != null) {
                    holder!!.getCurrentItems()?.get(pos)?.setDescription(new_desc)
                    holder!!.getCurrentItems()?.get(pos)?.setLastModified()
                    adapter.notifyItemChanged(pos)
                }
            }
        }
        broadcastReceiverForCheckedEdit = object : BroadcastReceiver() {
            override fun onReceive(context: Context, incomingIntent: Intent) {
                if (incomingIntent.action != "checkbox_in_edit") return
                val status = incomingIntent.getBooleanExtra("check_status",false)
                val pos = incomingIntent.getIntExtra("pos",0)
                    holder!!.getCurrentItems()?.get(pos)?.setLastModified()
                    adapter.notifyItemChanged(pos)
                val todoItem = holder!!.getCurrentItems()?.get(pos)
                if (status != true) {
                    val new_pos= holder!!.markItemInProgress(todoItem,pos)
                    //reorder_Marked_Progress(holder!!.getCurrentItems(), adapter, new_pos)
                    adapter.notifyItemMoved(pos,new_pos)
                    val bc =Intent("status_changed")
                    bc.putExtra("new_pos",new_pos)
                    sendBroadcast(bc)
                } else {
                    val new_pos = holder!!.markItemDone(todoItem,pos)
                    //reorder_Marked_Done(holder!!.getCurrentItems(), adapter, pos)
                    adapter.notifyItemMoved(pos,new_pos)
                    val bc =Intent("status_changed")
                    bc.putExtra("new_pos",new_pos)
                    sendBroadcast(bc)
                }

                }
            }

        registerReceiver(broadcastReceiverForDbChanged, IntentFilter("db_changed"))
        registerReceiver(broadcastReceiverForCheckedEdit, IntentFilter("checkbox_in_edit"))
    }

    override fun onDestroy() {
        super.onDestroy()
        holder!!.saveInSp()
        unregisterReceiver(broadcastReceiverForDbChanged)

    }

//    fun reorder_Marked_Done(lst: ArrayList<TodoItem?>?, adapter: TodoAdapter, position: Int) {
//        var new_pos = position
//        if (lst != null) {
//            for (i in position until lst.size - 1) {
//                if (lst.get(i + 1)?.getStatus()!!) {
//                    break
//                }
//                new_pos++
//                val temp = lst.get(i)
//                lst.set(i, lst.get(i + 1))
//                lst.set(i + 1, temp)
//            }
//        }
//        adapter.notifyItemMoved(position, new_pos)
//    }
//    fun reorder_Marked_Progress(lst: ArrayList<TodoItem?>?, adapter: TodoAdapter, position: Int) {
//        if (lst!!.size == 0){
//            return
//        }
//        var temp= lst[0]
//        lst[0] = lst[position]
//        for (i in 1 until lst.size) {
//            val temp1= lst.get(i)
//            lst.set(i, temp)
//            temp = temp1
//        }
//        adapter.notifyItemMoved(position, 0)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("state", holder!!.saveState())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //calculator.loadState(savedInstanceState);
        val prevState = savedInstanceState.getSerializable("state")
        holder!!.loadState(prevState)
    }
}


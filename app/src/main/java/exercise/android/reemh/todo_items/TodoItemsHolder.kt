package exercise.android.reemh.todo_items

import java.io.Serializable

// TODO: feel free to add/change/remove methods as you want
interface TodoItemsHolder {
    /** Get a copy of the current items list  */
    fun getCurrentItems(): ArrayList<TodoItem?>?

    /**
     * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
     * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
     */
    fun addNewInProgressItem(description: String?)

    /** mark the @param item as DONE  */
    fun markItemDone(item: TodoItem?)

    /** mark the @param item as IN-PROGRESS  */
    fun markItemInProgress(item: TodoItem?)

    /** delete the @param item  */
    fun deleteItem(item: TodoItem?)

    /**
     * @return some object that encapsulates the current state
     */
    fun saveState(): Serializable?

    /**
     * Read the input state and load from it.
     * Example:
     * given the input "14+5-7",
     * Calling saveState() will return some serializable object that stores all needed data for the state.
     * If we will create a new Calculator instance and call loadState(), passing the serializable from earlier,
     * and then call output() →
     * the result should be "14+5-7" (i.e. successfully loaded the serializable state).
     *
     * On any error just reset the state back to zero.
     *
     * @param prevState the state to load
     */
    fun loadState(prevState: Serializable?)
}
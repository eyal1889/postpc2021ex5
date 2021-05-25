package exercise.android.reemh.todo_items;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.Serializable;

public class TodoItemsHolderImplTest extends TestCase {
  EditTodo a = new EditTodo();
  @Test
  public void test_when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);
    assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    assertEquals(1, holderUnderTest.getCurrentItems().size());
  }


  // TODO: add at least 10 more tests to verify correct behavior of your implementation of `TodoItemsHolderImpl` class

  @Test
  public void test_when_addingTodoItem_and_removing_then_callingListShouldNotHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    TodoItem item = holderUnderTest.getCurrentItems().get(0);
    holderUnderTest.deleteItem(item);
    // verify
    assertEquals(0, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void test_when_addingTodoItem_then_ThisItemInProgress(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    TodoItem item = holderUnderTest.getCurrentItems().get(0);
    // verify
    assertFalse(item.getStatus());
  }

  @Test
  public void test_when_markingAsDone_then_ThisItemIsDone(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    TodoItem item = holderUnderTest.getCurrentItems().get(0);
    holderUnderTest.markItemDone(item,0);
    // verify
    assertTrue(item.getStatus());
  }

  @Test
  public void test_when_adding2TodoItems_then_newIsFirst(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("item1");
    TodoItem item1 = holderUnderTest.getCurrentItems().get(0);
    holderUnderTest.addNewInProgressItem("item2");
    // verify
    assertEquals(holderUnderTest.getCurrentItems().get(0).getDescription(),"item2");
  }

  @Test
  public void test_when_savingState_then_stateNotNull(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("1");
    // verify
    Serializable saved= holderUnderTest.saveState();
    assertNotNull(saved);
  }
  @Test
  public void test_when_savingState_then_LoadingIsGood(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("1");
    // verify
    Serializable saved= holderUnderTest.saveState();
    assertNotNull(saved);
    holderUnderTest.loadState(saved);
    assertEquals(holderUnderTest.getCurrentItems().get(0).getDescription(),"1");
  }

  @Test
  public void test_when_2HoldersAndSavingState_then_LoadingIsGood(){
    // setup
    TodoItemsHolderImpl holderUnderTest1 = new TodoItemsHolderImpl(a);
    TodoItemsHolderImpl holderUnderTest2 = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest1.addNewInProgressItem("1");
    // verify
    Serializable saved= holderUnderTest1.saveState();
    assertNotNull(saved);
    holderUnderTest2.loadState(saved);
    assertEquals(holderUnderTest2.getCurrentItems().get(0).getDescription(),"1");
  }

  @Test
  public void test_when_holderWithEmptyListIntoHolderWithNonEmptyList_then_ListIsCleared(){
    // setup
    TodoItemsHolderImpl holderUnderTest1 = new TodoItemsHolderImpl(a);
    TodoItemsHolderImpl holderUnderTest2 = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest2.addNewInProgressItem("1");
    // verify
    Serializable saved= holderUnderTest1.saveState();
    assertNotNull(saved);
    holderUnderTest2.loadState(saved);
    assertEquals(holderUnderTest2.getCurrentItems().size(),0);
  }

  @Test
  public void test_when_creatingHolder_then_ListIsEmpty(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    assertEquals(holderUnderTest.getCurrentItems().size(),0);
  }

  @Test
  public void test_when_MarkAsDone_AndThenMarkInProgress_then_ItemIsInProgress(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl(a);

    // test
    holderUnderTest.addNewInProgressItem("do shopping");
    TodoItem item = holderUnderTest.getCurrentItems().get(0);
    // verify
    holderUnderTest.markItemDone(item,0);
    holderUnderTest.markItemInProgress(item,0);
    assertFalse(item.getStatus());
  }
}
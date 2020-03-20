package com.example.shoppinglist.models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {

    int Id;
    String ShoppingListContent;
    boolean IsDone;
    String CreateDate;
    String DoneDate;
    int NoOfItemsInList;

    public ShoppingList() {
        ShoppingListContent = "Default Content";
        IsDone = false;
        CreateDate = "01.01.2020";
        DoneDate = "";
        NoOfItemsInList = 0;
    }

    public ShoppingList(int id, String shoppingListContent, boolean isDone, String createDate, String doneDate, int noOfItemsInList) {
        Id = id;
        ShoppingListContent = shoppingListContent;
        IsDone = isDone;
        CreateDate = createDate;
        DoneDate = doneDate;
        NoOfItemsInList = noOfItemsInList;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getShoppingListContent() {
        return ShoppingListContent;
    }

    public void setShoppingListContent(String shoppingListContent) {
        ShoppingListContent = shoppingListContent;
    }

    public boolean isDone() {
        return IsDone;
    }

    public void setDone(boolean done) {
        IsDone = done;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getDoneDate() {
        return DoneDate;
    }

    public void setDoneDate(String doneDate) {
        DoneDate = doneDate;
    }

    public int getNoOfItemsInList() {
        return NoOfItemsInList;
    }

    public void setNoOfItemsInList(int noOfItemsInList) {
        NoOfItemsInList = noOfItemsInList;
    }
}

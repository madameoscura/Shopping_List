package com.example.shoppinglist.models;

public class Items {
    int Id;
    String ItemsListContent;
    boolean IsDone;
    String CreateDate;
    String DoneDate;
    int ShoppingListID;

    public Items() {
        ItemsListContent = "Default Content";
        IsDone = false;
        CreateDate = "01.01.2020";
        DoneDate = "";
        ShoppingListID = 0;
    }


    public Items(int id, String shoppingListContent, boolean isDone, String createDate, String doneDate, int shoppingListID) {
        Id = id;
        ItemsListContent = shoppingListContent;
        IsDone = isDone;
        CreateDate = createDate;
        DoneDate = doneDate;
        ShoppingListID = shoppingListID;

    }

    public int getShoppingListID() {
        return ShoppingListID;
    }

    public void setShoppingListID(int shoppingListID) {
        ShoppingListID = shoppingListID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getItemsListContent() {
        return ItemsListContent;
    }

    public void setItemsListContent(String itemsListContent) {
        ItemsListContent = itemsListContent;
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
}

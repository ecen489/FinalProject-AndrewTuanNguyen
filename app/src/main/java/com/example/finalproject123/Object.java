package com.example.finalproject123;

public class Object {
    int Quantity;
    String id;
    String name;
    public  Object()
    {

    }

    public Object(String id, String name, int Quantity)
    {
        this.Quantity= Quantity;
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
    public int getQuantity()
    {
        return this.Quantity;
    }
    public void setQuantity(int quantity)
    {
        this.Quantity= quantity;
    }
    public String getId()
    {
        return this.id;
    }
}

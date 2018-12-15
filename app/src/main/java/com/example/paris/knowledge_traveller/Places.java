package com.example.paris.knowledge_traveller;

public class Places {

    private String Name ;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Post{" +
                "Name=" + Name +
                '}';
    }


}
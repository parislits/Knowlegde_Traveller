package com.example.paris.knowledge_traveller;

public class Places {

    private String Name ;
    private String Wiki;

    public String getWiki() {
        return Wiki;
    }

    public void setWiki(String wiki) {
        Wiki = wiki;
    }

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
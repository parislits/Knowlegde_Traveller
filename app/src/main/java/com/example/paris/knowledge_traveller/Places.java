package com.example.paris.knowledge_traveller;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "Name", unique = true)
})
public class Places {
    @org.greenrobot.greendao.annotation.Id(autoincrement = true)
    private Long Id;

    @NotNull
    private String Name ;
    private String Wiki;

    @Generated(hash = 1885521771)
    public Places(Long Id, @NotNull String Name, String Wiki) {
        this.Id = Id;
        this.Name = Name;
        this.Wiki = Wiki;
    }

    @Generated(hash = 1930620307)
    public Places() {
    }

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

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }


}
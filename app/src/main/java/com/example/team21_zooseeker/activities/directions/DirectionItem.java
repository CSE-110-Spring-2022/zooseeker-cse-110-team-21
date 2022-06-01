package com.example.team21_zooseeker.activities.directions;

public class DirectionItem {
    private String name;
    private String dirDesc;
    private String id;

    public DirectionItem(String name, String dirDesc, String id) {
        this.name = name;
        this.dirDesc = dirDesc;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDirDesc() {
        return dirDesc;
    }

    public String getId() {
        return id;
    }
}

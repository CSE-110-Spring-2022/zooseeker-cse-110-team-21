package com.example.team21_zooseeker.activities.directions;

public class DirectionItem {
    private String name;
    private String dirDesc;

    public DirectionItem(String name, String dirDesc) {
        this.name = name;
        this.dirDesc = dirDesc;
    }

    public String getName() {
        return name;
    }

    public String getDirDesc() {
        return dirDesc;
    }
}

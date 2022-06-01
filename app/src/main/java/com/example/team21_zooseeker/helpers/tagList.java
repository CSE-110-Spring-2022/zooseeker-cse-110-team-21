package com.example.team21_zooseeker.helpers;

import java.util.List;

public class tagList {
    private List<String> tags;

    public tagList(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

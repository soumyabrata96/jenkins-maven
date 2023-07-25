package com.multithreading.demo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TopStories {
    private List<String> titles;
    private String section;

    public TopStories(){
        titles=new ArrayList<>();
    }

    public List<String> getTitle() {
        return titles;
    }

    public void setTitle(String title) {
        titles.add(title);
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopStories that = (TopStories) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }
}

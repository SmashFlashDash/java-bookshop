package com.example.MyBookShopApp.data.tag;

public class TagDto {

    private String tag;

    public TagDto(String name) {
        this.tag = name;
    }

    public TagDto(){}

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

package com.example.MyBookShopApp.data.genre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenreDto implements Comparable<GenreDto> {
    private final List<GenreDto> childs = new ArrayList<>();
    private GenreEntity item;
    private GenreDto parent;
    private String slug = "";
    private Integer countBooks = null;
    private Integer maxDepth = null;

    public GenreDto() {
        this.item = null;
    }

    public GenreDto(GenreEntity item) {
        this.item = item;
        slug = item.getSlug();
    }

    public void addChild(GenreDto item) {
        item.setSlug(slug.concat((slug.isEmpty()) ? "" : "/").concat(item.getSlug()));
        item.setParent(this);
        childs.add(item);
    }

    public List<GenreDto> getChilds() {
        return childs;
    }

    public GenreEntity getItem() {
        return item;
    }

    public void setItem(GenreEntity item) {
        this.item = item;
    }

    public int getCountBooks() {
        return countBooks;
    }

    public void setCountBooks(Integer countBooks) {
        this.countBooks = countBooks;
    }

    public GenreDto getParent() {
        return parent;
    }

    public void setParent(GenreDto parent) {
        this.parent = parent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public int compareTo(GenreDto o) {
        if (this.getMaxDepth() == null || o.getMaxDepth() == null) {
            return -1;
        }
        return Comparator.comparing(GenreDto::getSlug).compare(this, o);
//        return Comparator.comparing(GenreDto::getMaxDepth).reversed()
//                .thenComparing(GenreDto::getCountBooks)
//                .compare(this, o);
    }
}

package com.example.MyBookShopApp.data.genre;

import java.util.*;
import java.util.stream.Collectors;

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
        item.setSlug(slug.concat("/").concat(item.getSlug()));
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

    public int calculateCountBooks() {
//        if (childs.size()==0){
//            countBooks = item.getBooks().size();
//        } else {
//            countBooks = childs.stream().mapToInt(GenreDto::calculateCountBooks).sum();
//        }
//        return countBooks;
        countBooks = (childs.size() == 0) ? item.getBooks().size() : childs.stream().mapToInt(GenreDto::calculateCountBooks).sum();
        return countBooks;
    }

    public Integer calculateDepth() {
//        if (childs.size() == 0) {
//            maxDepth = 0;
//        } else {
//            int tmpMaxDepth;
//            for (GenreDto ch : childs) {
//                tmpMaxDepth = ch.calculateDepth() + 1;
//                if (maxDepth == null || tmpMaxDepth > maxDepth) {
//                    maxDepth = tmpMaxDepth;
//                }
//            }
//            // maxDepth += childs.stream().mapToInt(GenreDto::calculateDepth).max().getAsInt() + 1;
//        }
//        return maxDepth;
        maxDepth =  (childs.size() == 0) ? 0 : childs.stream().mapToInt(GenreDto::calculateDepth).max().getAsInt() + 1;
        return maxDepth;
    }

    public int getCountBooks() {
        return countBooks;
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

    public void treeOfMap(List<GenreEntity> genres) {
       Map<Integer, GenreDto> genresMap =  genres.stream().collect(Collectors.toMap(GenreEntity::getId, GenreDto::new));
        for (Map.Entry<Integer, GenreDto> item : genresMap.entrySet()){
            GenreDto dto = item.getValue();
            Integer parId = dto.getItem().getParentId();
            if (parId == null){
                addChild(dto);
            } else {
                genresMap.get(parId).addChild(dto);
            }
        }
        calculateDepth();
        calculateCountBooks();
    }

    // TODO: не сортирует при update значений
    @Override
    public int compareTo(GenreDto o) {
        if (this.getMaxDepth() == null || o.getMaxDepth() == null) {
            return -1;
        }
        return Comparator.comparing(f -> o.getItem().getName()).compare(this, o);
    }


}

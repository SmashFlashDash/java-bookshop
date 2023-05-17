package com.example.MyBookShopApp.data.genre;

import com.example.MyBookShopApp.dto.GenreDto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapperGenreEntityToGenreDro {
//    public int calculateCountBooks() {
//        countBooks = (childs.size() == 0) ? item.getBooks().size() : childs.stream().mapToInt(GenreDto::calculateCountBooks).sum();
//        return countBooks;
//    }
//
//    public Integer calculateDepth() {
//        maxDepth =  (childs.size() == 0) ? 0 : childs.stream().mapToInt(GenreDto::calculateDepth).max().getAsInt() + 1;
//        return maxDepth;
//    }

    private static int[] calculateAndSortTree(GenreDto genre) {
        GenreEntity item = genre.getItem();
        List<GenreDto> childs = genre.getChilds();
        if (childs.size() == 0) {
            genre.setMaxDepth(0);
            genre.setCountBooks(item.getBooks().size());
            Collections.sort(childs);
        } else {
            int tmpMaxDepth;
            int tmpCountBooks = (item == null) ? 0 : item.getBooks().size();
            for (GenreDto ch : childs) {
                int[] w = calculateAndSortTree(ch);
                tmpMaxDepth = w[0] + 1;
                if (genre.getMaxDepth() == null || tmpMaxDepth > genre.getMaxDepth()) {
                    genre.setMaxDepth(tmpMaxDepth);
                }
                tmpCountBooks += ch.getCountBooks();
            }
            Collections.sort(childs);
            genre.setCountBooks(tmpCountBooks);
        }
        return new int[]{genre.getMaxDepth(), genre.getCountBooks()};
    }

    public static void treeOfMap(List<GenreEntity> genres, GenreDto root) {
        Map<Integer, GenreDto> tmpMap = genres.stream().collect(Collectors.toMap(GenreEntity::getId, GenreDto::new));
        for (Map.Entry<Integer, GenreDto> item : tmpMap.entrySet()) {
            GenreDto dto = item.getValue();
            Integer parId = dto.getItem().getParentId();
            if (parId == null) {
                root.addChild(dto);
            } else {
                tmpMap.get(parId).addChild(dto);
            }
        }
        // calculateCountBooks();
        // calculateDepth();
        // sortByNames();
        calculateAndSortTree(root);
        root.getChilds().sort(Comparator.comparing(GenreDto::getMaxDepth).reversed());
    }
}

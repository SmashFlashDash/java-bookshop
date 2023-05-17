package com.example.MyBookShopApp.dto;

import java.util.*;

public class BookRatingDto {
    private final HashMap<Short, Integer> fiveRating = new HashMap<>();

    public BookRatingDto(List<BookRatingItem> values) {
        fiveRating.put((short) 1, 0);
        fiveRating.put((short) 2, 0);
        fiveRating.put((short) 3, 0);
        fiveRating.put((short) 4, 0);
        fiveRating.put((short) 5, 0);
        for(BookRatingItem rating : values) {
            fiveRating.put(rating.getValue(), rating.getCount());
        }
    }

    public int getCount() {
        return fiveRating.values().stream().reduce(0, Integer::sum);
    }

    public int getOneStar() {
        return fiveRating.get((short) 1);
    }

    public int getTwoStar() {
        return fiveRating.get((short) 2);
    }

    public int getThreeStar() {
        return fiveRating.get((short) 3);
    }

    public int getFourStar() {
        return fiveRating.get((short) 4);
    }

    public int getFiveStar() {
        return fiveRating.get((short) 5);
    }
}

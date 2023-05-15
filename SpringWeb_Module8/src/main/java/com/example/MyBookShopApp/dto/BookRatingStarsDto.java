package com.example.MyBookShopApp.dto;

import java.util.HashMap;
import java.util.List;

public class BookRatingStarsDto {
    private final HashMap<Short, Integer> fiveRating = new HashMap<Short, Integer>() {{
        this.put((short) 1, 0);
        this.put((short) 2, 0);
        this.put((short) 3, 0);
        this.put((short) 4, 0);
        this.put((short) 5, 0);
    }};

    public BookRatingStarsDto(List<BookRatingItem> values) {
        for (BookRatingItem rating : values) {
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

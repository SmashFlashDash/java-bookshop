package org.example.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Digits;

public class BookIdToRemove {

    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

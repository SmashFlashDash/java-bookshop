package com.example.MyBookShopApp.data.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagEntity findByTagName(String name){
        return tagRepository.findByTag(name);
    }

    public List<TagEntity> findAllSortedByBooksCount(){
        return tagRepository.findAllTagsSortedByBookCount();
    }
}

package com.devdojo.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class PageableResponse<T> extends PageImpl<T> {

    private boolean left;
    private boolean last;
    private boolean first;
    private int totalPages;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageableResponse(@JsonProperty("content") List<T> content,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("numberOfElements") int numberOfElements,
                            @JsonProperty("size") int size,
                            @JsonProperty("number") int number,
                            @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public PageableResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageableResponse(List<T> content) {
        super(content);
    }

    public PageableResponse() {
        super(new ArrayList<T>());
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    @Override
    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
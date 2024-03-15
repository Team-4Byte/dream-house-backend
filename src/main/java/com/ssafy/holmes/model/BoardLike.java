package com.ssafy.holmes.model;

import lombok.Data;

@Data
public class BoardLike {
    public BoardLike(int articleNo, String userId) {
        this.articleNo = articleNo;
        this.userId = userId;
    }

    private int articleNo;
    private String userId;
}

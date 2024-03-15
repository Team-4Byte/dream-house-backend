package com.ssafy.holmes.model.service;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.holmes.model.BoardDto;
import com.ssafy.holmes.model.BoardLike;
import com.ssafy.holmes.model.BoardListDto;

public interface BoardService {

	BoardListDto getBySearchArticle(Map<String, String> map) throws Exception;

	void writeArticle(BoardDto boardDto) throws Exception;

	BoardListDto listArticle(Map<String, String> map) throws Exception;

	BoardDto getArticle(int articleNo) throws Exception;

	void updateHit(int articleNo) throws Exception;

	void modifyArticle(BoardDto boardDto) throws Exception;

	void deleteArticle(int articleNo) throws Exception;

	int getArticleLikeCount(int articleNo) throws Exception;

	void pressArticleLike(int articleNo) throws Exception;

	int isPressArticleLike(BoardLike boardLike) throws SQLException;
}
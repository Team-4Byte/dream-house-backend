package com.ssafy.holmes.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.holmes.model.BoardDto;
import com.ssafy.holmes.model.BoardLike;

@Mapper
public interface BoardMapper {

	List<BoardDto> getBySearchArticle(Map<String, Object> param) throws SQLException;

	int getTotalArticleCount(Map<String, Object> param) throws SQLException;

	void writeArticle(BoardDto boardDto) throws SQLException;

	void registerFile(BoardDto boardDto) throws Exception;

	List<BoardDto> listArticle(Map<String, Object> param) throws SQLException;

	BoardDto getArticle(int articleNo) throws SQLException;

	void updateHit(int articleNo) throws SQLException;

	void modifyArticle(BoardDto boardDto) throws SQLException;

	void deleteArticle(int articleNo) throws SQLException;

	int getArticleLikeCount(int articleNo) throws SQLException;

	void pressArticleLike(int articleNo) throws SQLException;

	int isPressArticleLike(BoardLike boardLike) throws SQLException;
}

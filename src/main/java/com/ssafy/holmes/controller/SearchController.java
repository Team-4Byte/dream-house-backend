package com.ssafy.holmes.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/search")
@Api(tags = "크롤링", description = "Version 1.0")
@Slf4j
public class SearchController {
    private static final String API_KEY = "AIzaSyCw_y6CZ0B0STTRYjpJYvIdV8DWWMaYTQI";

    @ApiOperation(value = "부동산 유튜브 링크", notes = "부동산 유튜브 링크 5개 가져오기", response = Map.class)
    @GetMapping("/")
    public ResponseEntity<List<SearchResult>> getYoutubeLink() throws IOException {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        })
                .setApplicationName("youtube-api-search-example")
                .build();

        // YouTube Data API의 "search" 엔드포인트 사용
        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setKey(API_KEY);
        search.setQ("부동산"); // 검색할 키워드 설정
        search.setType("video"); // 동영상 타입으로 설정
        search.setMaxResults((long) 4); // 최대 결과 수 설정

        // API 호출 및 결과 반환
        SearchListResponse searchListResponse = search.execute();
        List<SearchResult> list = searchListResponse.getItems();

        for (int i = 0; i < list.size(); i++) {
            // SearchListResponse에서 videoId 추출
            String videoId = list.get(i).getId().getVideoId();

            // videoId를 사용하여 동영상의 상세 정보 가져오기
            YouTube.Videos.List videosList = youtube.videos().list("statistics");
            videosList.setKey(API_KEY);
            videosList.setId(videoId);

            // API 호출 및 결과 반환
            VideoListResponse videoListResponse = videosList.execute();
            Video video = videoListResponse.getItems().get(0);

            // 동영상의 조회수 가져오기
            BigInteger viewCount = video.getStatistics().getViewCount();
            list.get(i).set("viewCount", viewCount);
        }

        return ResponseEntity.ok(list);
    }
}
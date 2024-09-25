package com.matzip.matzipback.board.query.mapper;

import com.matzip.matzipback.board.query.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostDto> searchPosts(int offset, Integer size, String postTitle, String userNickname);

    long countPostsBySearch(String postTitle, String userNickname);

    List<PostDto> getPostsByCategory(int offset, Integer size, Long boardCategorySeq);

    long countPostsByCategory(Long boardCategorySeq);
}

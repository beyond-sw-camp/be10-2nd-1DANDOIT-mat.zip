package com.matzip.matzipback.matzipList.query.mapper;

import com.matzip.matzipback.matzipList.query.dto.ListCommentSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ListCommentQueryMapper {

    List<ListCommentSearchDTO> getListComments(Long listSeq);
}

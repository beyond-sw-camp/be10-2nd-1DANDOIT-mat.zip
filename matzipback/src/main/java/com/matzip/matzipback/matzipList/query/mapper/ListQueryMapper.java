package com.matzip.matzipback.matzipList.query.mapper;

import com.matzip.matzipback.matzipList.query.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ListQueryMapper {



    List<ListSearchUserDTO> getListBox(int offset, Integer size, Long listUserSeq);

    List<ListContentDTO> getListContests(Long listSeq);

    List<ListSearchUserDTO> getListsByUserListBox(int offset, Integer size, Long listUserSeq);

    long countListsByListUserSeq(Long listUserSeq);

    Long countListsByList();

    List<ListSearchDTO> getListAll(int offset, Integer size);
}

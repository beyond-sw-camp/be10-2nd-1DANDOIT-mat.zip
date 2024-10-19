package com.matzip.matzipback.matzipList.query.mapper;

import com.matzip.matzipback.matzipList.query.dto.ListBoxListDTO;
import com.matzip.matzipback.matzipList.query.dto.ListContentDTO;
import com.matzip.matzipback.matzipList.query.dto.ListSearchAllDTO;
import com.matzip.matzipback.matzipList.query.dto.ListSearchUserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ListQueryMapper {



    List<ListSearchAllDTO> getListBox(long listUserSeq);

    List<ListSearchUserDTO> getUserListBox(Long listUserSeq);

    List<ListContentDTO> getListContests(Long listSeq);

    List<ListSearchUserDTO> getListsByUserListBox(int offset, Integer size, Long listUserSeq);

    long countListsByListUserSeq(Long listUserSeq);
}

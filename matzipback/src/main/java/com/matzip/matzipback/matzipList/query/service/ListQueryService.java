package com.matzip.matzipback.matzipList.query.service;


import com.matzip.matzipback.matzipList.query.dto.*;
import com.matzip.matzipback.matzipList.query.mapper.ListQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListQueryService {

    private final ListQueryMapper listQueryMapper;

    public List<ListSearchAllDTO> getListBox(Long listUserSeq) {
        return listQueryMapper.getListBox(listUserSeq);
    }

//    public List<ListSearchUserDTO> getUserListBox(Long listUserSeq) {
//        return listQueryMapper.getUserListBox(listUserSeq);
//    }


    public List<ListContentDTO> getListContests(Long listSeq) {
        return listQueryMapper.getListContests(listSeq);
    }
    @Transactional(readOnly = true)
    public ListBoxResponse getUserListBox(Integer page, Integer size, Long listUserSeq) {

        // offset : 몇번째 페이지를 펼지 설정
        int offset = (page - 1) * size;

        // 해당 리스트에 속해있는 리스트 조회
        List<ListSearchUserDTO> ListBox = listQueryMapper.getListsByUserListBox(offset, size, listUserSeq);

        // 해당 게시판 카테고리에 작성된 리스트 개수
        long totalLists = listQueryMapper.countListsByListUserSeq(listUserSeq);

        return ListBoxResponse.builder()
                .listSearchUserDTOs(ListBox)
                .currentPage(page)
                .totalPages((int) Math.ceil((double)totalLists / size))
                .totalLists(totalLists)
                .build();
    }
}

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

    // 리스트 전체 조회
    @Transactional
    public ListAllResponse getAllList(Integer page, Integer size){

        // offset: 몇번째 페이지를 펼지 설정
        int offset = (page - 1) * size;

        List<ListSearchDTO> listAll = listQueryMapper.getListAll(offset, size);

        Long totalLists = listQueryMapper.countListsByList();

        return ListAllResponse.builder()
                .listSearchAllDTOs(listAll)
                .currentPage(page)
                .totalPages((int) Math.ceil((double)totalLists / size))
                .totalLists(totalLists)
                .build();
    }

    // 유저 본인의 리스트 박스 조회
    @Transactional
    public ListBoxResponse getListBox(Integer page, Integer size, Long listUserSeq) {

        // offset: 몇번째 페이지를 펼지 설정
        int offset = (page - 1) * size;

        List<ListSearchUserDTO> ListBox = listQueryMapper.getListBox(offset, size, listUserSeq);

        Long totalLists = listQueryMapper.countListsByListUserSeq(listUserSeq);

        return ListBoxResponse.builder()

                .listSearchUserDTOs(ListBox)
                .currentPage(page)
                .totalPages((int) Math.ceil((double)totalLists / size))
                .totalLists(totalLists)
                .build();
    }

    // 리스트 상세 조회
//    @Transactional
    public List<ListContentDTO> getListContests(Long listSeq) {
        return listQueryMapper.getListContests(listSeq);
    }

    // 다른 유저의 리스트
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

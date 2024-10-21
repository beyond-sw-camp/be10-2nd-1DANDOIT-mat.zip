package com.matzip.matzipback.matzipList.command.domain.service;

import com.matzip.matzipback.matzipList.command.application.dto.UpdateListRequest;
import com.matzip.matzipback.matzipList.command.domain.aggregate.MyList;
import com.matzip.matzipback.matzipList.command.domain.repository.ListDomainRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainListUpdateService {

    private final ModelMapper modelMapper;
    private final ListDomainRepository listDomainRepository;


    public long updateList(Long listSeq, UpdateListRequest updateListRequest, long listUserSeq) {

        MyList existList = listDomainRepository.findById(listSeq)
                .orElseThrow();

        modelMapper.map(updateListRequest, existList);
        existList.updateListTitle(updateListRequest.getListTitle());
        existList.updateListContent(updateListRequest.getListContent());


        listDomainRepository.save(existList);

        return existList.getListSeq();

    }
}

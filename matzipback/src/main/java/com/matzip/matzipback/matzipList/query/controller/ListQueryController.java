package com.matzip.matzipback.matzipList.query.controller;

import com.matzip.matzipback.common.util.CustomUserUtils;

import com.matzip.matzipback.exception.ErrorCode;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.matzipList.query.dto.*;

import com.matzip.matzipback.matzipList.query.service.ListQueryService;
import com.matzip.matzipback.responsemessage.SuccessCode;
import com.matzip.matzipback.responsemessage.SuccessSearchResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "List", description = "리스트")
public class ListQueryController {

    private final ListQueryService listQueryService;

    // 리스트 전체 조회
    @GetMapping("/listbox/all")
    @Operation(summary = "모든 리스트 조회", description = "모든 리스트를 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> listBoxAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        ListAllResponse response = listQueryService.getAllList(page, size);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response
                ));
    }

    // 인기 리스트 조회
    @GetMapping("/listBox/popularList")
    @Operation(summary = "인기 리스트 조회", description = "인기리스트를 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getPopularList(){

        List<ListSearchDTO> response = listQueryService.getPopularList();

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response
                ));
    }

    // 유저 본인의 리스트 서랍 조회(모든 리스트 상태 조회)
    @GetMapping("/listbox/listUserSeq")
    @Operation(summary = "본인 리스트 서랍 조회", description = "본인의 리스트 서랍을 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getListBox(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Long listUserSeq = CustomUserUtils.getCurrentUserSeq();

        if (listUserSeq != null){
            //URL에서 유저 시퀀스 받지 않고 로그인한 사용자의 유저 시퀀스를 받아오는 것으로 구현
            ListBoxResponse response = listQueryService.getListBox(page, size, listUserSeq);

            return ResponseEntity.ok()
                    .body(new SuccessSearchResMessage<>(
                            SuccessCode.BASIC_GET_SUCCESS
                            , response
                    ));
        }else{
            throw new RestApiException(ErrorCode.UNAUTHORIZED_REQUEST);}
    }

    // 다른 유저의 리스트 서랍 조회
    @GetMapping("/listbox/{listUserSeq}")
    @Operation(summary = "다른 유저 리스트 서랍 조회", description = "다른 유저의 리스트 서랍을 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getUserListBox(
            @RequestParam Long listUserSeq,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size

    ) {
        ListBoxResponse response = listQueryService.getUserListBox(page, size, listUserSeq);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response));
    }

    // 리스트 상세 조회
    @GetMapping("/listbox/listUserSeq/{listSeq}")
    @Operation(summary = "리스트 상세 조회", description = "공개된 리스트를 상세 조회한다.")
    public ResponseEntity<SuccessSearchResMessage<?>> getListContents(@PathVariable("listSeq") Long listSeq) {
//        return ResponseEntity.ok().body(listQueryService.getListContests(listSeq));
        List<ListContentDTO> response = listQueryService.getListContests(listSeq);

        return ResponseEntity.ok()
                .body(new SuccessSearchResMessage<>(
                        SuccessCode.BASIC_GET_SUCCESS
                        , response
                ));
    }

}

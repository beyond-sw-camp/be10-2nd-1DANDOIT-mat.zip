package com.matzip.matzipback.matzipList.command.application.controller;


import com.matzip.matzipback.matzipList.command.application.dto.CreateMatzipRequest;
import com.matzip.matzipback.matzipList.command.application.service.MatzipCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MatzipCommandController {

    private final MatzipCommandService matzipCommandService;

    // 맛집 등록
    @PostMapping("/list/matzip/")
    public ResponseEntity<Void> createMatzip(@RequestBody CreateMatzipRequest matzipRequest) {

        Long listMatzipSeq = matzipCommandService.createMatzip(matzipRequest);

        return ResponseEntity.created(URI.create("/api/v1/list/matzip" + listMatzipSeq)).build();
    }
}
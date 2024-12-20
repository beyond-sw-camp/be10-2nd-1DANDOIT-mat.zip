package com.matzip.matzipback.board.command.application.controller;

import com.google.gson.Gson;
import com.matzip.matzipback.board.command.application.dto.PostAndTagRequestDTO;
import com.matzip.matzipback.board.command.application.service.PostCommandService;
import com.matzip.matzipback.common.util.CustomUserUtils;
import com.matzip.matzipback.exception.RestApiException;
import com.matzip.matzipback.responsemessage.SuccessResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import static com.matzip.matzipback.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.matzip.matzipback.exception.ErrorCode.UNAUTHORIZED_REQUEST;
import static com.matzip.matzipback.responsemessage.SuccessCode.BASIC_DELETE_SUCCESS;
import static com.matzip.matzipback.responsemessage.SuccessCode.BASIC_UPDATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/api/v1")
@Tag(name = "Post", description = "게시글")
public class PostCommandController {

    private final PostCommandService postCommandService;
    private final Gson gson;    // Gson Bean 주입 받기

    /* 1. 게시글 등록, 이미지 업로드, 이미지 삭제 */
    // 게시글 기본 정보 + 태그 등록
    @PostMapping("/posts")
    @Operation(summary = "게시글 등록", description = "게시글을 등록한다.")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> registPost(
            @Valid @RequestBody PostAndTagRequestDTO newPost    // 게시글 정보 + 태그 정보
    ){

        // 게시글 등록 (에디터 테스트용)
/*        Long postSeq = postCommandService.createPost(newPost);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/back/api/v1/posts/" + postSeq))    // 리소스가 생성된 위치
                .build();
 */

        try{
            if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("user")) {

                // 게시글 등록
                Long postSeq = postCommandService.createPost(newPost);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .location(URI.create("/back/api/v1/posts/" + postSeq))    // 리소스가 생성된 위치
                        .build();
            } else {
                throw new RestApiException(FORBIDDEN_ACCESS);
            }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }

    }

    // 이미지가 업로드 될 때 마다 작동할 메소드
    // 이미지 파일을 업로드하고, 성공적으로 저장된 경우 이미지의 URL과 응답 코드를 JSON 형식으로 반환
    @PostMapping(value ="/posts/uploadImage", produces = "application/json")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadPostImageFile(@RequestParam("image") MultipartFile file) {

        JsonObject jsonObject = new JsonObject();

        // 저장될 외부 파일 경로
        String fileRoot = "/Users/jiyoung/Desktop/toast_image/";	// MacBook
        // String fileRoot = "C:\\toast_image\\";	// Windows 사용시 설정 경로

        // 디렉토리 존재 여부 체크
        File directory = new File(fileRoot);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
        }

        String originalFileName = file.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일명
        File targetFile = new File(fileRoot + savedFileName);   // 최종 저장할 파일 객체 생성

        try {
            InputStream fileStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/toastImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");

            // URL 출력
            System.out.println("Image URL: " + "/toastImage/" + savedFileName);

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        // Gson을 사용하여 JSON 문자열 반환
        return ResponseEntity.ok(gson.toJson(jsonObject));

    }

    /* 2. 게시글 수정 */
    @PutMapping("/posts/{postSeq}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정한다.")
    public ResponseEntity<SuccessResMessage> updatePost(
            @PathVariable Long postSeq,
            @Valid @RequestBody PostAndTagRequestDTO updatedPost
    ) {

        try{
            if (CustomUserUtils.getCurrentUserAuthorities().iterator().next().getAuthority().equals("user")) {

                // 게시글 수정
                postCommandService.updatePost(postSeq, updatedPost);

                return ResponseEntity.ok(new SuccessResMessage(BASIC_UPDATE_SUCCESS));
            } else {
                throw new RestApiException(FORBIDDEN_ACCESS);
            }
        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }
    }

    /* 3. 게시글 삭제 */
    @DeleteMapping("/posts/{postSeq}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제한다.")
    public ResponseEntity<SuccessResMessage> deletePost(@PathVariable Long postSeq) {

        try{
            // 게시글 삭제
            postCommandService.deletePost(postSeq);

            return ResponseEntity.ok(new SuccessResMessage(BASIC_DELETE_SUCCESS));

        } catch (NullPointerException e) { throw new RestApiException(UNAUTHORIZED_REQUEST); }
    }

}

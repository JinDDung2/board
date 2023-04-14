package com.example.sns.controller;

import com.example.sns.entity.dto.post.*;
import com.example.sns.service.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;

    @ApiOperation(value = "글쓰기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "postId, message 반환"),
            @ApiResponse(code = 401, message = "사용자 권한이 없습니다.")
    })
    @PostMapping("")
    public RsData<PostCreateResponseDto> createPost(@RequestBody @Validated PostCreateRequestDto requestDto, Authentication authentication) {
        PostCreateResponseDto responseDto = postService.createPost(requestDto, authentication.getName());
        return RsData.success(responseDto);
    }

    @ApiOperation(value = "최신 순으로 20개씩 표시 (Pageable 사용)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "body, pageable 반환")
    })
    @GetMapping("")
    public RsData<Page<PostReadResponseDto>> findAllPost(@PageableDefault(sort = "id", direction = Sort.Direction.DESC,size = 20) Pageable pageable) {
        Page<PostReadResponseDto> pages = postService.findAllByPage(pageable);
        return RsData.success(pages);
    }

    @ApiOperation(value = "마이피드 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "pageable 반환"),
            @ApiResponse(code = 401, message = "사용자 권한이 없습니다.")
    })
    @GetMapping("/my")
    public RsData<Page<PostMyFeedResponseDto>> findMyFeed(@PageableDefault(sort = "createdDate",
            direction = Sort.Direction.DESC,size = 20) Pageable pageable,
                                                          Authentication authentication) {
        Page<PostMyFeedResponseDto> myFeed = postService.findMyFeed(authentication.getName(), pageable);
        return RsData.success(myFeed);
    }

    @ApiOperation(value = "포스트 단건 조회")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "id, title, body, userName, createdDate(yyyy-mm-dd hh:mm:ss), modifiedDate(yyyy-mm-dd hh:mm:ss)")
    })
    @GetMapping("/{postId}")
    public RsData<PostReadResponseDto> findById(@PathVariable Integer postId) {
        PostReadResponseDto responseDto = postService.findById(postId);
        return RsData.success(responseDto);
    }

    @ApiOperation(value = "포스트 수정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "postId, message"),
            @ApiResponse(code = 401, message = "작성자와 수정자 불일치 or 사용자 권한이 없습니다."),
            @ApiResponse(code = 404, message = "해당 포스트 없음")
    })
    @PutMapping("/{postId}")
    public RsData<PostUpdateResponseDto> update(@PathVariable Integer postId,
                                                @RequestBody @Validated PostUpdateRequestDto requestDto,
                                                Authentication authentication) {
        PostUpdateResponseDto responseDto = postService.update(requestDto, postId, authentication.getName());
        return RsData.success(responseDto);
    }

    @ApiOperation(value = "포스트 삭제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "postId, message"),
            @ApiResponse(code = 401, message = "작성자와 삭제유저 불일치 or 사용자 권한이 없습니다."),
            @ApiResponse(code = 404, message = "해당 포스트 없음, 유저 존재하지 않음")
    })
    @DeleteMapping("/{postId}")
    public RsData<PostDeleteResponseDto> deleteById(@PathVariable Integer postId,
                                                    Authentication authentication) {
        PostDeleteResponseDto responseDto = postService.deleteById(postId, authentication.getName());
        return RsData.success(responseDto);
    }

    @ApiOperation(value = "좋아요 추가")
    @ApiResponses({
            @ApiResponse(code = 200, message = "좋아요를 눌렀습니다."),
            @ApiResponse(code = 404, message = "해당 포스트가 없습니다."),
            @ApiResponse(code = 404, message = "UserName을 찾을 수 없습니다."),
    })
    @PostMapping("/{postId}/likes")
    public RsData<String> likes(@PathVariable Integer postId, Authentication authentication) {
        boolean result = postService.isLike(postId, authentication.getName());
        return RsData.success(result ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다.");
    }

    @ApiOperation(value = "좋아요 개수 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "좋아요 개수"),
            @ApiResponse(code = 404, message = "해당 포스트가 없습니다.")
    })
    @GetMapping("/{postId}/likes")
    public RsData<Integer> getLikes(@PathVariable Integer postId) {
        return RsData.success(postService.findById(postId).getPostLikes());
    }


}

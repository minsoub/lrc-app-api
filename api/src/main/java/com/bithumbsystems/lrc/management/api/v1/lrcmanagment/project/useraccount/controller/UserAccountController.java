package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserSaveRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "담당자 정보", description = "담당자 정보 API")
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 담당자 정보 리스트 project id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/user-account/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 리스트 project id로 찾기", description = "projectId를 이용하여 담당자 정보 리스트를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 검색")
    public ResponseEntity<Mono<?>> getUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                      @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(userAccountService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }
    /**
     * 담당자 정보 리스트 project id로 찾기 (UnMasking)
     * @param projectId
     * @param reason
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/user-account/unmasking/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 리스트 project id로 찾기", description = "projectId를 이용하여 담당자 정보 리스트를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 검색")
    public ResponseEntity<Mono<?>> getUserAccountUnMask(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH) @PathVariable("projectId") String projectId,
                                                        @Parameter(name = "reason", description = "개인정보 열람 사유", required = true) @RequestParam(required = true) String reason,
                                                        @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(userAccountService.unMaskfindByProjectId(projectId, reason, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 생성자 정보 project id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/create-user-account/{projectId}")
    @Operation(summary = "거래지원 관리 - 생성자 정보 project id로 찾기", description = "projectId를 이용하여 생성자 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 생성자 정보 > 검색")
    public ResponseEntity<Mono<?>> getCreateUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                  @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(userAccountService.findCreateUserByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }
    /**
     * 담당자 정보 신규 저장
     * @param userAccountRequest
     * @return ReviewEstimateResponse Object
     */
    @PostMapping("/user-account/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 신규 저장", description = "projectId를 이용하여 담당자 정보를 저장합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 신규 저장")
    public ResponseEntity<Mono<?>> createUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                         @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "user Object", description = "사용자 정보", in = ParameterIn.PATH)
                                                     @RequestBody UserAccountRequest userAccountRequest, @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(userAccountService.create(projectId, userAccountRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 거래지원 담당자 일괄 수정
     * @param projectId
     * @param userSaveRequest
     * @param account
     * @return
     */
    @PostMapping("/user-accounts/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 수정", description = "projectId를 이용하여 담당자 정보를 수정합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 수정")
    public ResponseEntity<Mono<?>> saveUserAccounts(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                     @PathVariable("projectId") String projectId,
                                                    @Parameter(name = "user Object", description = "사용자 정보", in = ParameterIn.PATH)
                                                     @RequestBody UserSaveRequest userSaveRequest, @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(userAccountService.save(projectId, userSaveRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 거래지원 사용자 검색
     * @param keyword
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/user-account")
    @Operation(summary = "거래지원 관리 - 거래지원 사용자 찾기", description =  "keyword를 이용하여 거래지원 사용자 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 검색")
    public ResponseEntity<Mono<?>> getUserAccountSearch(@Parameter(name = "keyword", description = "검색 단어", required = true) @RequestParam(required = true) String keyword) {
        return ResponseEntity.ok().body(userAccountService.findUserSearch(keyword)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 생성자 정보 project id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @DeleteMapping("/user-account/{projectId}/{id}")
    @Operation(summary = "거래지원 관리 - 담당자 탈퇴 처리", description = "해당 프로젝트 담당자 탈퇴 처리", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 탈퇴 처리")
    public ResponseEntity<Mono<?>> deleteUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH) @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "id", description = "담당자 ID", in = ParameterIn.PATH) @PathVariable("id") String id,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(userAccountService.deleteUser(projectId, id, account)
                .map(c -> new SingleResponse(c))
        );
    }
}

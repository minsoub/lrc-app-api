package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment")
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 담당자 정보 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/user-account/{projectId}")
    public ResponseEntity<Mono<?>> getUserAccount(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(userAccountService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 담당자 정보 여러개 저장 및 업데이트
     * @param userAccountRequest
     * @return ReviewEstimateResponse Object
     */
    @PostMapping("/user-account/{projectId}")
    public ResponseEntity<Mono<?>> createUserAccount(@PathVariable("projectId") String projectId,
                                                        @RequestBody UserAccountRequest userAccountRequest) {
        return ResponseEntity.ok().body(userAccountService.create(projectId, userAccountRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}

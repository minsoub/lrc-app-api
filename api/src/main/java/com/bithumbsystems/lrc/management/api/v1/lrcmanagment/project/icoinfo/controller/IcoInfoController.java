package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.service.IcoInfoService;
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
@Tag(name = "상장 정보", description = "상장 정보 API")
public class IcoInfoController {

    private final IcoInfoService icoInfoService;


    /**
     * 상장 정보 찾기
     * @param projectId
     * @return IcoInfoResponse Object
     */
    @GetMapping("/ico-info/{projectId}")
    @Operation(summary = "상장 정보 조회", description = "projectId를 이용하여 상정 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getIcoInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                  @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(icoInfoService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
   }

    /**
     * 상장 정보 여러개 저장 및 업데이트
     * @param icoInfoRequest
     * @return IcoInfoResponse Object
     */
    @PostMapping("/ico-info/{projectId}")
    @Operation(summary = "상장 정보 여러개 저장 및 업데이트", description = "projectId를 이용하여 상정 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createIcoInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                     @PathVariable("projectId") String projectId,
                                                 @RequestBody IcoInfoRequest icoInfoRequest,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(icoInfoService.create(projectId, icoInfoRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }
}

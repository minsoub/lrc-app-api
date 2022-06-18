package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.service.IcoInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
public class IcoInfoController {

    private final IcoInfoService icoInfoService;


    /**
     * 상장 정보 찾기
     * @param projectId
     * @return IcoInfoResponse Object
     */
    @GetMapping("/ico-info/{projectId}")
    public ResponseEntity<Mono<?>> getIcoInfo(@PathVariable("projectId") String projectId) {
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
    public ResponseEntity<Mono<?>> createIcoInfo(@PathVariable("projectId") String projectId,
                                                 @RequestBody IcoInfoRequest icoInfoRequest) {
        return ResponseEntity.ok().body(icoInfoService.create(projectId, icoInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}

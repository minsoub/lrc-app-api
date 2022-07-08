package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request.LineMngRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.service.LineMngService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
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
@RequestMapping("lrc/statusmanagment")
@Tag(name = "계열 관리", description = "계열 관리 API")
public class LineMngController {

    private final LineMngService lineMngService;

    /**
     * 계열 모두 가져오기
     * @return BusinessListResponse
     */
    @GetMapping("/line-code")
    @Operation(summary = "계열 모두 가져오기", description = "계열 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getLines(LineType type) {
        return ResponseEntity.ok().body(lineMngService.findAll(type)
                .collectList()
                .map(c -> new MultiResponse(c))
        );
    }


    /**
     * 계열관리 1개 저장
     * @param lineMngRequest
     * @return BusinessListResponse
     */
    @PostMapping("/line-code")
    @Operation(summary = "계열관리 1개 저장", description = "계열관리 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createLine(@Parameter(name = "business Object", description = "계열관리 Model", in = ParameterIn.PATH)
                                                      @RequestBody LineMngRequest lineMngRequest,
                                                  @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(lineMngService.create(lineMngRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 계열관리 업데이트
     * @param id
     * @param lineMngRequest
     * @return BusinessListResponse
     */
    @PutMapping("/line-code/{id}")
    @Operation(summary = "계열관리 업데이트", description = "계열관리 정보를 수정합니다.")
    public ResponseEntity<Mono<?>> updateLines(@Parameter(name = "id", description = "계열관리 id", in = ParameterIn.PATH)
                                                      @PathVariable("id") String id,
                                                  @Parameter(name = "serviceLog", description = "계열관리 Model", in = ParameterIn.PATH)
                                                  @RequestBody LineMngRequest lineMngRequest,
                                               @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(lineMngService.updateLine(id, lineMngRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 계열관리 삭제
     * @param id
     * @return null
     */
    @DeleteMapping("/line-code/{id}")
    @Operation(summary = "계열관리 삭제", description = "계열관리 정보를 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteBusiness(@Parameter(name = "id", description = "계열관리 id", in = ParameterIn.PATH)
                                                      @PathVariable("id") String id,
                                                  @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(lineMngService.deleteLine(id, account)
                .then(Mono.just(new SingleResponse()))
        );
    }
}

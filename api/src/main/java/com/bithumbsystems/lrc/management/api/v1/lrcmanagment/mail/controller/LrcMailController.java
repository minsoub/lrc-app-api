package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.mail.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.mail.service.LrcMailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/mail")
@Tag(name = "재단", description = "재단 Mail API")
public class LrcMailController {
    private final LrcMailService mailService;

    /**
     * 알림 메일 전송
     * @return FoundationResponse
     */
    @GetMapping("/send")
    @Operation(summary = "거래지원 관리 - 알림 메일 발송하기", description = "알림 메일을 발송한다.", tags = "사이트 운영  > 거래지원 관리 > 알림메일 전송색")
    public ResponseEntity<Mono<?>> sendEmail(@Parameter(name = "email", description = "이메일", in = ParameterIn.QUERY) @RequestParam(required = true) String email,
                                             @Parameter(name = "type", description = "구분", in = ParameterIn.QUERY) @RequestParam(required = true) String type) {
        return ResponseEntity.ok().body(mailService.sendEmail(email, type)
                .map(c -> new SingleResponse(c))
        );
    }
}

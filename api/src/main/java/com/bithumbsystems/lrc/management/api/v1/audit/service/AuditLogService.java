package com.bithumbsystems.lrc.management.api.v1.audit.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.util.MaskingUtil;
import com.bithumbsystems.lrc.management.api.v1.accesslog.request.AccessLogRequest;
import com.bithumbsystems.lrc.management.api.v1.audit.mapper.AuditLogMapper;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogDetailResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogSimpleResponse;
import com.bithumbsystems.persistence.mongodb.accesslog.model.enums.ActionType;
import com.bithumbsystems.persistence.mongodb.audit.service.AuditLogDomainService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogDomainService auditLogDomainService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Mono<List<AuditLogSimpleResponse>> findAuditServiceLog(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {

        return auditLogDomainService.findPageBySearchText(
                        fromDate,
                        toDate, keyword, mySiteId)
                .flatMap(result -> {
                    result.setEmail(MaskingUtil.getEmailMask(result.getEmail()));
                    return Mono.just(result);
                })
                .map(AuditLogMapper.INSTANCE::auditLogSimpleResponse)
                .collectSortedList(Comparator.comparing(AuditLogSimpleResponse::getCreateDate).reversed());
    }

    /**
     * ????????? ?????? ?????? ????????? ????????????.
     *
     * @param id
     * @return
     */
    public Mono<AuditLogDetailResponse> findAuditServiceLogDetail(String id) {
        return auditLogDomainService.findById(id)
                .map(AuditLogMapper.INSTANCE::auditLogDetailResponse);
    }

    public Mono<ByteArrayInputStream> downloadExcel(LocalDate fromDate, LocalDate toDate, String keyword, String reason, Account account) {

        // log ??????
        String title = "????????? ?????? ?????? > ?????? ????????????";

        applicationEventPublisher.publishEvent(
                AccessLogRequest.builder()
                        .accountId(account.getAccountId())
                        .actionType(ActionType.DOWNLOAD)
                        .reason(reason)
                        .email(account.getEmail())
                        .description(title)
                        .siteId(account.getMySiteId())
                        .ip(account.getUserIp())
                        .build()
        );
        return auditLogDomainService.findPageBySearchText(
                        fromDate,
                        toDate, keyword, account.getMySiteId())
                .map(AuditLogMapper.INSTANCE::auditLogResponse)
                .collectSortedList(Comparator.comparing(AuditLogResponse::getCreateDate).reversed())
                .flatMap(this::createExcelFile);
    }

    private Mono<ByteArrayInputStream> createExcelFile(List<AuditLogResponse> fraudReportList) {
        return Mono.fromCallable(() -> {
                    log.debug("?????? ?????? ?????? ??????");

                    SXSSFWorkbook workbook = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE);  // keep 100 rows in memory, exceeding rows will be flushed to disk
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    CreationHelper creationHelper = workbook.getCreationHelper();

                    Sheet sheet = workbook.createSheet("???????????????");

                    Font headerFont = workbook.createFont();
                    headerFont.setFontName("?????? ??????");
                    headerFont.setFontHeight((short) (10 * 20));
                    headerFont.setBold(true);
                    headerFont.setColor(IndexedColors.BLACK.index);

                    Font bodyFont = workbook.createFont();
                    bodyFont.setFontName("?????? ??????");
                    bodyFont.setFontHeight((short) (10 * 20));

                    // Cell ????????? ??????
                    CellStyle headerStyle = workbook.createCellStyle();
                    headerStyle.setAlignment(HorizontalAlignment.CENTER);
                    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerStyle.setFont(headerFont);

                    // Row for Header
                    Row headerRow = sheet.createRow(0);

                    // Header
                    String[] fields = {"SN", "ID", "??????IP", "??????", "CRUD", "URI", "Parameter", "????????????"};
                    for (int col = 0; col < fields.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        cell.setCellValue(fields[col]);
                        cell.setCellStyle(headerStyle);
                    }

                    // Body
                    int rowIdx = 1;
                    for (AuditLogResponse res : fraudReportList) {
                        Row row = sheet.createRow(rowIdx++);

                        row.createCell(0).setCellValue(res.getId());
                        row.createCell(1).setCellValue(res.getEmail());
                        row.createCell(2).setCellValue(res.getIp());
                        row.createCell(3).setCellValue(res.getMenuName());
                        row.createCell(4).setCellValue(res.getMethod());
                        row.createCell(5).setCellValue(res.getUri());
                        row.createCell(6).setCellValue(res.getQueryParams());
                        row.createCell(7).setCellValue(res.getCreateDate().plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                    workbook.write(out);

                    log.debug("?????? ?????? ?????? ??????");
                    return new ByteArrayInputStream(out.toByteArray());
                })
                .log();
    }

}

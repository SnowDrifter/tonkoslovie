package ru.romanov.tonkoslovie.audit.web;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.tonkoslovie.audit.dto.AuditDto;
import ru.romanov.tonkoslovie.audit.service.AuditService;
import ru.romanov.tonkoslovie.model.web.RestPage;

import jakarta.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/api/audits")
    public RestPage<AuditDto> audits(@RequestParam(defaultValue = "0") @Min(0) int page,
                                    @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                    @RequestParam(required = false) String table,
                                    @RequestParam(required = false) String entityId) {
        return auditService.findAuditRecords(page, size, table, entityId);
    }

}

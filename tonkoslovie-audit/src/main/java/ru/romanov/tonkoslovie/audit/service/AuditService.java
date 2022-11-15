package ru.romanov.tonkoslovie.audit.service;

import ru.romanov.tonkoslovie.audit.dto.AuditDto;
import ru.romanov.tonkoslovie.model.web.RestPage;


public interface AuditService {

    RestPage<AuditDto> findAuditRecords(int page, int size, String table, String entityId);

}

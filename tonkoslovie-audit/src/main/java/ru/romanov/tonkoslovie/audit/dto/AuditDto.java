package ru.romanov.tonkoslovie.audit.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class AuditDto {

    private String id;
    private String table;
    private String entityId;
    private Map<String, String> before;
    private Map<String, String> after;
    private AuditOperation operation;
    private Date createdAt;

}

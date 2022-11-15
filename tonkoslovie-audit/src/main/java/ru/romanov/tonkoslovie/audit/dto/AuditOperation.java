package ru.romanov.tonkoslovie.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AuditOperation {

    CREATE("c"),
    UPDATE("u"),
    DELETE("d"),
    READ("r");

    private final String code;

    private static final Map<String, AuditOperation> VALUES;
    static {
        VALUES = Arrays.stream(AuditOperation.values())
                .collect(Collectors.toMap(AuditOperation::getCode, Function.identity()));
    }

    public static AuditOperation fromCode(String code) {
        return VALUES.get(code);
    }

}

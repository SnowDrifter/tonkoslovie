package ru.romanov.tonkoslovie.audit.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.romanov.tonkoslovie.audit.dto.AuditDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditResponse {

    private List<AuditDto> data;

}

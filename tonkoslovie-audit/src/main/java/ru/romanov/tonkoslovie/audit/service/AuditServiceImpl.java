package ru.romanov.tonkoslovie.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.audit.dto.AuditDto;
import ru.romanov.tonkoslovie.audit.dto.AuditMapper;
import ru.romanov.tonkoslovie.audit.elastic.Audit;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public RestPage<AuditDto> findAuditRecords(int page, int size, String table, String entityId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("source.ts_ms").descending());
        Query query = createQuery(pageable, table, entityId);

        SearchHits<Audit> search = elasticsearchTemplate.search(query, Audit.class);

        return RestPage.of(convertSearchHits(search, pageable));
    }

    private Query createQuery(Pageable pageable, String table, String entityId) {
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(table)) {
            criteria.and(new Criteria("source.table").is(table));
        }
        if (StringUtils.hasText(entityId)) {
            criteria.subCriteria(
                    new Criteria("before.id").is(entityId)
                            .or("after.id").is(entityId)
            );
        }
        return new CriteriaQuery(criteria, pageable);
    }

    private Page<AuditDto> convertSearchHits(SearchHits<Audit> searchHits, Pageable pageable) {
        if (searchHits.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }

        List<AuditDto> content = searchHits.stream()
                .map(SearchHit::getContent)
                .map(AuditMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

}

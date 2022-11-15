package ru.romanov.tonkoslovie.audit.service;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public RestPage<AuditDto> findAuditRecords(int page, int size, String table, String entityId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Query query = createQuery(pageable, table, entityId);

        SearchHits<Audit> search = elasticsearchTemplate.search(query, Audit.class);

        return RestPage.of(convertSearchHits(search, pageable));
    }

    private Query createQuery(Pageable pageable, String table, String entityId) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(table)) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(table).field("source.table"));
        }
        if (StringUtils.hasText(entityId)) {
            boolQueryBuilder.must(
                    QueryBuilders.boolQuery()
                            .should(QueryBuilders.queryStringQuery(entityId).field("before.id"))
                            .should(QueryBuilders.queryStringQuery(entityId).field("after.id"))
            );
        }

        return new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
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

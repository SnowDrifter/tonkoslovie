package ru.romanov.tonkoslovie.hibernate.specification;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.query.sqm.ComparisonOperator;
import org.hibernate.query.sqm.internal.SqmCriteriaNodeBuilder;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.query.sqm.tree.predicate.SqmComparisonPredicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = createPredicate(root, builder);
        return builder.and(predicate);
    }

    @SneakyThrows
    private Predicate createPredicate(Root<T> root, CriteriaBuilder builder) {
        SqmCriteriaNodeBuilder sqmBuilder = (SqmCriteriaNodeBuilder) builder;

        Class<?> fieldType = root.get(criteria.getKey()).getJavaType();

        if (fieldType == String.class) {
            return builder.like(root.get(criteria.getKey()), String.format("%%%s%%", criteria.getValue()));
        } else {
            Object value = convertValue(criteria.getValue(), fieldType);

            return new SqmComparisonPredicate(
                    (SqmBasicValuedSimplePath<Object>) root.get(criteria.getKey()),
                    getCompressionOperator(),
                    sqmBuilder.value(value),
                    sqmBuilder
            );
        }
    }

    private ComparisonOperator getCompressionOperator() {
        return switch (criteria.getOperation()) {
            case ">" -> ComparisonOperator.GREATER_THAN;
            case "<" -> ComparisonOperator.LESS_THAN;
            default -> ComparisonOperator.EQUAL;
        };
    }

    private Object convertValue(String value, Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(value);
        } else if (type == Date.class) {
            return new Date(Long.parseLong(value));
        } else if (type == Long.class) {
            return Long.valueOf(value);
        } else if (type == Integer.class) {
            return Integer.valueOf(value);
        }

        return value;
    }
}

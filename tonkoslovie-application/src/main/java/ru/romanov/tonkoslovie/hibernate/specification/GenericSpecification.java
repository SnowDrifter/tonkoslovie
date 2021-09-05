package ru.romanov.tonkoslovie.hibernate.specification;

import lombok.AllArgsConstructor;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate;
import org.hibernate.query.criteria.internal.predicate.LikePredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = createPredicate(root, builder);
        return builder.and(predicate);
    }

    private Predicate createPredicate(Root<T> root, CriteriaBuilder builder) {
        Class<?> fieldType = root.get(criteria.getKey()).getJavaType();

        if (fieldType == String.class) {
            return new LikePredicate((CriteriaBuilderImpl) builder, root.get(criteria.getKey()), String.format("%%%s%%", criteria.getValue()));
        } else {
            Object value = convertValue(criteria.getValue(), fieldType);
            ComparisonPredicate.ComparisonOperator operator = getCompressionOperator();
            return builder.and(new ComparisonPredicate((CriteriaBuilderImpl) builder, operator, root.get(criteria.getKey()), value));
        }
    }

    private ComparisonPredicate.ComparisonOperator getCompressionOperator() {
        switch (criteria.getOperation()) {
            case ">":
                return ComparisonPredicate.ComparisonOperator.GREATER_THAN;
            case "<":
                return ComparisonPredicate.ComparisonOperator.LESS_THAN;
            default:
                return ComparisonPredicate.ComparisonOperator.EQUAL;
        }
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

package ru.romanov.tonkoslovie.hibernate.specification;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case ">":
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue());
            case "<":
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue());
            case ":":
                Class<?> fieldType = root.get(criteria.getKey()).getJavaType();

                if (fieldType == String.class) {
                    return builder.like(root.get(criteria.getKey()), String.format("%%%s%%", criteria.getValue()));
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    return builder.equal(root.get(criteria.getKey()), Boolean.valueOf(criteria.getValue()));
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
        }
        return null;
    }
}

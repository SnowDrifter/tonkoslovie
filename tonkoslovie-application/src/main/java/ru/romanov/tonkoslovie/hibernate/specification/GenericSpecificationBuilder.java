package ru.romanov.tonkoslovie.hibernate.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GenericSpecificationBuilder<T> {

    private final List<SearchCriteria> parameters = new ArrayList<>();

    private static final Pattern SEARCH_QUERY_PATTERN = Pattern.compile("(\\w+)([:<>])([\\w\\d.@]+);?");

    public GenericSpecificationBuilder<T> addParametersFromSearchQuery(String searchQuery) {
        if (StringUtils.hasText(searchQuery)) {
            Matcher matcher = SEARCH_QUERY_PATTERN.matcher(searchQuery);
            while (matcher.find()) {
                String key = matcher.group(1);
                String operation = matcher.group(2);
                String value = matcher.group(3);

                parameters.add(new SearchCriteria(key, operation, value));
            }
        }

        return this;
    }

    public GenericSpecificationBuilder<T> addParameter(String key, String operation, String value) {
        parameters.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (parameters.isEmpty()) {
            return null;
        }

        List<Specification<T>> specifications = parameters.stream()
                .map(GenericSpecification<T>::new)
                .collect(Collectors.toList());

        Specification<T> result = specifications.get(0);
        for (int i = 1; i < parameters.size(); i++) {
            result =  Specification.where(result).and(specifications.get(i));
        }
        return result;
    }

}

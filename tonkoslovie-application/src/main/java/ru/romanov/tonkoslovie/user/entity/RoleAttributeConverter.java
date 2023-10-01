package ru.romanov.tonkoslovie.user.entity;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleAttributeConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> roles) {
        Set<String> roleNames = roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        return String.join(",", roleNames);
    }

    @Override
    public Set<Role> convertToEntityAttribute(String string) {
        return Arrays.stream(string.split(","))
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}

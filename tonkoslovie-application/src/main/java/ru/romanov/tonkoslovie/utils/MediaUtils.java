package ru.romanov.tonkoslovie.utils;


import java.util.HashSet;
import java.util.Set;

public class MediaUtils {

    private static final Set<String> AVAILABLE_SIZES = new HashSet<>();

    static {
        AVAILABLE_SIZES.add("200_200");
    }

    public static Set<String> getAvailableSizes() {
        return AVAILABLE_SIZES;
    }

}

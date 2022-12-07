package ru.romanov.tonkoslovie.media.utils;


import ru.romanov.tonkoslovie.media.dto.SizeData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaUtils {

    private static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)-(\\d+)\\.(\\w+)");

    public static SizeData parseSize(String size) {
        Matcher matcher = SIZE_PATTERN.matcher(size);

        if (matcher.find()) {
            return SizeData.builder()
                    .height(Integer.parseInt(matcher.group(1)))
                    .width(Integer.parseInt(matcher.group(2)))
                    .format(matcher.group(3))
                    .build();
        } else {
            throw new IllegalArgumentException("Can't parse size " + size);
        }
    }

}

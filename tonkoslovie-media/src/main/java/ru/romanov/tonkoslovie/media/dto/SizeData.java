package ru.romanov.tonkoslovie.media.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SizeData {

    private String format;
    private int height;
    private int width;

}

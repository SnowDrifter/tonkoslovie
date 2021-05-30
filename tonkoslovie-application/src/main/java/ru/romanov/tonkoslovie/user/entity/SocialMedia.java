package ru.romanov.tonkoslovie.user.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocialMedia {

    private Integer vkId;
    private String vkPhoto;

    private String facebookId;

    private String googleId;
    private String googlePhoto;

}

package ru.romanov.tonkoslovie.user.entity;


import lombok.Data;

@Data
public class SocialMedia {

    private Integer vkId;
    private String vkBirthDate;
    private Integer vkSex;
    private String vkPhoto;
    private Integer vkFriendStatus;
    private Integer vkRelation;
    private Integer vkRelationPartner;

    private String facebookId;

    private String googleId;
    private String googlePhoto;

}

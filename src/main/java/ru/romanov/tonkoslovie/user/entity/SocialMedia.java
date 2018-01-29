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


    private Long facebookId;

    private Long googleId;
    private String googlePhoto;

}
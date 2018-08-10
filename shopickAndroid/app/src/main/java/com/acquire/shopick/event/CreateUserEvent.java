package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/27/15.
 */
public class CreateUserEvent {
    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    private String AuthToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    private String profileImageUrl;

    public String getProfileCoverUrl() {
        return profileCoverUrl;
    }

    public void setProfileCoverUrl(String profileCoverUrl) {
        this.profileCoverUrl = profileCoverUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private String profileCoverUrl ;


    private int gender;

    public int getAgeMax() {
        return age_max;
    }

    public void setAgeMax(int age) {
        this.age_max = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private int age_max;

    public int getAge_min() {
        return age_min;
    }

    public void setAge_min(int age_min) {
        this.age_min = age_min;
    }

    private int age_min;

    public String getFacebookId() {

        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    private String facebookId;

    private String loginType;


    public CreateUserEvent(String authToken, String email, String name, String profileImageUrl, String profileCoverUrl, int gender, int age_max,int age_min, String facebookId, String loginType ) {
        AuthToken = authToken;
        this.email =   email;
        this.name = name;
        this.profileCoverUrl = profileCoverUrl;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.age_max = age_max;
        this.age_min = age_min;
        this.facebookId = facebookId;
        this.loginType = loginType;
    }
}

package com.educhain.dto.response;

public class UniversityResponse {

    private Long id;
    private String universityName;
    private String email;
    private String address;
    private String phone;

    public UniversityResponse() {
    }

    public UniversityResponse(Long id, String universityName, String email, String address, String phone) {
        this.id = id;
        this.universityName = universityName;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
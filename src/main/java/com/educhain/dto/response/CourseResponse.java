package com.educhain.dto.response;

public class CourseResponse {

    private Long id;
    private String courseName;
    private String courseCode;
    private String department;
    private Integer credits;
    private Integer duration;
    private String description;

    // University Details
    private Long universityId;
    private String universityName;

    public CourseResponse() {
    }

    public CourseResponse(Long id,
                          String courseName,
                          String courseCode,
                          String department,
                          Integer credits,
                          Integer duration,
                          String description,
                          Long universityId,
                          String universityName) {

        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.credits = credits;
        this.duration = duration;
        this.description = description;
        this.universityId = universityId;
        this.universityName = universityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}
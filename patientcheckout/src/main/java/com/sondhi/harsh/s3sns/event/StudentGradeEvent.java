package com.sondhi.harsh.s3sns.event;

public class StudentGradeEvent {
    public String rollNo;
    public String name;
    public Integer testScore;
    public String grade;

    public StudentGradeEvent() {
    }

    public StudentGradeEvent(String rollNo, String name, Integer testScore, String grade) {
        this.rollNo = rollNo;
        this.name = name;
        this.testScore = testScore;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "StudentGradeEvent{" +
                "rollNo='" + rollNo + '\'' +
                ", name='" + name + '\'' +
                ", testScore=" + testScore +
                ", grade='" + grade + '\'' +
                '}';
    }
}

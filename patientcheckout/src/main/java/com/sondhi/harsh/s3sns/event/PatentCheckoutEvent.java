package com.sondhi.harsh.s3sns.event;

public class PatentCheckoutEvent {

    public String firstName;
    public String middleName;
    public String lastName;
    public String ssn;

    public PatentCheckoutEvent() {
    }

    public PatentCheckoutEvent(String firstName, String middleName, String lastName, String ssn) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "PatentCheckoutEvent{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ssn='" + ssn + '\'' +
                '}';
    }
}

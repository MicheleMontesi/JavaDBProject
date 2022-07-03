package model;

import java.util.Date;
import java.util.Objects;

public record Patient(String fiscalCode, String name, String surname, Date birthday, String residence, String gender,
                      int patientId, boolean privacyDocumentation, boolean consentTreatment, boolean acceptRules)
        implements PersonRelated {

    public Patient(String fiscalCode, String name, String surname, Date birthday, String residence, String gender,
                   int patientId, boolean privacyDocumentation, boolean consentTreatment, boolean acceptRules) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.name = Objects.requireNonNull(name);
        this.surname = Objects.requireNonNull(surname);
        this.birthday = Objects.requireNonNull(birthday);
        this.residence = Objects.requireNonNull(residence);
        this.gender = Objects.requireNonNull(gender);
        this.patientId = patientId;
        this.privacyDocumentation = privacyDocumentation;
        this.consentTreatment = consentTreatment;
        this.acceptRules = acceptRules;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", residence='" + residence + '\'' +
                ", gender='" + gender + '\'' +
                ", patientId=" + patientId +
                ", privacyDocumentation=" + privacyDocumentation +
                ", consentTreatment=" + consentTreatment +
                ", acceptRules=" + acceptRules +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return patientId == patient.patientId
                && privacyDocumentation == patient.privacyDocumentation
                && consentTreatment == patient.consentTreatment
                && acceptRules == patient.acceptRules
                && fiscalCode.equals(patient.fiscalCode)
                && name.equals(patient.name)
                && surname.equals(patient.surname)
                && birthday.equals(patient.birthday)
                && residence.equals(patient.residence)
                && gender.equals(patient.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, name, surname, birthday, residence, gender,
                patientId, privacyDocumentation, consentTreatment, acceptRules);
    }
}

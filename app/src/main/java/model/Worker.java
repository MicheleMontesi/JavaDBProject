package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public record Worker(String fiscalCode, String name, String surname, Optional<Date> birthDay, String residence,
                     String gender, int workerId, String edQualification, boolean suitability, boolean partner,
                     int ECMCredits) {
    public Worker(final String fiscalCode, final String name, final String surname, final Optional<Date> birthDay,
                  final String residence, final String gender, final int workerId, final String edQualification,
                  final boolean suitability, final boolean partner, final int ECMCredits) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.name = Objects.requireNonNull(name);
        this.surname = Objects.requireNonNull(surname);
        this.birthDay = birthDay;
        this.residence = Objects.requireNonNull(residence);
        this.gender = Objects.requireNonNull(gender);
        this.workerId = workerId;
        this.edQualification = Objects.requireNonNull(edQualification);
        this.suitability = suitability;
        this.partner = partner;
        this.ECMCredits = ECMCredits;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDay=" + birthDay +
                ", residence='" + residence + '\'' +
                ", gender='" + gender + '\'' +
                ", workerId=" + workerId +
                ", edQualification='" + edQualification + '\'' +
                ", suitability=" + suitability +
                ", partner=" + partner +
                ", ECMCredits=" + ECMCredits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker that = (Worker) o;
        return workerId == that.workerId
                && suitability == that.suitability
                && partner == that.partner
                && ECMCredits == that.ECMCredits
                && fiscalCode.equals(that.fiscalCode)
                && name.equals(that.name)
                && surname.equals(that.surname)
                && birthDay.equals(that.birthDay)
                && residence.equals(that.residence)
                && gender.equals(that.gender)
                && edQualification.equals(that.edQualification);
    }

}

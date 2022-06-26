package model;

import java.util.Date;
import java.util.Objects;

public record Worker(String fiscalCode, String name, String surname, Date birthDay, String residence,
                     String gender, int workerId, String edQualification, boolean suitability, boolean partner,
                     int ECMCredits) {

    public Worker(final String fiscalCode, final String name, final String surname, final Date birthDay,
                  final String residence, final String gender, final int workerId, final String edQualification,
                  final boolean suitability, final boolean partner, final int ECMCredits) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.name = Objects.requireNonNull(name);
        this.surname = Objects.requireNonNull(surname);
        this.birthDay = Objects.requireNonNull(birthDay);
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
        Worker worker = (Worker) o;
        return workerId == worker.workerId
                && suitability == worker.suitability
                && partner == worker.partner
                && ECMCredits == worker.ECMCredits
                && fiscalCode.equals(worker.fiscalCode)
                && name.equals(worker.name)
                && surname.equals(worker.surname)
                && birthDay.equals(worker.birthDay)
                && residence.equals(worker.residence)
                && gender.equals(worker.gender)
                && edQualification.equals(worker.edQualification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, name, surname, birthDay, residence, gender,
                workerId, edQualification, suitability, partner, ECMCredits);
    }
}

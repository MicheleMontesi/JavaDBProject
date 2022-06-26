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
        return (o instanceof Worker)
                && ((Worker) o).workerId() == this.workerId()
                && Objects.equals(((Worker) o).fiscalCode(), this.fiscalCode())
                && Objects.equals(((Worker) o).name(), this.name())
                && Objects.equals(((Worker) o).surname(), this.surname())
                && ((Worker) o).birthDay() == this.birthDay()
                && Objects.equals(((Worker) o).residence(), this.residence())
                && Objects.equals(((Worker) o).gender(), this.gender())
                && Objects.equals(((Worker) o).edQualification(), this.edQualification())
                && ((Worker) o).suitability() == this.suitability()
                && ((Worker) o).partner() == this.partner()
                && ((Worker) o).ECMCredits() == this.ECMCredits();
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerId, suitability, partner, ECMCredits, fiscalCode,
                name, surname, birthDay, residence, gender, edQualification);
    }
}

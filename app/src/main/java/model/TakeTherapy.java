package model;

import java.util.Objects;

public record TakeTherapy(String fiscalCode, int therapyId) {

    public TakeTherapy(String fiscalCode, int therapyId) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.therapyId = therapyId;
    }

    @Override
    public String toString() {
        return "TakeTherapy{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", therapyId=" + therapyId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TakeTherapy that = (TakeTherapy) o;
        return therapyId == that.therapyId && fiscalCode.equals(that.fiscalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, therapyId);
    }
}

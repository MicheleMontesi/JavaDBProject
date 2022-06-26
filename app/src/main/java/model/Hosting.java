package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public record Hosting(String fiscalCode, Date beginDate, Optional<Date> endDate, String unitCode) {

    public Hosting(String fiscalCode, Date beginDate, Optional<Date> endDate, String unitCode) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.beginDate = Objects.requireNonNull(beginDate);
        this.endDate = Objects.requireNonNull(endDate);
        this.unitCode = Objects.requireNonNull(unitCode);
    }

    @Override
    public String toString() {
        return "Hosting{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", unitCode='" + unitCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hosting hosting = (Hosting) o;
        return fiscalCode.equals(hosting.fiscalCode)
                && beginDate.equals(hosting.beginDate)
                && endDate.equals(hosting.endDate)
                && unitCode.equals(hosting.unitCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, beginDate, endDate, unitCode);
    }
}

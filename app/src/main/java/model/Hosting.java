package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Hosting(String fiscalCode, Date beginDate, Optional<Date> endDate, String unitId) implements Entity {

    public Hosting(String fiscalCode, Date beginDate, Optional<Date> endDate, String unitId) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.beginDate = Objects.requireNonNull(beginDate);
        this.endDate = Objects.requireNonNull(endDate);
        this.unitId = Objects.requireNonNull(unitId);
    }

    @Override
    public String toString() {
        return "Hosting{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", unitId='" + unitId + '\'' +
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
                && unitId.equals(hosting.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, beginDate, endDate, unitId);
    }

    @Override
    public List<String> getId() {
        return List.of(fiscalCode, beginDate.toString(), unitId);
    }
}

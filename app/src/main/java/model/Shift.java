package model;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public record Shift(String fiscalCode, String dayOfTheWeek, LocalTime beginTime, LocalTime endTime,
                    String unitId) implements Entity {

    public Shift(final String fiscalCode, final String dayOfTheWeek, final LocalTime beginTime,
                 final LocalTime endTime, final String unitId) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.dayOfTheWeek = Objects.requireNonNull(dayOfTheWeek);
        this.beginTime = Objects.requireNonNull(beginTime);
        this.endTime = Objects.requireNonNull(endTime);
        this.unitId = Objects.requireNonNull(unitId);
    }

    @Override
    public String toString() {
        return "Turn{" +
                "workerFiscalCode='" + fiscalCode + '\'' +
                ", dayOfTheWeek='" + dayOfTheWeek + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", unitId='" + unitId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return fiscalCode.equals(shift.fiscalCode)
                && dayOfTheWeek.equals(shift.dayOfTheWeek)
                && beginTime.equals(shift.beginTime)
                && endTime.equals(shift.endTime)
                && unitId.equals(shift.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, dayOfTheWeek, beginTime, endTime, unitId);
    }

    @Override
    public List<String> getId() {
        return List.of(fiscalCode, dayOfTheWeek, beginTime.toString(), unitId);
    }
}

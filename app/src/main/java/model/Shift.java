package model;

import java.time.LocalTime;
import java.util.Objects;

public record Shift(String workerFiscalCode, String dayOfTheWeek, LocalTime beginTime, LocalTime endTime,
                    String unitId) {

    public Shift(final String workerFiscalCode, final String dayOfTheWeek, final LocalTime beginTime,
                 final LocalTime endTime, final String unitId) {
        this.workerFiscalCode = workerFiscalCode;
        this.dayOfTheWeek = Objects.requireNonNull(dayOfTheWeek);
        this.beginTime = Objects.requireNonNull(beginTime);
        this.endTime = Objects.requireNonNull(endTime);
        this.unitId = Objects.requireNonNull(unitId);
    }

    @Override
    public String toString() {
        return "Turn{" +
                "workerFiscalCode='" + workerFiscalCode + '\'' +
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
        return workerFiscalCode.equals(shift.workerFiscalCode)
                && dayOfTheWeek.equals(shift.dayOfTheWeek)
                && beginTime.equals(shift.beginTime)
                && endTime.equals(shift.endTime)
                && unitId.equals(shift.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerFiscalCode, dayOfTheWeek, beginTime, endTime, unitId);
    }
}

package model;

import java.time.LocalTime;
import java.util.Objects;

public record Turn(String workerFiscalCode, String dayOfTheWeek, LocalTime beginTime, LocalTime endTime,
                   String unitId) {

    public Turn(final String workerFiscalCode, final String dayOfTheWeek, final LocalTime beginTime,
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
        Turn turn = (Turn) o;
        return workerFiscalCode.equals(turn.workerFiscalCode)
                && dayOfTheWeek.equals(turn.dayOfTheWeek)
                && beginTime.equals(turn.beginTime)
                && endTime.equals(turn.endTime)
                && unitId.equals(turn.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerFiscalCode, dayOfTheWeek, beginTime, endTime, unitId);
    }
}

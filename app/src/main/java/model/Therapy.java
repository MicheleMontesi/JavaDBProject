package model;

import java.util.Date;
import java.util.Objects;

public record Therapy(int therapyId, Date creationDate) {

    public Therapy(int therapyId, Date creationDate) {
        this.therapyId = therapyId;
        this.creationDate = Objects.requireNonNull(creationDate);
    }

    @Override
    public String toString() {
        return "Therapy{" +
                "therapyId=" + therapyId +
                ", creationDate=" + creationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Therapy therapy = (Therapy) o;
        return therapyId == therapy.therapyId && creationDate.equals(therapy.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(therapyId, creationDate);
    }
}

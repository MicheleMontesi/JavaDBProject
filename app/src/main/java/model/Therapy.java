package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record Therapy(int therapyId, Date creationDate, String description) implements Entity{

    public Therapy(int therapyId, Date creationDate, String description) {
        this.therapyId = therapyId;
        this.creationDate = Objects.requireNonNull(creationDate);
        this.description = Objects.requireNonNull(description);
    }

    @Override
    public String toString() {
        return "Therapy{" +
                "therapyId=" + therapyId +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Therapy therapy = (Therapy) o;
        return therapyId == therapy.therapyId && creationDate.equals(therapy.creationDate) && description.equals(therapy.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(therapyId, creationDate, description);
    }

    @Override
    public List<String> getId() {
        return List.of(Objects.toString(therapyId));
    }
}

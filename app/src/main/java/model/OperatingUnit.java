package model;

import java.util.List;
import java.util.Objects;

public record OperatingUnit(String unitId, String type, String name, String location, int bedsNumber,
                            int patientsNumber, boolean operatingAuth, boolean accreditation) implements Entity {

    @Override
    public String toString() {
        return "OperatingUnit{" +
                "unitId='" + unitId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bedsNumber=" + bedsNumber +
                ", patientsNumber=" + patientsNumber +
                ", operatingAuth=" + operatingAuth +
                ", accreditation=" + accreditation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatingUnit that = (OperatingUnit) o;
        return bedsNumber == that.bedsNumber
                && patientsNumber == that.patientsNumber
                && operatingAuth == that.operatingAuth
                && accreditation == that.accreditation
                && unitId.equals(that.unitId)
                && type.equals(that.type)
                && name.equals(that.name)
                && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId, type, name, location, bedsNumber, patientsNumber, operatingAuth, accreditation);
    }

    @Override
    public List<String> getId() {
        return List.of(unitId);
    }
}

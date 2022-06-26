package model;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public record CapitalGoods(String unitId, int goodId, Date purchaseDate, Date nextMaintenance, boolean vehicle,
                           Optional<String> toolName, Optional<String> licencePlate, Optional<String> typology,
                           Optional<Date> insuranceExpiration) {

    public CapitalGoods(String unitId, int goodId, Date purchaseDate, Date nextMaintenance, boolean vehicle,
                        Optional<String> toolName, Optional<String> licencePlate, Optional<String> typology,
                        Optional<Date> insuranceExpiration) {
        this.unitId = Objects.requireNonNull(unitId);
        this.goodId = goodId;
        this.purchaseDate = Objects.requireNonNull(purchaseDate);
        this.nextMaintenance = Objects.requireNonNull(nextMaintenance);
        this.vehicle = vehicle;
        this.toolName = Objects.requireNonNull(toolName);
        this.licencePlate = Objects.requireNonNull(licencePlate);
        this.typology = Objects.requireNonNull(typology);
        this.insuranceExpiration = Objects.requireNonNull(insuranceExpiration);
    }

    @Override
    public String toString() {
        return "CapitalGoods{" +
                "unitId='" + unitId + '\'' +
                ", goodId=" + goodId +
                ", purchaseDate=" + purchaseDate +
                ", nextMaintenance=" + nextMaintenance +
                ", vehicle=" + vehicle +
                ", toolName=" + toolName +
                ", licencePlate=" + licencePlate +
                ", typology=" + typology +
                ", insuranceExpiration=" + insuranceExpiration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapitalGoods that = (CapitalGoods) o;
        return goodId == that.goodId
                && vehicle == that.vehicle
                && unitId.equals(that.unitId)
                && purchaseDate.equals(that.purchaseDate)
                && nextMaintenance.equals(that.nextMaintenance)
                && toolName.equals(that.toolName)
                && licencePlate.equals(that.licencePlate)
                && typology.equals(that.typology)
                && insuranceExpiration.equals(that.insuranceExpiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId, goodId, purchaseDate, nextMaintenance, vehicle,
                toolName, licencePlate, typology, insuranceExpiration);
    }
}

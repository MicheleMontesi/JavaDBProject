package model;

import utilities.FillUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record TherapyDrug(int therapyId, int consumptionId, Date consumptionDate, int quantity,
                          String administratorFiscalCode, int drugId) implements Entity {

    public TherapyDrug(int therapyId, int consumptionId, Date consumptionDate, int quantity,
                       String administratorFiscalCode, int drugId) {
        this.therapyId = therapyId;
        this.consumptionId = consumptionId;
        this.consumptionDate = Objects.requireNonNull(consumptionDate);
        this.quantity = quantity;
        this.administratorFiscalCode = Objects.requireNonNull(administratorFiscalCode);
        this.drugId = drugId;
    }

    @Override
    public String toString() {
        return "TherapyDrug{" +
                "therapyId=" + therapyId +
                ", consumptionId=" + consumptionId +
                ", consumptionDate=" + consumptionDate +
                ", quantity=" + quantity +
                ", administratorFiscalCode='" + administratorFiscalCode + '\'' +
                ", drugId=" + drugId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TherapyDrug that = (TherapyDrug) o;
        return therapyId == that.therapyId
                && consumptionId == that.consumptionId
                && quantity == that.quantity
                && drugId == that.drugId
                && consumptionDate.equals(that.consumptionDate)
                && administratorFiscalCode.equals(that.administratorFiscalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(therapyId, consumptionId, consumptionDate, quantity, administratorFiscalCode, drugId);
    }

    @Override
    public List<String> getId() {
        return List.of(Objects.toString(therapyId), Objects.toString(consumptionId),
                administratorFiscalCode, Objects.toString(drugId));
    }
}

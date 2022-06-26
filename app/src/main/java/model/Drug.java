package model;

import java.util.Date;
import java.util.Objects;

public record Drug(int drugId, String name, String pharmaCompany, Date purchaseDate, Date expirationDate, int quantity) {

    public Drug(int drugId, String name, String pharmaCompany, Date purchaseDate, Date expirationDate, int quantity) {
        this.drugId = drugId;
        this.name = Objects.requireNonNull(name);
        this.pharmaCompany = Objects.requireNonNull(pharmaCompany);
        this.purchaseDate = Objects.requireNonNull(purchaseDate);
        this.expirationDate = Objects.requireNonNull(expirationDate);
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugId=" + drugId +
                ", name='" + name + '\'' +
                ", pharmaCompany='" + pharmaCompany + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", expirationDate=" + expirationDate +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return drugId == drug.drugId
                && quantity == drug.quantity
                && name.equals(drug.name)
                && pharmaCompany.equals(drug.pharmaCompany)
                && purchaseDate.equals(drug.purchaseDate)
                && expirationDate.equals(drug.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugId, name, pharmaCompany, purchaseDate, expirationDate, quantity);
    }
}

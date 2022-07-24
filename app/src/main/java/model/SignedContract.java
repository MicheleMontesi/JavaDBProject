package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record SignedContract(String fiscalCode, Date stipulationDate, Date endDate, String contractName) implements Entity {

    public SignedContract(String fiscalCode, Date stipulationDate, Date endDate, String contractName) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.stipulationDate = Objects.requireNonNull(stipulationDate);
        this.endDate = Objects.requireNonNull(endDate);
        this.contractName = Objects.requireNonNull(contractName);
    }

    @Override
    public String toString() {
        return "SignedContract{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", stipulationDate=" + stipulationDate +
                ", endDate=" + endDate +
                ", contractName='" + contractName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignedContract that = (SignedContract) o;
        return fiscalCode.equals(that.fiscalCode)
                && stipulationDate.equals(that.stipulationDate)
                && endDate.equals(that.endDate)
                && contractName.equals(that.contractName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, stipulationDate, endDate, contractName);
    }

    @Override
    public List<String> getId() {
        return List.of(fiscalCode, stipulationDate.toString(), contractName);
    }
}

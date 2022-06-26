package model;

import java.util.Objects;

public record ContractType(String name, int contractualHours) {

    public ContractType(String name, int contractualHours) {
        this.name = Objects.requireNonNull(name);
        this.contractualHours = contractualHours;
    }

    @Override
    public String toString() {
        return "ContractType{" +
                "name='" + name + '\'' +
                ", contractualHours=" + contractualHours +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractType that = (ContractType) o;
        return contractualHours == that.contractualHours
                && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contractualHours);
    }
}

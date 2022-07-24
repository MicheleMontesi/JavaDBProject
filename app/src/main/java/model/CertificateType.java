package model;

import java.util.List;
import java.util.Objects;

public record CertificateType(String name, int ecmCredits) implements Entity {

    public CertificateType(String name, int ecmCredits) {
        this.name = Objects.requireNonNull(name);
        this.ecmCredits = ecmCredits;
    }

    @Override
    public String toString() {
        return "CertificateType{" +
                "name='" + name + '\'' +
                ", ecmCredits=" + ecmCredits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateType that = (CertificateType) o;
        return ecmCredits == that.ecmCredits && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ecmCredits);
    }

    @Override
    public List<String> getId() {
        return List.of(name);
    }
}

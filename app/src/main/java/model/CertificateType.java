package model;

import java.util.Objects;

public record CertificateType(String name) {

    public CertificateType(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return "CertificateType{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateType that = (CertificateType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

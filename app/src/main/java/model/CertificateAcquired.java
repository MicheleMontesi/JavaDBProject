package model;

import java.util.Date;
import java.util.Objects;

public record CertificateAcquired(String fiscalCode, Date acquisitionDate, String certificateName) {

    public CertificateAcquired(String fiscalCode, Date acquisitionDate, String certificateName) {
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.acquisitionDate = Objects.requireNonNull(acquisitionDate);
        this.certificateName = Objects.requireNonNull(certificateName);
    }

    @Override
    public String toString() {
        return "CertificateAcquired{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", acquisitionDate=" + acquisitionDate +
                ", certificateName='" + certificateName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateAcquired that = (CertificateAcquired) o;
        return fiscalCode.equals(that.fiscalCode)
                && acquisitionDate.equals(that.acquisitionDate)
                && certificateName.equals(that.certificateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiscalCode, acquisitionDate, certificateName);
    }
}

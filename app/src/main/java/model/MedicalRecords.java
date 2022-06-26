package model;

import java.util.Objects;

public record MedicalRecords(int medicalRecordId, String fiscalCode, String anamnesis, String diagnosis,
                             String rehabProject) {

    public MedicalRecords(int medicalRecordId, String fiscalCode, String anamnesis,
                          String diagnosis, String rehabProject) {
        this.medicalRecordId = medicalRecordId;
        this.fiscalCode = Objects.requireNonNull(fiscalCode);
        this.anamnesis = Objects.requireNonNull(anamnesis);
        this.diagnosis = Objects.requireNonNull(diagnosis);
        this.rehabProject = Objects.requireNonNull(rehabProject);
    }

    @Override
    public String toString() {
        return "MedicalRecords{" +
                "medicalRecordId=" + medicalRecordId +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", anamnesis='" + anamnesis + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", rehabProject='" + rehabProject + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecords that = (MedicalRecords) o;
        return medicalRecordId == that.medicalRecordId
                && fiscalCode.equals(that.fiscalCode)
                && anamnesis.equals(that.anamnesis)
                && diagnosis.equals(that.diagnosis)
                && rehabProject.equals(that.rehabProject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalRecordId, fiscalCode, anamnesis, diagnosis, rehabProject);
    }
}

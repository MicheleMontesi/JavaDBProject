package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.MedicalRecords;

public class CreateMedicalRecordsView {

    public static void create(final TableView<MedicalRecords> table,
                       final TableColumn<MedicalRecords, Integer> medicalIdColumn,
                       final TableColumn<MedicalRecords, String> fiscalCodeColumn,
                       final TableColumn<MedicalRecords, String> anamnesisColumn,
                       final TableColumn<MedicalRecords, String> diagnosisColumn,
                       final TableColumn<MedicalRecords, String> rehabColumn,
                       final ObservableList<MedicalRecords> list) {

        medicalIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().medicalRecordId()));
        fiscalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        anamnesisColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().anamnesis()));
        diagnosisColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().diagnosis()));
        rehabColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().rehabProject()));

        table.setItems(list);
    }
}

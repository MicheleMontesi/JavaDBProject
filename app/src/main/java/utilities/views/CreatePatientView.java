package utilities.views;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Patient;

import java.util.Date;

public class CreatePatientView {

    public static void create(final TableView<Patient> table,
                       final TableColumn<Patient, String> idColumn,
                       final TableColumn<Patient, String> nameColumn,
                       final TableColumn<Patient, String> surnameColumn,
                       final TableColumn<Patient, Date> birthColumn,
                       final TableColumn<Patient, String> residenceColumn,
                       final TableColumn<Patient, String> genderColumn,
                       final TableColumn<Patient, Integer> patientIdColumn,
                       final TableColumn<Patient, Boolean> privacyColumn,
                       final TableColumn<Patient, Boolean> consentColumn,
                       final TableColumn<Patient, Boolean> rulesColumn,
                       final ObservableList<Patient> list) {

        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        surnameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().surname()));
        birthColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().birthday()));
        residenceColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().residence()));
        genderColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().gender()));
        patientIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().patientId()));
        privacyColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().privacyDocumentation()));
        consentColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().consentTreatment()));
        rulesColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().acceptRules()));

        table.setItems(list);
    }
}

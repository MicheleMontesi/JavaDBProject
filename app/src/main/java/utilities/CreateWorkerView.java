package utilities;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Worker;

import java.util.Date;

public class CreateWorkerView {

    public static void create(final TableView<Worker> table,
                       final TableColumn<Worker, String> idColumn,
                       final TableColumn<Worker, String>nameColumn,
                       final TableColumn<Worker, String>surnameColumn,
                       final TableColumn<Worker, Date> birthColumn,
                       final TableColumn<Worker, String>residenceColumn,
                       final TableColumn<Worker, String>genderColumn,
                       final TableColumn<Worker, Integer> workerCodeColumn,
                       final TableColumn<Worker, Boolean> suitabilityColumn,
                       final TableColumn<Worker, Boolean>partnerColumn,
                       final TableColumn<Worker, String>edQualColumn,
                       final TableColumn<Worker, Integer>ECMColumn,
                       final ObservableList<Worker> list) {

        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        surnameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().surname()));
        birthColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(null, "", cellData.getValue().birthDay()));
        residenceColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().residence()));
        genderColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().gender()));
        workerCodeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().workerId()));
        edQualColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().edQualification()));
        suitabilityColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().suitability()));
        partnerColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().partner()));
        ECMColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().ECMCredits()));

        table.setItems(list);
    }
}

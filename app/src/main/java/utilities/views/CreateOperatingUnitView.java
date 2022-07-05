package utilities.views;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.OperatingUnit;

public class CreateOperatingUnitView {

    public static void create(final TableView<OperatingUnit> table,
                       final TableColumn<OperatingUnit, String> idColumn,
                       final TableColumn<OperatingUnit, String> typeColumn,
                       final TableColumn<OperatingUnit, String> nameColumn,
                       final TableColumn<OperatingUnit, String> locationColumn,
                       final TableColumn<OperatingUnit, Integer> bedsColumn,
                       final TableColumn<OperatingUnit, Integer> patientsColumn,
                       final TableColumn<OperatingUnit, Boolean> authColumn,
                       final TableColumn<OperatingUnit, Boolean> accreditColumn,
                       final ObservableList<OperatingUnit> list) {

        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().unitId()));
        typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().type()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        locationColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().location()));
        bedsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().bedsNumber()));
        patientsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().patientsNumber()));
        authColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().operatingAuth()));
        accreditColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().accreditation()));

        table.setItems(list);
    }
}

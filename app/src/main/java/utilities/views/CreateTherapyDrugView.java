package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TherapyDrug;

import java.util.Date;

public class CreateTherapyDrugView {

    public static void create(final TableView<TherapyDrug> table,
                       final TableColumn<TherapyDrug, Integer> therapyColumn,
                       final TableColumn<TherapyDrug, Integer> consumptionColumn,
                       final TableColumn<TherapyDrug, Date> dateColumn,
                       final TableColumn<TherapyDrug, Integer> quantityColumn,
                       final TableColumn<TherapyDrug, String> fiscalCodeColumn,
                       final TableColumn<TherapyDrug, Integer> drugIdColumn,
                       final ObservableList<TherapyDrug> list) {

        therapyColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().therapyId()));
        consumptionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().consumptionId()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().consumptionDate()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().quantity()));
        fiscalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().administratorFiscalCode()));
        drugIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().drugId()));

        table.setItems(list);
    }
}

package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Drug;

import java.util.Date;

public class CreateDrugView {

    public static void create(final TableView<Drug> table,
                       final TableColumn<Drug, Integer> idColumn,
                       final TableColumn<Drug, String> nameColumn,
                       final TableColumn<Drug, String> pharmaHouseColumn,
                       final TableColumn<Drug, Date> purchaseColumn,
                       final TableColumn<Drug, Date> expirationColumn,
                       final TableColumn<Drug, Integer> quantityColumn,
                       final ObservableList<Drug> list) {

        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().drugId()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        pharmaHouseColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().pharmaCompany()));
        purchaseColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().purchaseDate()));
        expirationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().expirationDate()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().quantity()));

        table.setItems(list);
    }
}

package utilities.views;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CapitalGood;

import java.util.Date;

public class CreateCapitalGoodsView {

    public static void create(final TableView<CapitalGood> table,
                       final TableColumn<CapitalGood, String> unitIdColumn,
                       final TableColumn<CapitalGood, Integer> goodIdColumn,
                       final TableColumn<CapitalGood, Date> purchaseColumn,
                       final TableColumn<CapitalGood, Date> maintenanceColumn,
                       final TableColumn<CapitalGood, Boolean> vehicleColumn,
                       final TableColumn<CapitalGood, String> toolColumn,
                       final TableColumn<CapitalGood, String> plateColumn,
                       final TableColumn<CapitalGood, String> typeColumn,
                       final TableColumn<CapitalGood, Date> expirationColumn,
                       final ObservableList<CapitalGood> list) {

        unitIdColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().unitId()));
        goodIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().goodId()));
        purchaseColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().purchaseDate()));
        maintenanceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().nextMaintenance()));
        vehicleColumn.setCellValueFactory(cellData -> new ReadOnlyBooleanWrapper(cellData.getValue().vehicle()));
        toolColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().toolName().orElse(null)));
        plateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().licencePlate().orElse(null)));
        typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().typology().orElse(null)));
        expirationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().insuranceExpiration().orElse(null)));

        table.setItems(list);
    }
}

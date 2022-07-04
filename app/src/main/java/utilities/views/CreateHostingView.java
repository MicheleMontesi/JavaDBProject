package utilities.views;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Hosting;
import model.Worker;

import java.util.Date;

public class CreateHostingView {

    public static void create(final TableView<Hosting> table,
                       final TableColumn<Hosting, String> fiscalCodeColumn,
                       final TableColumn<Hosting, Date> signedColumn,
                       final TableColumn<Hosting, Date> endColumn,
                       final TableColumn<Hosting, String> unitIdColumn,
                       final ObservableList<Hosting> list) {

        fiscalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        signedColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().beginDate()));
        endColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().endDate().orElse(null)));
        unitIdColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().unitId()));

        table.setItems(list);
    }
}

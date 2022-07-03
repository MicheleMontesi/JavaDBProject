package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.TakeTherapy;

public class CreateTakeTherapyView {

    public static void create(final TableView<TakeTherapy> table,
                       final TableColumn<TakeTherapy, String> fiscalCodeColumn,
                       final TableColumn<TakeTherapy, Integer> thearapyColummn,
                       final ObservableList<TakeTherapy> list) {

        fiscalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        thearapyColummn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().therapyId()));

        table.setItems(list);
    }
}

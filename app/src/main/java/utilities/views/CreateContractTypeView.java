package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ContractType;
import model.TakeTherapy;

public class CreateContractTypeView {

    public static void create(final TableView<ContractType> table,
                       final TableColumn<ContractType, String> nameColumn,
                       final TableColumn<ContractType, Integer> hoursColumn,
                       final ObservableList<ContractType> list) {

        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        hoursColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().contractualHours()));

        table.setItems(list);
    }
}

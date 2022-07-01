package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateAcquired;
import model.SignedContract;

import java.util.Date;

public class CreateSignedContractView {

    public static void create(final TableView<SignedContract> table,
                       final TableColumn<SignedContract, Date> beginColumn,
                       final TableColumn<SignedContract, Date> endColumn,
                       final TableColumn<SignedContract, String> nameColumn,
                       final ObservableList<SignedContract> list) {

        beginColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stipulationDate()));
        endColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().endDate()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().contractName()));

        table.setItems(list);
    }
}

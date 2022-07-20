package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateType;

public class CreateCertificateTypeView {

    public static void create(final TableView<CertificateType> table,
                       final TableColumn<CertificateType, String> nameColumn,
                       final TableColumn<CertificateType, Integer> ecmColumn,
                       final ObservableList<CertificateType> list) {

        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));
        ecmColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().ecmCredits()));

        table.setItems(list);
    }
}

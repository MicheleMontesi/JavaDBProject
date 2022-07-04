package utilities.views;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateType;

public class CreateCertificateTypeView {

    public static void create(final TableView<CertificateType> table,
                       final TableColumn<CertificateType, String> nameColumn,
                       final ObservableList<CertificateType> list) {

        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().name()));

        table.setItems(list);
    }
}

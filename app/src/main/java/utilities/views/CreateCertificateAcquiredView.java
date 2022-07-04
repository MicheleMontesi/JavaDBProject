package utilities.views;

import db.tables.CertificateAcquiredTables;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CertificateAcquired;

import java.util.Date;

public class CreateCertificateAcquiredView {

    public static void create(final TableView<CertificateAcquired> table,
                       final TableColumn<CertificateAcquired, String> fiscalCodeColumn,
                       final TableColumn<CertificateAcquired, String> nameColumn,
                       final TableColumn<CertificateAcquired, Date> dateColumn,
                       final ObservableList<CertificateAcquired> list) {

        fiscalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().fiscalCode()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().certificateName()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().acquisitionDate()));

        table.setItems(list);
    }
}

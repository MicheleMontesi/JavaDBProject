package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Therapy;

import java.util.Date;

public class CreateTherapyView {

    public static void create(final TableView<Therapy> table,
                       final TableColumn<Therapy, Integer> therapyColumn,
                       final TableColumn<Therapy, Date> dateColumn,
                       final ObservableList<Therapy> list) {

        therapyColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().therapyId()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().creationDate()));

        table.setItems(list);
    }
}

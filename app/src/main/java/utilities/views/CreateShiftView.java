package utilities.views;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Shift;

import java.time.LocalTime;

public class CreateShiftView {

    public static void create(final TableView<Shift> table,
                              final TableColumn<Shift, String> dayColumn,
                              final TableColumn<Shift, LocalTime> beginColumn,
                              final TableColumn<Shift, LocalTime> endColumn,
                              final TableColumn<Shift, String> unitIdColumn,
                              final ObservableList<Shift> list) {

        dayColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().dayOfTheWeek()));
        beginColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().beginTime()));
        endColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().endTime()));
        unitIdColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().unitId()));

        table.setItems(list);
    }
}

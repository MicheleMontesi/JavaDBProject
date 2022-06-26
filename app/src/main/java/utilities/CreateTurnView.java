package utilities;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Turn;

import java.time.LocalTime;

public class CreateTurnView {

    public static void create(final TableView<Turn> table,
                              final TableColumn<Turn, String> dayColumn,
                              final TableColumn<Turn, LocalTime> beginColumn,
                              final TableColumn<Turn, LocalTime> endColumn,
                              final ObservableList<Turn> list) {

        dayColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().dayOfTheWeek()));
        beginColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().beginTime()));
        endColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().endTime()));

        table.setItems(list);
    }
}

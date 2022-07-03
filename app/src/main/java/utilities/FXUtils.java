package utilities;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FXUtils {

    public static void disableOnWrite(Button button, TextField field) {
        BooleanBinding idFieldValid = Bindings.createBooleanBinding(() -> field.getText().isEmpty(), field.textProperty());

        button.disableProperty().bind(idFieldValid);
    }
}

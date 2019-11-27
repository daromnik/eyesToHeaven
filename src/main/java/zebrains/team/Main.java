package zebrains.team;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        DetectEye detectEye = new DetectEye();
        Form form = new Form(stage, detectEye);
        form.createForm();
    }
}

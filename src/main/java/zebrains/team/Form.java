package zebrains.team;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class Form {

    private DetectEye detectEye;
    private Stage stage;

    public Form(Stage stage, DetectEye detectEye) {
        this.stage = stage;
        this.detectEye = detectEye;
    }

    public void createForm() {

        ByteArrayInputStream byteArrayInputStream = detectEye.getOriginImageStream();
        Image imageElem = new Image(byteArrayInputStream);

        final ImageView frameView =  new ImageView(imageElem);

        Group group = new Group(frameView);

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("Глаза в небо");
        stage.setWidth(frameView.getImage().getWidth());
        stage.setHeight(frameView.getImage().getHeight());
        stage.show();
    }

}

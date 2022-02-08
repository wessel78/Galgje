import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        GuiHandler guiHandler = new GuiHandler();
        Scene scene = new Scene(guiHandler.getPane());

        primaryStage.setTitle("Hangman!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
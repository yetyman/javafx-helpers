package org.yetyman.controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.ResourceBundle;

public class FXMLHelper {

    public static <T> T loadFXML(String fxml) {
        return FXMLHelper.loadFXML(fxml, null);
    }
    public static <T> T loadFXML(String fxml, T controller) {
        return FXMLHelper.loadFXML(fxml, controller, null);
    }

    /**
     * Allow FXML to be loaded to an already initialized class.
     * This allows for the duality of java and FXML in the same conceptual class without
     * unneeded separations or order of initialization concerns.
     * @param fxml the name of the fxml file to load
     * @param controller an @FXML annotated instance of the controller class to have FXML fields loaded into
     * @param bundle the resource bundle to use for localization
     * @return returns controller parameter if non-null and a new instance of the loaded FXML controller class if not
     * @param <T> The type of the FXML's controller class
     */
    public static <T> T loadFXML(String fxml, T controller, ResourceBundle bundle) {
        Class<?> clazz = null;
        if(controller != null)
            clazz = controller.getClass();
        else
            clazz = FXMLHelper.class;

        FXMLLoader loader = new FXMLLoader(clazz.getResource(fxml), bundle);
        if (controller != null) {
            loader.setRoot(controller);
//            loader.setClassLoader(controller.getClass().getClassLoader());
            loader.setControllerFactory(param -> controller);
        }
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

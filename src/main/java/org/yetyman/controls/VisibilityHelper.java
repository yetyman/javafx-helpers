package org.yetyman.controls;

import javafx.scene.Node;

import java.util.Collection;

public class VisibilityHelper {
    public static void removeWhileInvisible(Node n) {
        n.managedProperty().bind(n.visibleProperty());
    }
    public static void removeWhileInvisible(Node... n) {
        for (Node node : n) {
            removeWhileInvisible(node);
        }
    }
    public static void removeWhileInvisible(Collection<Node> n) {
        for (Node node : n) {
            removeWhileInvisible(node);
        }
    }
}

package org.yetyman.controls;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GridHelper {
    public static void size(GridPane gp, int columns, int rows) {
        for (int i = 0; i < columns; i++) {
            gp.getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE,USE_COMPUTED_SIZE,USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.CENTER, true));
        }
        for (int i = 0; i < rows; i++) {
            gp.getRowConstraints().add(new RowConstraints(USE_COMPUTED_SIZE,USE_COMPUTED_SIZE,USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        }
    }

    public static void layout(GridPane gp, Node... items) {
        if(items == null || items.length == 0) return;
        if(items.length != gp.getColumnCount()*gp.getRowCount()) throw new RuntimeException("items count mismatch");

        Map<Node, Span> map = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            Node item = items[i];
            int x = i%gp.getColumnCount();
            int y = i/gp.getColumnCount();

            Span s = map.computeIfAbsent(item, f->new Span(x,y));
            s.include(x,y);
        }

        for (Node node : map.keySet()) {
            if(node != null) {
                Span s = map.get(node);
                GridPane.setConstraints(node, s.x0, s.y0, s.x1 - s.x0 + 1, s.y1 - s.y0 + 1);
                if (!gp.getChildren().contains(node))
                    gp.getChildren().add(node);
            }
        }
    }

    private static class Span {
        int x0, y0, x1, y1;

        public Span(int x0, int y0) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x0;
            this.y1 = y0;
        }
        
        public void include(int x, int y) {
            if(x < x0) x0 = x;
            if(y < y0) y0 = y;
            if(x > x0) x1 = x;
            if(y > y0) y1 = y;
        }
    }
}

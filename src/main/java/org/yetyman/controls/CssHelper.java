package org.yetyman.controls;

import javafx.beans.property.ObjectProperty;
import javafx.css.*;
import javafx.css.converter.InsetsConverter;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CssHelper {
    static final Map<String, CssMetaData<?,?>> map = new WeakHashMap<>();

    public static <Q, T> StyleableObjectProperty<T> createCssProperty(Region ref, String cssName, StyleConverter<Q, T> styleConverter, T defaultValue) {
        AtomicReference<CssMetaData<Region, T>> cssRef = new AtomicReference<>(null);

        if(map.containsKey(cssName)) {
            //noinspection unchecked
            cssRef.set((CssMetaData<Region, T>)map.get(cssName));
        } else {
            CssMetaData<Region, T> css = new CssMetaData<>(
                    cssName, styleConverter, defaultValue) {
                @Override
                public boolean isSettable(Region styleable) {
                    return true;
                }

                @Override
                public StyleableProperty<T> getStyleableProperty(Region styleable) {
                    Map< Object, Object> map = styleable.getProperties();
                    if(map.containsKey(cssName) && map.get(cssName) instanceof StyleableObjectProperty<?> prop)
                        //noinspection unchecked
                        return (StyleableProperty<T>) prop;
                    else
                        return null;
                }
            };
            map.put(cssName, css);
            cssRef.set(css);
        }

        StyleableObjectProperty<T> prop = new SimpleStyleableObjectProperty<T>(cssRef.get(), null, cssName, defaultValue);

        ref.getProperties().put(cssName, prop);

        return prop;
    }
}

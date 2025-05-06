package org.yetyman.controls;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.scene.Node;

import java.util.Map;
import java.util.WeakHashMap;


/**
 * This class provides shorthand convenience methods for using custom PseudoClass states on a Node
 * <br/>
 * While the hope is to fill in some obvious gaps from HTML/CSS usage, I have not found a way to process functional
 * PseudoClasses (such as :nth-child(2n)).
 * <br/>
 * Functional PseudoClass states with constant values :nth-child(2) can be used by creating them on every element.
 * Hacky, but occasionally useful
 */
public class PseudoClassHelper {
    /**
     * This is simply shorthand for recommended PseudoClass usage wrapped in a boolean property.
     * It should be cached on whatever you used it for.
     *  On false the pseudo class state will be gone
     *  On true the pseudo class state will be applied
     **/
    public static BooleanProperty getPseudoClassProperty(Node n, String name, boolean defaultValue) {
        PseudoClass pseudo = PseudoClass.getPseudoClass(name);

        return new BooleanPropertyBase(false) {

            {
                set(defaultValue);
                invalidated();
            }

            @Override protected void invalidated() {
                n.pseudoClassStateChanged(pseudo, get());
            }

            @Override public Object getBean() {
                return n;
            }

            @Override public String getName() {
                return pseudo.getPseudoClassName();
            }
        };
    }

    /**<pre>
     * This is simply shorthand for recommended PseudoClass usage wrapped in a double property with a format string.
     * It should be cached on whatever you used it for.
     *
     * This adds the ability to represent a PseudoClass state with variable values.
     * The parallel in HTML CSS would be :nth-child(2), though we have total freedom over the format here and this
     * property will format your double any way you specify with String.format() rules.
     *
     * This property cannot be null, but the same effect can be achieved with nullability using {@link #getPseudoClassObjectProperty(Node, String, Object)}
     * </pre>
     * @param baseName a String.format() template for the PseudoClass name. It will be passed the value of the property.
     */
    public static DoubleProperty  getPseudoClassDoubleProperty(Node n, String baseName, double defaultValue) {
        return new DoublePropertyBase() {
            final Map<Double, PseudoClass> map = new WeakHashMap<>();
            PseudoClass previous = null;

            {
                set(defaultValue);
                invalidated();
            }

            private PseudoClass findPseudoClass(Double f) {
                return PseudoClass.getPseudoClass(String.format(baseName, get()));
            }

            @Override protected void invalidated() {
                if(previous != null)
                    n.pseudoClassStateChanged(previous, false);

                n.pseudoClassStateChanged(previous = map.computeIfAbsent(get(), this::findPseudoClass), true);
            }

            @Override public Object getBean() {
                return n;
            }

            @Override public String getName() {
                return baseName;
            }
        };
    }

    /**<pre>
     * This is simply shorthand for recommended PseudoClass usage wrapped in an integer property with a format string.
     * It should be cached on whatever you used it for.
     *
     * This adds the ability to represent a PseudoClass state with variable values.
     * The parallel in HTML CSS would be :nth-child(2), though we have total freedom over the format here and this
     * property will format your double any way you specify with String.format() rules.
     *
     * This property cannot be null, but the same effect can be achieved with nullability using {@link #getPseudoClassObjectProperty(Node, String, Object)}
     * </pre>
     * @param baseName a String.format() template for the PseudoClass name. It will be passed the value of the property.
     */
    public static IntegerProperty getPseudoClassIntegerProperty(Node n, String baseName, int defaultValue) {
        return new IntegerPropertyBase() {
            final Map<Integer, PseudoClass> map = new WeakHashMap<>();
            PseudoClass previous = null;

            {
                set(defaultValue);
                invalidated();
            }

            private PseudoClass findPseudoClass(Integer f) {
                return PseudoClass.getPseudoClass(String.format(baseName, f));
            }

            @Override protected void invalidated() {
                if(previous != null)
                    n.pseudoClassStateChanged(previous, false);

                n.pseudoClassStateChanged(previous = map.computeIfAbsent(get(), this::findPseudoClass), true);
            }

            @Override public Object getBean() {
                return n;
            }

            @Override public String getName() {
                return baseName;
            }
        };
    }

    /**<pre>
     * This is simply shorthand for recommended PseudoClass usage wrapped in a String property with a format string.
     * It should be cached on whatever you used it for.
     *
     * This adds the ability to represent a PseudoClass state with variable values.
     * The parallel in HTML CSS would be :nth-child(2), though we have total freedom over the format here and this
     * property will format your String any way you specify with String.format() rules.
     * </pre>
     * @param baseName a String.format() template for the PseudoClass name. It will be passed the value of the property.
     */
    public static StringProperty  getPseudoClassStringProperty(Node n, String baseName, String defaultValue) {
        return new StringPropertyBase() {
            final Map<String, PseudoClass> map = new WeakHashMap<>();
            PseudoClass previous = null;

            {
                set(defaultValue);
                invalidated();
            }

            private PseudoClass findPseudoClass(String f) {
                return PseudoClass.getPseudoClass(String.format(baseName, get()));
            }

            @Override protected void invalidated() {
                if(previous != null)
                    n.pseudoClassStateChanged(previous, false);

                if (get() != null)
                    n.pseudoClassStateChanged(previous = map.computeIfAbsent(get(), this::findPseudoClass), true);
            }

            @Override public Object getBean() {
                return n;
            }

            @Override public String getName() {
                return baseName;
            }
        };
    }

    /**<pre>
     * This is simply shorthand for recommended PseudoClass usage wrapped in an object property with a format string.
     * It should be cached on whatever you used it for.
     *
     * This adds the ability to represent a PseudoClass state with variable values.
     * The parallel in HTML CSS would be :nth-child(2), though we have total freedom over the format here and this
     * property will format your double any way you specify with String.format() rules.
     * </pre>
     * @param baseName a String.format() template for the PseudoClass name. It will be passed the value of the property.
     */
    public static <T> ObjectProperty<T> getPseudoClassObjectProperty(Node n, String baseName, T defaultValue) {
        return new ObjectPropertyBase<T>() {
            final Map<String, PseudoClass> map = new WeakHashMap<>();
            PseudoClass previous = null;

            {
                set(defaultValue);
                invalidated();
            }

            private PseudoClass findPseudoClass(String name) {
                return PseudoClass.getPseudoClass(name);
            }

            @Override protected void invalidated() {
                if(previous != null)
                    n.pseudoClassStateChanged(previous, false);

                if(get() != null)
                    n.pseudoClassStateChanged(previous = map.computeIfAbsent(String.format(baseName, get()), this::findPseudoClass), true);
            }

            @Override public Object getBean() {
                return n;
            }

            @Override public String getName() {
                return baseName;
            }
        };
    }
}

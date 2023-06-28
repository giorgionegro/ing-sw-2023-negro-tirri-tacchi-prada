package view.GUI.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 * This class implements {@link LayoutManager} allowing to display a single {@link Component} with a fixed width/height ratio
 */
public class AspectRatioLayout implements LayoutManager {

    /**
     * The ratio the displayed component must have
     */
    private final float ratio;

    /**
     * The number of component this LayoutManager is managing
     */
    private int compCount = 0;

    /**
     * Construct an instance of this class initializing ratio with the given value
     * @param ratio the ratio value that have to be respected
     */
    public AspectRatioLayout(float ratio) {
        this.ratio = ratio;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It allows to only one component to be manages, otherwise signals layout as invalid
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        /* Signals the layout is invalid if trying to add more than one component */
        assert(this.compCount == 0) : "AspectRatioLayout can only contain one component";
        /* Incrementing the number of component managed */
        this.compCount++;
    }

    /**
     * {@inheritDoc}
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        /* Decrement the number of component managed */
        this.compCount--;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the number of component in parent container is equal to one then sets bounds of this component in order to respect the fixed ratio
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {
        /* Only if the number of components is equal to one */
        if(parent.getComponentCount() == 1) {
            /* Get max width and height the component can occupy ... */
            Insets insets = parent.getInsets();
            /* ... as the space left from the difference between the size of the parent container and its insets */
            int maxWidth = parent.getWidth()
                    - (insets.left + insets.right);
            int maxHeight = parent.getHeight()
                    - (insets.top + insets.bottom);

            /* Get height and width of the component following ratio constraint */
            float w = Math.min(maxHeight * this.ratio, maxWidth);
            float h = w / this.ratio;

            Rectangle bounds = new Rectangle();
            /* Sets bounds height and width as we just found */
            bounds.width = (int) w;
            bounds.height = (int) h;
            /* And calculate start points placing the component in the center of the parent component*/
            bounds.x = (maxWidth - bounds.width)/2;
            bounds.y = (maxHeight - bounds.height)/2;

            /* Set bounds to the component */
            parent.getComponent(0).setBounds(bounds);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result is equal to parent insets if parent does not contain any components, otherwise the result is equal
     * to the minimum size of the contained component (the one this manager is managing)
     * @param parent the component to be laid out
     * @return {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        /* Calculate minimum size on parent insets */
        Insets i = parent.getInsets();

        Dimension min = new Dimension(0,0);
        min.width += i.left + i.right;
        min.height += i.top + i.bottom;

        if(parent.getComponentCount() == 1) {
            /* If parent contains only one component that the minimum size of the layout is the minimum size of that component */
            Dimension componentDimension = parent.getComponent(0).getMinimumSize();
            min.width += componentDimension.width;
            min.height += componentDimension.height;
        }

        return min;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result is equal to parent insets if parent does not contain any components, otherwise the result is equal
     * to the preferred size of the contained component (the one this manager is managing)
     * @param parent the container to be laid out
     * @return {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        /* Calculate preferred size on parent insets */
        Insets i = parent.getInsets();

        Dimension preferred = new Dimension(0,0);
        preferred.width += i.left + i.right;
        preferred.height += i.top + i.bottom;

        if(parent.getComponentCount() == 1) {
            /* If parent contains only one component that the preferred size of the layout is the preferred size of that component */
            Dimension componentDimension = parent.getComponent(0).getPreferredSize();
            preferred.width += componentDimension.width;
            preferred.height += componentDimension.height;
        }

        return preferred;
    }
}


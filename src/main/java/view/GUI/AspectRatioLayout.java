package view.GUI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

public class AspectRatioLayout implements LayoutManager {

    private final float ratio;
    private int compCount = 0;

    public AspectRatioLayout(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        compCount++;
        assert(compCount == 0) : "AspectRatioLayout can only contain one component";
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        compCount--;
    }

    @Override
    public void layoutContainer(Container parent) {
        if(parent.getComponentCount() == 1) {
            Insets insets = parent.getInsets();
            int maxWidth = parent.getWidth()
                    - (insets.left + insets.right);
            int maxHeight = parent.getHeight()
                    - (insets.top + insets.bottom);

            float w = Math.min(maxHeight * ratio, maxWidth);
            float h = w / ratio;

            Component c = parent.getComponent(0);

            Rectangle bounds = new Rectangle();
            bounds.width = (int) w;
            bounds.height = (int) h;
            bounds.x = (maxWidth - bounds.width)/2;
            bounds.y = (maxHeight - bounds.height)/2;

            c.setBounds(bounds);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Insets i = parent.getInsets();

        Dimension min = new Dimension(0,0);
        min.width += i.left + i.right;
        min.height += i.top + i.bottom;

        if(parent.getComponentCount() == 1) {
            Dimension componentDimension = parent.getComponent(0).getMinimumSize();
            min.width += componentDimension.width;
            min.height += componentDimension.height;
        }

        return min;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {

        Insets i = parent.getInsets();

        Dimension preferred = new Dimension(0,0);
        preferred.width += i.left + i.right;
        preferred.height += i.top + i.bottom;

        if(parent.getComponentCount() == 1) {
            Dimension componentDimension = parent.getComponent(0).getPreferredSize();
            preferred.width += componentDimension.width;
            preferred.height += componentDimension.height;
        }

        return preferred;
    }
}


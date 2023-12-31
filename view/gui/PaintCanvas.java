package view.gui;

import model.persistence.ApplicationState;
import model.strategypattern.PaintShape;
import model.ShapeList;
import model.ColorUtil;
import model.compositePattern.ShapeGroup;

import javax.swing.JComponent;
import java.awt.*;

public class PaintCanvas extends JComponent {

    private final ShapeList shapeList;

    public PaintCanvas(ShapeList shapeList) {
        this.shapeList = shapeList;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        for (PaintShape shape : shapeList.getAllShapes()){
            System.out.println(shape.getClass().getSimpleName());
            // if shape is a group, draw all shapes in the group
            if (shape.getClass().getSimpleName().equals("ShapeGroup")) {
                ShapeGroup shapeGroup = (ShapeGroup) shape;
                drawGroupRecursive(graphics2d, shapeGroup);
            } else {
                drawGraphic(graphics2d, shape);
            }

            if (shape.isSelected()) {
                Graphics2D stroked2D = (Graphics2D) g;
                stroked2D.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
                graphics2d.setColor(Color.BLACK);
                graphics2d.draw(shape.getShapeBorderWhenSelected());
            }
        }
    }


    private void drawGroupRecursive(Graphics2D graphics2d, ShapeGroup shapeGroup) {
        for (PaintShape shape : shapeGroup.getGroupedShaped()) {
            if (shape.getClass().getSimpleName().equals("ShapeGroup")) {
                ShapeGroup shapeGroup1 = (ShapeGroup) shape;
                drawGroupRecursive(graphics2d, shapeGroup1);
            } else {
                drawGraphic(graphics2d, shape);
            }
        }
    }



    private void drawGraphic(Graphics2D graphics2d, PaintShape shape) {
        Color primaryColor = ColorUtil.colorFrom(shape.getPrimaryColor());
        Color secondaryColor = ColorUtil.colorFrom(shape.getSecondaryColor());

        switch (shape.getShadingType()) {

            case FILLED_IN:
                graphics2d.setColor(primaryColor);
                graphics2d.fill(shape.getShape());
                break;
            case OUTLINE:
                graphics2d.setColor(primaryColor);
                graphics2d.setStroke(new BasicStroke(5));
                graphics2d.draw(shape.getShape());
                break;
            case OUTLINE_AND_FILLED_IN:
                graphics2d.setColor(primaryColor);
                graphics2d.fill(shape.getShape());
                graphics2d.setColor(secondaryColor);
                graphics2d.setStroke(new BasicStroke(5));
                graphics2d.draw(shape.getShape());
                break;
        }
    }

    public void setAppState(ApplicationState appState) {

    }
}

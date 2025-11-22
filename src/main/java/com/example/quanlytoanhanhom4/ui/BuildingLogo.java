package com.example.quanlytoanhanhom4.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Logo component cho phần mềm quản lý tòa nhà
 * Thiết kế đen trắng với ba tòa nhà cách điệu và nền tảng cong
 */
public class BuildingLogo extends Group {

    private static final double LOGO_WIDTH = 60;
    private static final double LOGO_HEIGHT = 60;

    public BuildingLogo() {
        this(LOGO_WIDTH, LOGO_HEIGHT);
    }

    public BuildingLogo(double width, double height) {
        super();
        createLogo(width, height);
    }

    private void createLogo(double width, double height) {
        // Màu đen trắng
        Color black = Color.BLACK;
        Color white = Color.WHITE;

        // Tòa nhà 1 (trái) - thiết kế động với các lớp tam giác/wedge nhọn
        double b1X = width * 0.08;
        double b1Y = height * 0.4;
        double b1RightEdge = width * 0.3; // Cạnh phải thẳng đứng
        double b1Height = height * 0.45;
        int layers = 8; // 7-8 lớp

        // Vẽ từng lớp tam giác từ dưới lên, mỗi lớp nhọn hơn về trái
        for (int i = 0; i < layers; i++) {
            double layerHeight = b1Height / layers;
            double layerY = b1Y + (layers - 1 - i) * layerHeight;
            double leftOffset = (i * (b1RightEdge - b1X) * 0.15); // Mỗi lớp nhọn hơn về trái

            Polygon layer = new Polygon();
            layer.getPoints().addAll(
                    b1X + leftOffset, layerY + layerHeight,  // Điểm trái dưới
                    b1RightEdge, layerY + layerHeight,        // Điểm phải dưới
                    b1RightEdge, layerY,                     // Điểm phải trên
                    b1X + leftOffset, layerY                 // Điểm trái trên (nhọn)
            );
            layer.setFill(black);
            getChildren().add(layer);
        }

        // Tòa nhà 2 (giữa) - cao nhất, hình chữ nhật, lưới 3x3 cửa sổ
        double b2X = width * 0.32;
        double b2Y = height * 0.15;
        double b2Width = width * 0.2;
        double b2Height = height * 0.7;

        Rectangle building2 = new Rectangle(b2X, b2Y, b2Width, b2Height);
        building2.setFill(black);
        getChildren().add(building2);

        // Lưới cửa sổ 3x3 trên mặt trước - vẽ bằng đường line trắng
        // Đường ngang
        for (int i = 1; i < 3; i++) {
            Line hLine = new Line(b2X, b2Y + (b2Height / 3) * i, b2X + b2Width, b2Y + (b2Height / 3) * i);
            hLine.setStroke(white);
            hLine.setStrokeWidth(1.5);
            getChildren().add(hLine);
        }
        // Đường dọc
        for (int i = 1; i < 3; i++) {
            Line vLine = new Line(b2X + (b2Width / 3) * i, b2Y, b2X + (b2Width / 3) * i, b2Y + b2Height);
            vLine.setStroke(white);
            vLine.setStrokeWidth(1.5);
            getChildren().add(vLine);
        }

        // Mặt bên phải tòa nhà 2 - các đường dọc mỏng
        double side2X = b2X + b2Width;
        double side2Width = width * 0.05;
        Rectangle side2 = new Rectangle(side2X, b2Y, side2Width, b2Height);
        side2.setFill(black);
        getChildren().add(side2);

        // Đường dọc mỏng trên mặt bên
        int verticalLines = 4;
        for (int i = 1; i < verticalLines; i++) {
            Line vLine = new Line(
                    side2X + (side2Width / verticalLines) * i, b2Y,
                    side2X + (side2Width / verticalLines) * i, b2Y + b2Height
            );
            vLine.setStroke(white);
            vLine.setStrokeWidth(1.2);
            getChildren().add(vLine);
        }

        // Tòa nhà 3 (phải) - thấp nhất, hình chữ nhật, các đường dọc mỏng
        double b3X = width * 0.58;
        double b3Y = height * 0.35;
        double b3Width = width * 0.22;
        double b3Height = height * 0.5;

        Rectangle building3 = new Rectangle(b3X, b3Y, b3Width, b3Height);
        building3.setFill(black);
        getChildren().add(building3);

        // Các đường dọc mỏng trên mặt trước
        int lines3 = 5;
        for (int i = 1; i < lines3; i++) {
            Line vLine = new Line(
                    b3X + (b3Width / lines3) * i, b3Y,
                    b3X + (b3Width / lines3) * i, b3Y + b3Height
            );
            vLine.setStroke(white);
            vLine.setStrokeWidth(1.2);
            getChildren().add(vLine);
        }
    }
}

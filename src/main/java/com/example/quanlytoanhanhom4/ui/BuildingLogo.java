package com.example.quanlytoanhanhom4.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Logo component cho phần mềm quản lý tòa nhà
 * Thiết kế với ba tòa nhà chung cư và text "APARTMENT BUILDING"
 */
public class BuildingLogo extends Group {

    private static final double LOGO_WIDTH = 60;
    private static final double LOGO_HEIGHT = 60;

    // Màu sắc - đồng bộ với top bar màu xanh #2874A6
    private static final Color BUILDING_COLOR_WHITE = Color.WHITE; // Tòa nhà màu trắng để nổi bật trên nền xanh
    private static final Color BUILDING_COLOR_DARK = Color.rgb(30, 58, 95); // #1E3A5F - Tòa nhà màu xanh đậm cho nền sáng
    private static final Color DARK_BLUE = Color.rgb(30, 58, 95); // #1E3A5F - cho đường nền và outline
    private static final Color LIGHT_BLUE = Color.rgb(135, 206, 235); // #87CEEB - cho cửa sổ
    private static final Color WHITE = Color.WHITE;
    private static final Color BACKGROUND_BEIGE = Color.rgb(250, 248, 245); // Màu be nhạt cho nền login
    private static final Color TRANSPARENT = Color.TRANSPARENT; // Nền trong suốt

    private boolean forBlueBackground; // true = cho nền xanh (main), false = cho nền sáng (login)

    public BuildingLogo() {
        this(LOGO_WIDTH, LOGO_HEIGHT, false);
    }

    public BuildingLogo(double width, double height) {
        this(width, height, false);
    }

    public BuildingLogo(double width, double height, boolean forBlueBackground) {
        super();
        this.forBlueBackground = forBlueBackground;
        createLogo(width, height);
    }

    private void createLogo(double width, double height) {
        // Chọn màu dựa trên nền
        Color buildingColor = forBlueBackground ? BUILDING_COLOR_WHITE : BUILDING_COLOR_DARK;
        Color windowColor = forBlueBackground ? DARK_BLUE : WHITE;
        Color doorColor = forBlueBackground ? DARK_BLUE : WHITE;
        Color groundColor = forBlueBackground ? WHITE : DARK_BLUE;

        // Nền
        if (!forBlueBackground) {
            // Nền be cho login
            Rectangle background = new Rectangle(0, 0, width, height);
            background.setFill(BACKGROUND_BEIGE);
            getChildren().add(background);
        }
        // Nếu forBlueBackground = true, nền trong suốt để hiển thị màu nền của top bar

        // Đường nền (ground line)
        double groundY = height * 0.75;
        Rectangle ground = new Rectangle(0, groundY, width, height * 0.05);
        ground.setFill(groundColor);
        getChildren().add(ground);

        // Tòa nhà trái (thấp hơn tòa giữa)
        double leftBuildingX = width * 0.05;
        double leftBuildingY = height * 0.25;
        double leftBuildingWidth = width * 0.22;
        double leftBuildingHeight = height * 0.5;

        Rectangle leftBuilding = new Rectangle(leftBuildingX, leftBuildingY, leftBuildingWidth, leftBuildingHeight);
        leftBuilding.setFill(buildingColor);
        leftBuilding.setStroke(buildingColor);
        getChildren().add(leftBuilding);

        // Cửa sổ tòa trái - 8 cửa sổ dọc
        double leftWindowWidth = leftBuildingWidth * 0.6;
        double leftWindowHeight = leftBuildingHeight * 0.08;
        double leftWindowSpacing = leftBuildingHeight * 0.1;
        double leftWindowX = leftBuildingX + (leftBuildingWidth - leftWindowWidth) / 2;

        for (int i = 0; i < 8; i++) {
            double windowY = leftBuildingY + leftWindowSpacing + i * (leftWindowHeight + leftWindowSpacing * 0.3);
            Rectangle window = new Rectangle(leftWindowX, windowY, leftWindowWidth, leftWindowHeight);
            window.setFill(windowColor);
            getChildren().add(window);
        }

        // Tòa nhà giữa (cao nhất)
        double centerBuildingX = width * 0.32;
        double centerBuildingY = height * 0.1;
        double centerBuildingWidth = width * 0.36;
        double centerBuildingHeight = height * 0.65;

        Rectangle centerBuilding = new Rectangle(centerBuildingX, centerBuildingY, centerBuildingWidth, centerBuildingHeight);
        centerBuilding.setFill(buildingColor);
        centerBuilding.setStroke(buildingColor);
        getChildren().add(centerBuilding);

        // Cửa vào ở tòa giữa
        double doorWidth = centerBuildingWidth * 0.25;
        double doorHeight = centerBuildingHeight * 0.12;
        double doorX = centerBuildingX + (centerBuildingWidth - doorWidth) / 2;
        double doorY = groundY - doorHeight;
        Rectangle door = new Rectangle(doorX, doorY, doorWidth, doorHeight);
        door.setFill(doorColor);
        getChildren().add(door);

        // Cửa sổ tòa giữa bên trái - 8 cửa sổ dọc
        double centerLeftWindowWidth = centerBuildingWidth * 0.25;
        double centerLeftWindowHeight = centerBuildingHeight * 0.08;
        double centerLeftWindowSpacing = centerBuildingHeight * 0.08;
        double centerLeftWindowX = centerBuildingX + centerBuildingWidth * 0.15;

        for (int i = 0; i < 8; i++) {
            double windowY = centerBuildingY + centerLeftWindowSpacing + i * (centerLeftWindowHeight + centerLeftWindowSpacing * 0.2);
            Rectangle window = new Rectangle(centerLeftWindowX, windowY, centerLeftWindowWidth, centerLeftWindowHeight);
            window.setFill(windowColor);
            getChildren().add(window);
        }

        // Cửa sổ tòa giữa bên phải - 8 cửa sổ màu xanh nhạt vuông (2 cột x 4 hàng)
        double centerRightWindowSize = centerBuildingWidth * 0.12;
        double centerRightWindowSpacing = centerBuildingWidth * 0.08;
        double centerRightWindowX = centerBuildingX + centerBuildingWidth * 0.6;
        double centerRightWindowStartY = centerBuildingY + centerBuildingHeight * 0.1;
        double centerRightWindowRowSpacing = centerBuildingHeight * 0.1;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 2; col++) {
                double windowX = centerRightWindowX + col * (centerRightWindowSize + centerRightWindowSpacing);
                double windowY = centerRightWindowStartY + row * (centerRightWindowSize + centerRightWindowRowSpacing);
                Rectangle window = new Rectangle(windowX, windowY, centerRightWindowSize, centerRightWindowSize);
                window.setFill(LIGHT_BLUE);
                getChildren().add(window);
            }
        }

        // Tòa nhà phải (thấp hơn tòa giữa)
        double rightBuildingX = width * 0.72;
        double rightBuildingY = height * 0.25;
        double rightBuildingWidth = width * 0.22;
        double rightBuildingHeight = height * 0.5;

        Rectangle rightBuilding = new Rectangle(rightBuildingX, rightBuildingY, rightBuildingWidth, rightBuildingHeight);
        rightBuilding.setFill(buildingColor);
        rightBuilding.setStroke(buildingColor);
        getChildren().add(rightBuilding);

        // Cửa sổ tòa phải - 8 cửa sổ màu xanh nhạt vuông (2 cột x 4 hàng)
        double rightWindowSize = rightBuildingWidth * 0.18;
        double rightWindowSpacing = rightBuildingWidth * 0.12;
        double rightWindowX = rightBuildingX + rightBuildingWidth * 0.15;
        double rightWindowStartY = rightBuildingY + rightBuildingHeight * 0.08;
        double rightWindowRowSpacing = rightBuildingHeight * 0.08;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 2; col++) {
                double windowX = rightWindowX + col * (rightWindowSize + rightWindowSpacing);
                double windowY = rightWindowStartY + row * (rightWindowSize + rightWindowRowSpacing);
                Rectangle window = new Rectangle(windowX, windowY, rightWindowSize, rightWindowSize);
                window.setFill(LIGHT_BLUE);
                getChildren().add(window);
            }
        }
        
    }
}

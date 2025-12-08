package com.example.quanlytoanhanhom4.ui;

import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.InvoiceService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.service.TicketService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Component hiển thị biểu đồ dashboard hiện đại trên màn hình chính
 */
public class DashboardCharts extends VBox {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardCharts.class);
    
    public DashboardCharts() {
        super(20);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        
        // Tạo KPI Cards
        HBox kpiCards = createKPICards();
        
        // Tạo biểu đồ
        HBox chartsRow = createChartsRow();
        
        getChildren().addAll(kpiCards, chartsRow);
    }
    
    /**
     * Tạo các KPI cards hiển thị số liệu tổng quan
     */
    private HBox createKPICards() {
        HBox kpiBox = new HBox(20);
        kpiBox.setAlignment(Pos.CENTER);
        kpiBox.setPadding(new Insets(0, 0, 10, 0));
        
        // Load dữ liệu trong background thread
        javafx.concurrent.Task<Void> loadTask = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    // Lấy dữ liệu
                    int totalResidents = ResidentService.getAllResidents().size();
                    int totalApartments = ApartmentService.getAllApartments().size();
                    int totalInvoices = InvoiceService.getAllInvoices().size();
                    int totalTickets = TicketService.getAllTickets().size();
                    
                    // Cập nhật UI trên JavaFX Application Thread
                    Platform.runLater(() -> {
                        kpiBox.getChildren().addAll(
                            createKPICard("👥 Tổng cư dân", String.valueOf(totalResidents), "#2874A6"),
                            createKPICard("🏠 Tổng căn hộ", String.valueOf(totalApartments), "#27AE60"),
                            createKPICard("📄 Tổng hóa đơn", String.valueOf(totalInvoices), "#F39C12"),
                            createKPICard("🎫 Tổng yêu cầu", String.valueOf(totalTickets), "#9B59B6")
                        );
                    });
                } catch (Exception e) {
                    logger.error("Lỗi khi tải dữ liệu KPI", e);
                    Platform.runLater(() -> {
                        // Hiển thị dữ liệu mẫu nếu có lỗi
                        kpiBox.getChildren().addAll(
                            createKPICard("👥 Tổng cư dân", "--", "#2874A6"),
                            createKPICard("🏠 Tổng căn hộ", "--", "#27AE60"),
                            createKPICard("📄 Tổng hóa đơn", "--", "#F39C12"),
                            createKPICard("🎫 Tổng yêu cầu", "--", "#9B59B6")
                        );
                    });
                }
                return null;
            }
        };
        
        new Thread(loadTask).start();
        
        return kpiBox;
    }
    
    /**
     * Tạo một KPI card
     */
    private VBox createKPICard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 4);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748B; -fx-font-weight: 500;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 36px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: " + color + ";"
        );
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    /**
     * Tạo hàng biểu đồ
     */
    private HBox createChartsRow() {
        HBox chartsBox = new HBox(20);
        chartsBox.setAlignment(Pos.CENTER);
        
        // Load biểu đồ trong background
        javafx.concurrent.Task<Void> loadTask = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    PieChart invoiceChart = createInvoiceStatusChart();
                    BarChart<String, Number> ticketChart = createTicketStatusChart();
                    
                    Platform.runLater(() -> {
                        chartsBox.getChildren().addAll(invoiceChart, ticketChart);
                    });
                } catch (Exception e) {
                    logger.error("Lỗi khi tạo biểu đồ", e);
                }
                return null;
            }
        };
        
        new Thread(loadTask).start();
        
        return chartsBox;
    }
    
    /**
     * Tạo biểu đồ tròn trạng thái hóa đơn
     */
    private PieChart createInvoiceStatusChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Trạng thái Hóa đơn");
        pieChart.setPrefWidth(450);
        pieChart.setPrefHeight(350);
        pieChart.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 4); " +
            "-fx-padding: 20;"
        );
        pieChart.setLabelLineLength(15);
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLabelsVisible(true);
        
        try {
            // Lấy dữ liệu từ database với các status tiếng Việt
            int pending = InvoiceService.getInvoicesByStatus("CHỜ_THANH_TOÁN").size() + 
                         InvoiceService.getInvoicesByStatus("PENDING").size();
            int paid = InvoiceService.getInvoicesByStatus("ĐÃ_THANH_TOÁN").size() + 
                      InvoiceService.getInvoicesByStatus("PAID").size();
            int partial = InvoiceService.getInvoicesByStatus("THANH_TOÁN_MỘT_PHẦN").size();
            int overdue = InvoiceService.getInvoicesByStatus("QUÁ_HẠN").size() + 
                         InvoiceService.getInvoicesByStatus("OVERDUE").size();
            
            PieChart.Data pendingData = new PieChart.Data("Chưa thanh toán", pending);
            PieChart.Data paidData = new PieChart.Data("Đã thanh toán", paid);
            PieChart.Data partialData = new PieChart.Data("Thanh toán một phần", partial);
            PieChart.Data overdueData = new PieChart.Data("Quá hạn", overdue);
            
            pieChart.getData().addAll(pendingData, partialData, paidData, overdueData);
            
            // Tùy chỉnh màu sắc sau khi biểu đồ được render
            // Sử dụng Platform.runLater với delay để đảm bảo node đã được tạo
            Platform.runLater(() -> {
                javafx.concurrent.Task<Void> colorTask = new javafx.concurrent.Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(200);
                        return null;
                    }
                };
                colorTask.setOnSucceeded(e -> {
                    if (pieChart.getData().size() > 0 && pieChart.getData().get(0).getNode() != null) {
                        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #F39C12;"); // Chưa thanh toán - Vàng
                    }
                    if (pieChart.getData().size() > 1 && pieChart.getData().get(1).getNode() != null) {
                        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #3498DB;"); // Thanh toán một phần - Xanh dương
                    }
                    if (pieChart.getData().size() > 2 && pieChart.getData().get(2).getNode() != null) {
                        pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #27AE60;"); // Đã thanh toán - Xanh lá
                    }
                    if (pieChart.getData().size() > 3 && pieChart.getData().get(3).getNode() != null) {
                        pieChart.getData().get(3).getNode().setStyle("-fx-pie-color: #E74C3C;"); // Quá hạn - Đỏ
                    }
                });
                new Thread(colorTask).start();
            });
            
        } catch (Exception e) {
            logger.error("Lỗi khi tạo biểu đồ hóa đơn", e);
        }
        
        return pieChart;
    }
    
    /**
     * Tạo biểu đồ cột trạng thái yêu cầu
     */
    private BarChart<String, Number> createTicketStatusChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng");
        
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Trạng thái Yêu cầu & Sự cố");
        barChart.setPrefWidth(450);
        barChart.setPrefHeight(350);
        barChart.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 4); " +
            "-fx-padding: 20;"
        );
        barChart.setLegendVisible(true);
        barChart.setAnimated(true);
        
        try {
            // Lấy dữ liệu từ database với các status tiếng Việt
            int open = TicketService.getTicketsByStatus("OPEN").size() + 
                      TicketService.getTicketsByStatus("TIẾP_NHẬN").size();
            int assigned = TicketService.getTicketsByStatus("ASSIGNED").size() + 
                          TicketService.getTicketsByStatus("ĐANG_XỬ_LÝ").size();
            int inProgress = TicketService.getTicketsByStatus("IN_PROGRESS").size() + 
                            TicketService.getTicketsByStatus("ĐANG_XỬ_LÝ").size();
            int resolved = TicketService.getTicketsByStatus("RESOLVED").size() + 
                          TicketService.getTicketsByStatus("HOÀN_THÀNH").size();
            int closed = TicketService.getTicketsByStatus("CLOSED").size() + 
                        TicketService.getTicketsByStatus("ĐÓNG_YÊU_CẦU").size();
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Số lượng");
            series.getData().add(new XYChart.Data<>("Mới", open));
            series.getData().add(new XYChart.Data<>("Đã phân công", assigned));
            series.getData().add(new XYChart.Data<>("Đang xử lý", inProgress));
            series.getData().add(new XYChart.Data<>("Đã giải quyết", resolved));
            series.getData().add(new XYChart.Data<>("Đã đóng", closed));
            
            barChart.getData().add(series);
            
            // Tùy chỉnh màu sắc cho các cột sau khi render
            String[] colors = {"#E74C3C", "#F39C12", "#27AE60", "#3498DB"};
            final String[] finalColors = colors;
            Platform.runLater(() -> {
                series.getData().forEach(data -> {
                    data.getNode().addEventHandler(javafx.scene.input.MouseEvent.ANY, e -> {});
                });
                // Đợi một chút để đảm bảo node được render
                javafx.concurrent.Task<Void> colorTask = new javafx.concurrent.Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(100);
                        return null;
                    }
                };
                colorTask.setOnSucceeded(e -> {
                    int index = 0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        if (data.getNode() != null) {
                            data.getNode().setStyle("-fx-bar-fill: " + finalColors[index % finalColors.length] + ";");
                        }
                        index++;
                    }
                });
                new Thread(colorTask).start();
            });
            
        } catch (Exception e) {
            logger.error("Lỗi khi tạo biểu đồ yêu cầu", e);
        }
        
        return barChart;
    }
}


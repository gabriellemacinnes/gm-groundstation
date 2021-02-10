
package controller.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;


import org.gillius.jfxutils.JFXUtil;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.FixedFormatTickFormatter;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;


import controller.Parser;
import javafx.fxml.FXMLLoader;

import javafx.scene.input.ScrollEvent;
import javafx.scene.Node;
import javafx.event.EventHandler;


public class GraphController {
	
//	@FXML private GridPane numbers;
//	@FXML private NumbersController numbersController;
//	
//	public void startTimer(double[] data, EnumMap<DataIndex, Integer> DataFormat) {
//		System.out.println(numbersController);
//		numbersController.updateNumDisplay(data, DataFormat);
//	}
	
	
	/* ---------------INFO------------------
	 *
	 * Drag/scroll to zoom
	 * 
	 * Double click to reset to auto-zoom
	 * 
	 */
	

	
	final int window_size = 20;
	ScheduledExecutorService scheduledExecutorService;
	
	@FXML
	private LineChart<Number, Number> altitudeChart;
	
	XYChart.Series<Number, Number> altitudeData;
	
	@FXML
	private LineChart<Number, Number> velocityChart;
	
	XYChart.Series<Number, Number> velocityData;
	
	@FXML
	private LineChart<Number, Number> accelerationChart;
	
	XYChart.Series<Number, Number> accelerationData;
	
	@FXML
	private LineChart<Number, Number> RSSIChart;
	
	XYChart.Series<Number, Number> RSSIData;
	
	public void initializeGraphs() {
        initializeAltitudeChart();
        initializeVelocityChart();
        initializeAccelerationChart();
        initializeRSSIChart();
	}
	
	private void initializeAltitudeChart() {
		
		altitudeData = new XYChart.Series<>();
		altitudeData.setName("altitudeData");
		altitudeChart.getData().add(altitudeData);
		
		NumberAxis yAxis = (NumberAxis) altitudeChart.getYAxis();
		yAxis.setForceZeroInRange(false);
		
		NumberAxis xAxis = (NumberAxis) altitudeChart.getXAxis();
		xAxis.setForceZeroInRange(false);
		
		JFXChartUtil.setupZooming(altitudeChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() != MouseButton.PRIMARY ||
				     mouseEvent.isShortcutDown() )
					mouseEvent.consume();
			}
		} );
		
		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(altitudeChart);

	}
	
	private void initializeVelocityChart() {
		velocityData = new XYChart.Series<>();
		velocityData.setName("velocityData");
		velocityChart.getData().add(velocityData);
		
		NumberAxis yAxis = (NumberAxis) velocityChart.getYAxis();
		yAxis.setForceZeroInRange(false);
		
		NumberAxis xAxis = (NumberAxis) velocityChart.getXAxis();
		xAxis.setForceZeroInRange(false);
		
		JFXChartUtil.setupZooming(velocityChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() != MouseButton.PRIMARY ||
				     mouseEvent.isShortcutDown() )
					mouseEvent.consume();
			}
			
		} );
		
		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(velocityChart);
		
	}
	
	private void initializeAccelerationChart() {
		accelerationData = new XYChart.Series<>();
		accelerationData.setName("accelerationData");
		accelerationChart.getData().add(accelerationData);	
		
		NumberAxis yAxis = (NumberAxis) accelerationChart.getYAxis();
		yAxis.setForceZeroInRange(false);
		
		NumberAxis xAxis = (NumberAxis) accelerationChart.getXAxis();
		xAxis.setForceZeroInRange(false);
		
		JFXChartUtil.setupZooming(accelerationChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() != MouseButton.PRIMARY ||
				     mouseEvent.isShortcutDown() )
					mouseEvent.consume();
			}
		} );

		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(accelerationChart);
		
	}
	
	private void initializeRSSIChart() {
		RSSIData = new XYChart.Series<>();
		RSSIData.setName("MYDATA");
		RSSIChart.getData().add(RSSIData);	
		
		NumberAxis yAxis = (NumberAxis) RSSIChart.getYAxis();
		yAxis.setForceZeroInRange(false);
		
		NumberAxis xAxis = (NumberAxis) RSSIChart.getXAxis();
		xAxis.setForceZeroInRange(false);
		
		JFXChartUtil.setupZooming(RSSIChart, new EventHandler<MouseEvent>() {
			@Override
			public void handle( MouseEvent mouseEvent ) {
				if ( mouseEvent.getButton() != MouseButton.PRIMARY ||
				     mouseEvent.isShortcutDown() )
					mouseEvent.consume();
			}
		} );
		
		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(RSSIChart);
		
	}
	
	public void addGraphData(double[] data, EnumMap<DataIndex, Integer> DataFormat) {
		
		addAltitudeData(data[DataFormat.get(DataIndex.TIME_INDEX)], data[DataFormat.get(DataIndex.ALTITUDE_INDEX)]);
		addVelocityData(data[DataFormat.get(DataIndex.TIME_INDEX)], data[DataFormat.get(DataIndex.VELOCITY_INDEX)]);
		addAccelerationData(data[DataFormat.get(DataIndex.TIME_INDEX)], data[DataFormat.get(DataIndex.ACCELERATION_INDEX)]);
		addRSSIData(data[DataFormat.get(DataIndex.TIME_INDEX)], data[DataFormat.get(DataIndex.RSSI_INDEX)]);
	}
	
	private void addAltitudeData(Double x, Double y) {
		altitudeData.getData().add(new XYChart.Data<>(x, y));
		if (altitudeData.getData().size() > window_size)
			altitudeData.getData().remove(0);
	}
	
	private void addVelocityData(Double x, Double y) {
		velocityData.getData().add(new XYChart.Data<>(x, y));
		if (velocityData.getData().size() > window_size)
			velocityData.getData().remove(0);
	}
	
	private void addAccelerationData(Double x, Double y) {
		accelerationData.getData().add(new XYChart.Data<>(x, y));
		if (accelerationData.getData().size() > window_size)
			accelerationData.getData().remove(0);
	}
	
	private void addRSSIData(Double x, Double y) {
		RSSIData.getData().add(new XYChart.Data<>(x, y));
		if (RSSIData.getData().size() > window_size)
			RSSIData.getData().remove(0);
	}
	

}



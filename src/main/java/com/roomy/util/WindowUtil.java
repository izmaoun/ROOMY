package com.roomy.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WindowUtil {
    
    public static double getCurrentWidth(Stage stage) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds.getWidth();
    }
    
    public static double getCurrentHeight(Stage stage) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds.getHeight();
    }
    
    public static double getAdaptiveWidth() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds.getWidth();
    }
    
    public static double getAdaptiveHeight() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds.getHeight();
    }
}
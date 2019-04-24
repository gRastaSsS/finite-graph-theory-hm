package candlelight;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.util.List;

public class ChartUtil {
    public static void generateAndSaveToFile(String title, String xTitle, String yTitle, String seriesName, List x, List y, String filePath) throws IOException {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle(xTitle)
                .yAxisTitle(yTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(0.99);
        chart.getStyler().setOverlapped(true);

        chart.addSeries(seriesName, x, y);

        BitmapEncoder.saveBitmapWithDPI(chart, filePath, BitmapEncoder.BitmapFormat.PNG, 300);
    }
}

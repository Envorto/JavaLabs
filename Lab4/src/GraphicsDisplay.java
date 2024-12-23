import java.awt.*;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Определяем границы отображения
        double minX = graphicsData[0][0], maxX = graphicsData[0][0];
        double minY = graphicsData[0][1], maxY = graphicsData[0][1];
        double sumY = 0;
        for (Double[] point : graphicsData) {
            if (point[0] < minX) minX = point[0];
            if (point[0] > maxX) maxX = point[0];
            if (point[1] < minY) minY = point[1];
            if (point[1] > maxY) maxY = point[1];
            sumY += point[1];
        }
        double avgY = sumY / graphicsData.length;

        // Увеличиваем границы для удобства
        double padding = 0.1 * Math.max(maxX - minX, maxY - minY); // добавим 10% от диапазона как отступы
        minX -= padding;
        maxX += padding;
        minY -= padding;
        maxY += padding;

        double scaleX = getWidth() / (maxX - minX);
        double scaleY = getHeight() / (maxY - minY);
        double offsetX = -minX * scaleX;
        double offsetY = maxY * scaleY;

        // Рисуем оси
        if (showAxis) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f));

            // Рисуем ось X
            g2d.drawLine(
                    x(minX, scaleX, offsetX),
                    y(0, scaleY, offsetY),
                    x(maxX, scaleX, offsetX),
                    y(0, scaleY, offsetY)
            );

            // Рисуем ось Y
            g2d.drawLine(
                    x(0, scaleX, offsetX),
                    y(minY, scaleY, offsetY),
                    x(0, scaleX, offsetX),
                    y(maxY, scaleY, offsetY)
            );

            int zeroX = x(0, scaleX, offsetX);
            int zeroY = y(0, scaleY, offsetY);
            g2d.drawString("0", zeroX + 5, zeroY - 5);
        }


        // Рисуем уровни
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{4, 4}, 0));
        double[] levels = {0.9, 0.5, 0.1};
        for (double level : levels) {
            double yLevel = minY + level * (maxY - minY);
            g2d.drawLine(x(minX, scaleX, offsetX), y(yLevel, scaleY, offsetY), x(maxX, scaleX, offsetX), y(yLevel, scaleY, offsetY));
        }

        // Рисуем линии графика
        g2d.setColor(Color.BLUE);
        float[] dashPattern = {3, 3, 3, 12, 6, 6}; // Паттерн: три точки, тире, дефис, дефис
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dashPattern, 0));
        for (int i = 1; i < graphicsData.length; i++) {
            g2d.drawLine(x(graphicsData[i - 1][0], scaleX, offsetX), y(graphicsData[i - 1][1], scaleY, offsetY),
                    x(graphicsData[i][0], scaleX, offsetX), y(graphicsData[i][1], scaleY, offsetY));
        }

        // Рисуем маркеры
        if (showMarkers) {
            for (Double[] point : graphicsData) {
                int centerX = x(point[0], scaleX, offsetX);
                int centerY = y(point[1], scaleY, offsetY);

                if (point[1] > 2 * avgY) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.RED);
                }

                // Рисуем перевернутый треугольник
                int[] xPoints = {centerX - 5, centerX + 5, centerX};
                int[] yPoints = {centerY - 5, centerY - 5, centerY + 5};
                g2d.fillPolygon(xPoints, yPoints, 3);
            }
        }
    }

    // Вспомогательный метод для преобразования X координаты
    private int x(double x, double scaleX, double offsetX) {
        return (int) (x * scaleX + offsetX);
    }

    // Вспомогательный метод для преобразования Y координаты
    private int y(double y, double scaleY, double offsetY) {
        return (int) (offsetY - y * scaleY);
    }
}
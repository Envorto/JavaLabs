import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;

    private Rectangle zoomRect = null;
    private Point zoomStart = null;
    private double originalMinX, originalMaxX, originalMinY, originalMaxY;
    private double minX, maxX, minY, maxY;

    public GraphicsDisplay() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Начало выделения области при нажатии левой кнопки мыши
                    zoomStart = e.getPoint();
                    zoomRect = new Rectangle(zoomStart); // Инициализация рамки
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // Сброс масштаба при нажатии правой кнопки мыши
                    resetZoom();
                    zoomRect = null; // Убираем рамку выделения
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && zoomRect != null) {
                    // Завершение выделения области
                    Point zoomEnd = e.getPoint();
                    if (zoomStart != null) {
                        double x1 = unscaleX(zoomStart.x);
                        double y1 = unscaleY(zoomStart.y);
                        double x2 = unscaleX(zoomEnd.x);
                        double y2 = unscaleY(zoomEnd.y);

                        minX = Math.min(x1, x2);
                        maxX = Math.max(x1, x2);
                        minY = Math.min(y1, y2);
                        maxY = Math.max(y1, y2);
                        repaint();
                    }
                    zoomRect = null; // Убираем рамку после завершения выделения
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (zoomStart != null && SwingUtilities.isLeftMouseButton(e)) {
                    // Обновление рамки выделения только при левой кнопке мыши
                    Point zoomEnd = e.getPoint();
                    zoomRect.setBounds(
                            Math.min(zoomStart.x, zoomEnd.x),
                            Math.min(zoomStart.y, zoomEnd.y),
                            Math.abs(zoomStart.x - zoomEnd.x),
                            Math.abs(zoomStart.y - zoomEnd.y)
                    );
                    repaint();
                }
            }
        });
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        calculateBounds();
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

    private void calculateBounds() {
        if (graphicsData == null || graphicsData.length == 0) return;

        originalMinX = graphicsData[0][0];
        originalMaxX = graphicsData[0][0];
        originalMinY = graphicsData[0][1];
        originalMaxY = graphicsData[0][1];

        for (Double[] point : graphicsData) {
            if (point[0] < originalMinX) originalMinX = point[0];
            if (point[0] > originalMaxX) originalMaxX = point[0];
            if (point[1] < originalMinY) originalMinY = point[1];
            if (point[1] > originalMaxY) originalMaxY = point[1];
        }

        // Увеличим диапазоны для отдаления графика
        double paddingX = (originalMaxX - originalMinX) * 0.1;
        double paddingY = (originalMaxY - originalMinY) * 0.1;

        minX = originalMinX - paddingX;
        maxX = originalMaxX + paddingX;
        minY = originalMinY - paddingY;
        maxY = originalMaxY + paddingY;
    }


    private void resetZoom() {
        double paddingX = (originalMaxX - originalMinX) * 0.1;
        double paddingY = (originalMaxY - originalMinY) * 0.1;

        minX = originalMinX - paddingX;
        maxX = originalMaxX + paddingX;
        minY = originalMinY - paddingY;
        maxY = originalMaxY + paddingY;
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

        // Вычисляем масштаб и смещение
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
            g2d.drawLine(x(minX, scaleX, offsetX), y(yLevel, scaleY, offsetY),
                    x(maxX, scaleX, offsetX), y(yLevel, scaleY, offsetY));
        }

        // Рисуем линии графика
        g2d.setColor(Color.BLUE);
        float[] dashPattern = {3, 3, 3, 12, 6, 6}; // Паттерн: три точки, тире, дефис, дефис
        g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dashPattern, 0));
        for (int i = 1; i < graphicsData.length; i++) {
            g2d.drawLine(x(graphicsData[i - 1][0], scaleX, offsetX),
                    y(graphicsData[i - 1][1], scaleY, offsetY),
                    x(graphicsData[i][0], scaleX, offsetX),
                    y(graphicsData[i][1], scaleY, offsetY));
        }

        // Рисуем маркеры
        if (showMarkers) {
            for (Double[] point : graphicsData) {
                int centerX = x(point[0], scaleX, offsetX);
                int centerY = y(point[1], scaleY, offsetY);

                if (point[1] > 2 * ((minY + maxY) / 2)) {
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

        // Рисуем рамку выделения
        if (zoomRect != null) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{5, 5}, 0));
            g2d.draw(zoomRect);
        }
    }

    private int x(double x, double scaleX, double offsetX) {
        return (int) (x * scaleX + offsetX);
    }

    private int y(double y, double scaleY, double offsetY) {
        return (int) (offsetY - y * scaleY);
    }

    private double unscaleX(int screenX) {
        double scaleX = getWidth() / (maxX - minX);
        double offsetX = -minX * scaleX;
        return (screenX - offsetX) / scaleX;
    }

    private double unscaleY(int screenY) {
        double scaleY = getHeight() / (maxY - minY);
        double offsetY = maxY * scaleY;
        return (offsetY - screenY) / scaleY;
    }
}

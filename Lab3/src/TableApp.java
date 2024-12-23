import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class TableApp extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] coefficients;
    private JFileChooser fileChooser;  // Для выбора файла при сохранении

    public TableApp(String[] args) {
        this.coefficients = args;
        initUI();
    }

    private void initUI() {
        setTitle("Многочлен");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton calculateButton = new JButton("Вычислить");
        calculateButton.addActionListener(e -> calculatePolynomial());
        controlPanel.add(calculateButton);

        JButton clearButton = new JButton("Очистить поле");
        clearButton.addActionListener(e -> clearTable());
        controlPanel.add(clearButton);

        // Кнопка "Сохранить данные для построения графика"
        JButton saveButton = new JButton("Сохранить данные для построения графика");
        saveButton.addActionListener(e -> saveToGraphicsFile());
        controlPanel.add(saveButton);

        add(controlPanel, BorderLayout.NORTH);

        // Таблица
        String[] columnNames = {"X", "Y", "Разностороннее"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : Object.class;
            }
        };
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CustomColor());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Меню
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Справка");

        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Программа для вычисления многочлена по схеме Горнера.\n" +
                            "Автор: Харевич Николай\nГруппа: 10",
                    "О программе",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void calculatePolynomial() {
        try {
            double start = Double.parseDouble(JOptionPane.showInputDialog(this, "Введите начало отрезка:"));
            double end = Double.parseDouble(JOptionPane.showInputDialog(this, "Введите конец отрезка:"));
            double step = Double.parseDouble(JOptionPane.showInputDialog(this, "Введите шаг:"));

            if (start >= end || step <= 0) {
                JOptionPane.showMessageDialog(this, "Неверные параметры диапазона или шага.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Очистка таблицы
            tableModel.setRowCount(0);

            // Вычисление значений
            for (double x = start; x <= end; x += step) {
                double y = PolynomialUtils.calculate(coefficients, x);
                boolean isRaznostoronnee = NumberUtils.calculateRaznostoronnee(y);
                tableModel.addRow(new Object[]{x, y, isRaznostoronnee});
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    // Метод для сохранения данных в файл для графика
    private void saveToGraphicsFile() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
        }

        // Открыть диалог для выбора файла
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Проходим по строкам таблицы и сохраняем значения
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Object xValue = tableModel.getValueAt(i, 0); // Получаем значение X
                    Object yValue = tableModel.getValueAt(i, 1); // Получаем значение Y

                    // Проверяем, что оба значения существуют
                    if (xValue != null && yValue != null) {
                        // Записываем данные в файл через пробел
                        writer.write(xValue.toString() + " " + yValue.toString());
                        writer.newLine(); // Перенос строки
                    }
                }
                JOptionPane.showMessageDialog(this, "Данные успешно сохранены!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}

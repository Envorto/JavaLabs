import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame {
    private double mem1 = 0;
    private double mem2 = 0;
    private double mem3 = 0;
    private int activeMemory = 1; // 1 - mem1, 2 - mem2, 3 - mem3
    private double LastResult = 0;

    public CalculatorApp() {
        // Настройка окна
        setTitle("Формулы");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Поля для ввода переменных с метками
        JLabel xLabel = new JLabel("x:");
        JLabel yLabel = new JLabel("y:");
        JLabel zLabel = new JLabel("z:");
        JTextField xField = new JTextField();  // x
        JTextField yField = new JTextField();  // y
        JTextField zField = new JTextField();  // z

        // Настройка меток
        xLabel.setBounds(20, 30, 20, 25);
        yLabel.setBounds(20, 60, 20, 25);
        zLabel.setBounds(20, 90, 20, 25);
        add(xLabel);
        add(yLabel);
        add(zLabel);

        // Настройка полей ввода
        xField.setBounds(50, 30, 100, 25);
        yField.setBounds(50, 60, 100, 25);
        zField.setBounds(50, 90, 100, 25);
        add(xField);
        add(yField);
        add(zField);

        // Радио-кнопки для выбора формулы
        JRadioButton formula1Button = new JRadioButton("Формула 1");
        JRadioButton formula2Button = new JRadioButton("Формула 2");
        formula1Button.setBounds(200, 30, 100, 25);
        formula2Button.setBounds(200, 60, 100, 25);
        ButtonGroup formulaGroup = new ButtonGroup();
        formulaGroup.add(formula1Button);
        formulaGroup.add(formula2Button);
        add(formula1Button);
        add(formula2Button);

        // Радио-кнопки для выбора активной памяти
        JRadioButton mem1Button = new JRadioButton("Переменная 1");
        JRadioButton mem2Button = new JRadioButton("Переменная 2");
        JRadioButton mem3Button = new JRadioButton("Переменная 3");
        mem1Button.setBounds(50, 150, 120, 25);
        mem2Button.setBounds(50, 180, 120, 25);
        mem3Button.setBounds(50, 210, 120, 25);
        ButtonGroup memoryGroup = new ButtonGroup();
        memoryGroup.add(mem1Button);
        memoryGroup.add(mem2Button);
        memoryGroup.add(mem3Button);
        add(mem1Button);
        add(mem2Button);
        add(mem3Button);


        mem1Button.setSelected(true);

        // Кнопки
        JButton calculateButton = new JButton("Рассчитать");
        JButton mcButton = new JButton("MC");
        JButton mPlusButton = new JButton("M+");
        calculateButton.setBounds(50, 250, 120, 30);
        mcButton.setBounds(180, 250, 80, 30);
        mPlusButton.setBounds(270, 250, 80, 30);
        add(calculateButton);
        add(mcButton);
        add(mPlusButton);

        // Поле для вывода результата
        JTextField resultField = new JTextField();
        resultField.setBounds(50, 300, 300, 30);
        resultField.setEditable(false);
        add(resultField);

        // Обработчики событий для кнопок переменной
        mem1Button.addActionListener(e -> activeMemory = 1);
        mem2Button.addActionListener(e -> activeMemory = 2);
        mem3Button.addActionListener(e -> activeMemory = 3);

        // Обработчик события кнопки "Рассчитать"
        calculateButton.addActionListener(e -> {
            try {
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());
                double z = Double.parseDouble(zField.getText());
                if (formula1Button.isSelected()) {
                    LastResult = calculateFormula1(x, y, z);
                } else if (formula2Button.isSelected()) {
                    LastResult = calculateFormula2(x, y, z);
                } else {
                    throw new Exception("Выберите формулу!");
                }
                resultField.setText(String.valueOf(LastResult));
            } catch (Exception ex) {
                resultField.setText("Ошибка: " + ex.getMessage());
                LastResult = 0;
            }
        });

        // Обработчик события кнопки "MC"
        mcButton.addActionListener(e -> clearActiveMemory());

        // Обработчик события кнопки "M+"
        mPlusButton.addActionListener(e -> {
            try {
                addToActiveMemory(LastResult);
                resultField.setText("Сумма: " + getActiveMemoryValue());
            } catch (Exception ex) {
                resultField.setText("Ошибка: " + ex.getMessage());
            }
        });
    }

    private double calculateFormula1(double x, double y, double z) {
        return Math.pow(Math.log(1 + x), 2) + Math.pow(Math.cos(Math.PI * Math.pow(z, 3)), Math.sin(y))
                + Math.pow(y, 3);
    }

    private double calculateFormula2(double x, double y, double z) {
        double numerator = y * Math.pow(x, 2);
        double denominator = Math.log10(Math.pow(z, y)) + Math.pow(Math.cos(Math.cbrt(3 * x)), 2);
        return numerator / denominator;
    }

    private void clearActiveMemory() {
        if (activeMemory == 1) mem1 = 0;
        else if (activeMemory == 2) mem2 = 0;
        else if (activeMemory == 3) mem3 = 0;
    }

    private void addToActiveMemory(double value) {
        if (activeMemory == 1) mem1 += value;
        else if (activeMemory == 2) mem2 += value;
        else if (activeMemory == 3) mem3 += value;
    }

    private double getActiveMemoryValue() {
        if (activeMemory == 1) return mem1;
        else if (activeMemory == 2) return mem2;
        else return mem3;
    }
}

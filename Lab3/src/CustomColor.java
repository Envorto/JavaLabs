import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class CustomColor extends DefaultTableCellRenderer {

    private DecimalFormat formatter;

    public CustomColor() {
        formatter = new DecimalFormat("0.000");
        formatter.setGroupingUsed(false);
        formatter.setDecimalSeparatorAlwaysShown(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Number) {
            double number = ((Number) value).doubleValue();
            String formattedValue = formatter.format(number);


            setText(formattedValue);


            if (column == 1) {
                String[] parts = formattedValue.split("\\.");
                if (parts.length == 2) {
                    boolean allEven = parts[0].chars().allMatch(c -> (c - '0') % 2 == 0) &&
                            parts[1].chars().allMatch(c -> (c - '0') % 2 == 0);
                    if (allEven) {
                        cell.setBackground(Color.CYAN); // Голубой
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                } else {
                    cell.setBackground(Color.WHITE);
                }
            } else {
                cell.setBackground(Color.WHITE);
            }
        } else {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}

public class NumberUtils {

    public static boolean calculateRaznostoronnee(double number) {
        String[] parts = String.valueOf(number).split("\\.");
        if (parts.length != 2) return false; // Если нет дробной части

        String integerPart = parts[0];      // Целая часть
        String fractionalPart = parts[1];  // Дробная часть


        boolean allEvenInInteger = integerPart.chars().allMatch(c -> (c - '0') % 2 == 0);
        boolean allOddInFractional = fractionalPart.chars().allMatch(c -> (c - '0') % 2 != 0);

        boolean allOddInInteger = integerPart.chars().allMatch(c -> (c - '0') % 2 != 0);
        boolean allEvenInFractional = fractionalPart.chars().allMatch(c -> (c - '0') % 2 == 0);


        return (allEvenInInteger && allOddInFractional) || (allOddInInteger && allEvenInFractional);
    }
}

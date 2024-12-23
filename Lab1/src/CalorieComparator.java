import java.util.Comparator;

public class CalorieComparator implements Comparator<Food> {
    @Override
    public int compare(Food o1, Food o2) {
        if (o1 instanceof Nutritious && o2 instanceof Nutritious) {
            return Integer.compare(((Nutritious) o2).calculateCalories(),
                    ((Nutritious) o1).calculateCalories());
        }
        return 0; // Если объекты не поддерживают Nutritious, они равны
    }
}

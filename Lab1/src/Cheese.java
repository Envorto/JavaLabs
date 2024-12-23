public class Cheese extends Food implements Nutritious {

    private int calories;

    public Cheese() {
        super("Сыр");
        this.calories = 0;
    }

    @Override
    public void consume() {
        System.out.println(this + " съеден");
        this.calories = 360;
    }

    @Override
    public int calculateCalories() {
        return calories;
    }
}

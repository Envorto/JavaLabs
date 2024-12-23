public class Apple extends Food implements Nutritious {

    private String size; // Размер яблока
    private int calories; // Калорийность яблока

    public Apple(String size) {
        super("Яблоко");
        this.size = size.toLowerCase();
        this.calories = 0; // Изначально калорийность равна 0
    }

    @Override
    public void consume() {
        System.out.println(this + " съедено");
        if (this.size.equals("small")) {
            this.calories = 116;
        } else if (this.size.equals("large")) {
            this.calories = 169;
        }
    }

    @Override
    public int calculateCalories() {
        return calories;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size.toLowerCase();
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof Apple)) return false;
            return size.equals(((Apple)arg0).size);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " размера '" + size.toUpperCase() + "'";
    }
}

public class ChewingGum extends Food implements Nutritious {
    private String flavour; // Привкус
    private int calories; // Калорийность

    public ChewingGum(String flavour) {
        super("Жевательная резинка");
        this.flavour = flavour;
        switch (flavour.toLowerCase()) {
            case "мята":
                this.calories = 163;
                break;
            case "арбуз":
                this.calories = 170;
                break;
            case "вишня":
                this.calories = 191;
                break;
            default:
                this.calories = 0; // Неизвестный вкус
        }
    }

    @Override
    public void consume() {
        System.out.println(this + " съедена");
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    @Override
    public int calculateCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof ChewingGum)) return false;
            return flavour.equalsIgnoreCase(((ChewingGum) arg0).flavour);
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " со вкусом '" + flavour + "'";
    }
}

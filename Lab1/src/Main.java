import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Food[] breakfast = new Food[10];
        int itemsSoFar = 0;


        for (String arg : args) {
            String[] parts = arg.split("/");
            if (parts[0].equals("Cheese")) {
                breakfast[itemsSoFar] = new Cheese();
            } else if (parts[0].equals("Apple")) {
                breakfast[itemsSoFar] = new Apple(parts[1]);
            } else if (parts[0].equals("ChewingGum")) {
                breakfast[itemsSoFar] = new ChewingGum(parts[1]);
            }
            itemsSoFar++;
        }

        // Употребление продуктов
        for (Food item : breakfast) {
            if (item != null) {
                item.consume();
            } else {
                break;
            }
        }

        // Подсчет общей калорийности
        int totalCalories = 0;
        for (Food item : breakfast) {
                if (item != null && item instanceof Nutritious) {
                totalCalories += ((Nutritious) item).calculateCalories();
            }
        }


        // Подсчет количества продуктов
        int countCheese = countItems(breakfast, new Cheese());
        int countAppleSmall = countItems(breakfast, new Apple("small"));
        int countAppleLarge = countItems(breakfast, new Apple("large"));
        int countGumFMint = countItems(breakfast, new ChewingGum("мята"));
        int countGumFWaterMelon = countItems(breakfast, new ChewingGum("арбуз"));
        int countGumFCherry = countItems(breakfast, new ChewingGum("вишня"));

        System.out.println("Количество сыра в завтраке " + countCheese);
        System.out.println("Количество маленького яблока в завтраке " + countAppleSmall);
        System.out.println("Количество большего яблока в завтраке " + countAppleLarge);
        System.out.println("Количество жевачки со вкусом мяты в завтраке " + countGumFMint);
        System.out.println("Количество жевачки со вкусом арбуза в завтраке " + countGumFWaterMelon);
        System.out.println("Количество жевачки со вкусом вишни в завтраке " + countGumFCherry);




        // Обработка аргументов для вывода информации
        for (String arg : args) {
            if (arg.equals("-calories")) {
                System.out.println("Общая калорийность завтрака: " + totalCalories);
            } else if (arg.equals("-sort")) {
                // Сортировка по калорийности в обратном порядке
                Arrays.sort(breakfast, 0, itemsSoFar, new CalorieComparator());
                System.out.println("Завтрак отсортирован по калорийности в обратном порядке:");
                for (Food item : breakfast) {
                    if (item != null) {
                        System.out.println(item);
                    }
                }
            }
        }
        System.out.println("Всего хорошего!");

    }


    public static int countItems(Food[] breakfast, Food target) {
        int count = 0;
        for(Food item : breakfast){
            if(item != null && item.equals(target)){
                count++;
            }
        }
        return count;
    }

}

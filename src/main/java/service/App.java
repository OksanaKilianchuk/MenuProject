package service;

import entities.Dish;
import entities.DishDao;
import entities.IDishDao;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by uzer on 07.09.2016.
 */
public class App {

    static IDishDao dishInterface = new DishDao();
    static List<Dish> selectedDishes = new ArrayList<>();

    public static void main(String[] args) {


        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("Choose a command, please:");
                System.out.println("Add new dish - > NEW DISH");
                System.out.println("Print all menu - > MENU");
                System.out.println("Find an dish - > FIND DISH");
                System.out.println("Select a set of dishes -> SELECT DISHES");

                String command = sc.nextLine();
                switch (command) {
                    case "NEW DISH":
                        addDish(sc);
                        break;
                    case "MENU":
                        allMenu(sc);
                        break;
                    case "FIND DISH":
                        findDish(sc);
                        break;
                    case "SELECT DISHES":
                        selectDish(sc);
                        break;
                }
            }
        }
    }

    public static void allMenu(Scanner sc) {
        List<Dish> dishesList = dishInterface.findAll();
        printDishes(dishesList, "Our menu:");
    }

    public static void addDish(Scanner sc) {
        Dish dish = new Dish();
        System.out.println("Enter dish name:");
        dish.setName(sc.nextLine());
        System.out.println("Enter dish price:");
        dish.setPrice(Double.parseDouble(sc.nextLine()));
        System.out.println("Enter dish weight:");
        dish.setWeight(Double.parseDouble(sc.nextLine()));
        System.out.println("Enter discount:");
        dish.setDiscount(Double.parseDouble(sc.nextLine()));
        dishInterface.addDish(dish);
    }

    public static void findDish(Scanner sc) {
        System.out.println("Enter field and value for search, please:");
        System.out.print("Field name: ");
        String field = sc.nextLine();
        System.out.print(" value: ");
        String value = sc.nextLine();
        List<Dish> results = dishInterface.findByField(field, value);
    }


    public static void printDishes(List<Dish> dishesList, String message){
        System.out.println(message);
        for (Dish dish : dishesList)
            System.out.println(dish.toString());
    }

    public static void selectDish(Scanner sc) {
        while (true) {
            System.out.println("\n select dishes by price - > price");
            System.out.println("select dishes by discount - > discount");
            System.out.println("back to menu - > back");
            String command = sc.nextLine();
            Double price1, price2;
            List<Dish> dishes = new ArrayList<>();
            switch (command) {
                case "price":
                    System.out.print("Price from:");
                    price1 = Double.parseDouble(sc.nextLine());
                    System.out.print("Price to:");
                    price2 = Double.parseDouble(sc.nextLine());
                    dishes = dishInterface.findByPrice(price1, price2);
                    printDishes(dishes,"");
                    break;
                case "discount":
                    System.out.println("Only with discount? -> yes/no");
                    if (sc.nextLine().equals("yes")) {
                        dishes = dishInterface.findByDiscont();
                        printDishes(dishes,"");
                    }
                    break;
                default:
                    System.out.println("Back to menu? -> yes/no");
                    if (sc.nextLine().equals("yes"))
                        return;
            }
            if (dishes.size() > 0) {
                System.out.println("Add selected dishes to set? -> yes/no");
                if (sc.nextLine().equals("yes")) {
                    Double totalWeight = dishInterface.getWeightCount(dishes) + dishInterface.getWeightCount(selectedDishes);
                    if (totalWeight < 1000) {
                        for (Dish dish : dishes)
                            selectedDishes.add(dish);
                        printDishes(selectedDishes,"Selected dishes:");
                        System.out.println("The total weight is " + totalWeight);
                    } else {
                        System.out.println("\n" + "The total weight of the food should be less than 1 kg");
                    }
                }
            }
        }
    }

}

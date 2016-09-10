package entities;

import java.util.List;

public interface IDishDao {
    public void addDish(Dish dish);
    public List<Dish> findAll();
    public List<Dish> findByField(String field, String value);
    public List<Dish> findByDiscont();
    public List<Dish> findByPrice(Double from, Double to);
    public double getWeightCount(List<Dish> dishesList);
}

package entities;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

public class DishDao implements IDishDao {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
    static EntityManager em = emf.createEntityManager();
    static CriteriaBuilder cb = em.getCriteriaBuilder();
    static Metamodel model = em.getMetamodel();

    public void addDish(Dish dish) {
        em.getTransaction().begin();
        try {
            em.persist(dish);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            em.getTransaction().rollback();
            return;
        }
    }

    public List<Dish> findByPrice(Double from, Double to) {
        EntityType<Dish> dishType = model.entity(Dish.class);
        CriteriaQuery<Dish> criteriaQuery = cb.createQuery(Dish.class);
        Root<Dish> dish = criteriaQuery.from(dishType);
        Expression<Double> field = dish.get("price");
        criteriaQuery.select(dish).where(cb.between(field, from, to));
        TypedQuery<Dish> q = em.createQuery(criteriaQuery);
        return q.getResultList();
    }

    public List<Dish> findByDiscont() {
        EntityType<Dish> dishType = model.entity(Dish.class);
        CriteriaQuery<Dish> criteriaQuery = cb.createQuery(Dish.class);
        Root<Dish> dish = criteriaQuery.from(dishType);
        Expression<Double> field = dish.get("discount");
        criteriaQuery.select(dish).where(cb.gt(field,0),cb.isNotNull(field));
        TypedQuery<Dish> q = em.createQuery(criteriaQuery);
        return q.getResultList();
    }

    public List<Dish> findByField(String field, String value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Metamodel m = em.getMetamodel();
        EntityType<Dish> dishType = m.entity(Dish.class);
        CriteriaQuery<Dish> criteriaQuery = cb.createQuery(Dish.class);
        Root<Dish> dish = criteriaQuery.from(dishType);
        criteriaQuery.where(cb.equal(dish.get(field), value));
        TypedQuery<Dish> q = em.createQuery(criteriaQuery);
        return q.getResultList();
    }

    public double getWeightCount(List<Dish> dishesList) {
        double weight = 0;
        if (dishesList.size() > 0)
            for (Dish dish : dishesList)
                weight += dish.getWeight();
        return weight;
    }

    public List<Dish> findAll() {
        Query query = em.createNamedQuery("Dish.findAll", Dish.class);
        List<Dish> dishesList = query.getResultList();
        return dishesList;
    }
}

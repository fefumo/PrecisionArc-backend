package se.ifmo.services;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.ifmo.dto.PointRequest;
import se.ifmo.models.Point;
import se.ifmo.models.Users;
import se.ifmo.util.AreaChecker;

@Stateless
public class PointService {

    @PersistenceContext
    private EntityManager entityManager;

    public Point savePoint(PointRequest pointRequest, Users user) {
        // Валидируем точку
        boolean result = AreaChecker.isInArea(pointRequest.getxCoord(), pointRequest.getyCoord(), pointRequest.getRadius());

        Point point = new Point();
        point.setxCoord(pointRequest.getxCoord());
        point.setyCoord(pointRequest.getyCoord());
        point.setRadius(pointRequest.getRadius());
        point.setResult(result);
        point.setUser(user);

        entityManager.persist(point);

        return point; 
    }

    public List<Point> getUserPoints(Users user) {
        return entityManager.createQuery(
                "SELECT p FROM Point p WHERE p.user = :user", Point.class)
                .setParameter("user", user)
                .getResultList();
    }
    
}

package se.ifmo.services;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.ifmo.dto.PointRequest;
import se.ifmo.entities.Point;
import se.ifmo.entities.Users;
import se.ifmo.util.AreaChecker;

@Stateless
public class PointService {

    @PersistenceContext
    private EntityManager entityManager;

    public Point savePoint(PointRequest pointRequest, Users user, long elapsedTime) {
        // Валидируем точку
        boolean result = AreaChecker.isInArea(pointRequest.getX(), pointRequest.getY(), pointRequest.getR());

        Point point = new Point();
        point.setX(pointRequest.getX());
        point.setY(pointRequest.getY());
        point.setR(pointRequest.getR());
        point.setResult(result);
        point.setElapsedTime(elapsedTime);
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

    public void deleteUserPoints(Users user) {
        entityManager.createQuery(
            "DELETE FROM Point p WHERE p.user.id = :userId"
        )
        .setParameter("userId", user.getId())
        .executeUpdate();
    }    
    
}

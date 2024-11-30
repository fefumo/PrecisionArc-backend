package se.ifmo.ejb;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import se.ifmo.model.User;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public User findUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                     .setParameter("username", username)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public boolean registerUser(String username, String passwordHash) {
        if (findUserByUsername(username) != null) {
            return false; // Пользователь с таким именем уже существует
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        em.persist(user);
        return true;
    }
}

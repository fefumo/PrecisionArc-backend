package se.ifmo.ejb;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import se.ifmo.models.Users;
import se.ifmo.util.PasswordHasher;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public Users findUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class)
                     .setParameter("username", username)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public boolean registerUser(String username, String password) {
        if (findUserByUsername(username) != null) {
            return false; // Пользователь с таким именем уже существует
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPasswordHash(PasswordHasher.hashPassword(password));
        em.persist(user);
        return true;
    }

    @Transactional
    public Users authenticate(String username, String password) {
        Users user = findUserByUsername(username);
        if (user != null && PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
    
}


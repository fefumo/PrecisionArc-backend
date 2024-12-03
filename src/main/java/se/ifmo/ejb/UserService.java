package se.ifmo.ejb;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import se.ifmo.model.Users;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public Users findUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", Users.class)
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

        Users user = new Users();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        em.persist(user);
        return true;
    }

    public Users authenticate(String username, String password) {
        Users user = findUserByUsername(username);
        if (user != null && verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }


    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    
}

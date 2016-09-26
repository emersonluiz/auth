package br.com.emersonluiz.repository;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.emersonluiz.model.User;
import br.com.emersonluiz.util.StatusException;

@Named
@Transactional
public class DefaultUserRepository implements UserRepository {

    @PersistenceContext
    public EntityManager entityManager;
    private static final Logger logger  = LoggerFactory.getLogger(DefaultUserRepository.class);

    @Override
    public User findOne(String userName, String password) {
        try {
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password");
            query.setParameter("userName", userName);
            query.setParameter("password", password);
            @SuppressWarnings("unchecked")
            List<User> list = query.getResultList();
            logger.debug("User was found successfully.");
            if (list != null) {
                if (list.size() > 0) {
                    return list.get(0);
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public User findOne(String id) throws Exception {
        try {
            User user = entityManager.find(User.class, id);
            if (user == null) {
                logger.error("Id departament : "+id+" was not found.");
                throw new StatusException(Status.NOT_FOUND.getStatusCode(), "Id user: "+id+" was not found.");
            }
            logger.debug("User was found successfully.");
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}

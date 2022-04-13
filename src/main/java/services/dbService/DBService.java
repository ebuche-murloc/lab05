package services.dbService;

import model.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import services.dbService.UsersDAO;
import services.dbService.UsersDataSet;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private static SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getConfiguration();
        sessionFactory = getSessionFactory(configuration);
    }

    public boolean checkUserExists(String login) {
        Session session = sessionFactory.openSession();
        boolean userExists = new UsersDAO(session).checkUserExists(login);
        session.close();
        return userExists;
    }

    public UserProfile getUser(String login) {
        Session session = sessionFactory.openSession();
        UserProfile userProfile = new UsersDAO(session).getUserProfile(login);
        session.close();
        return userProfile;
    }

    public void addUser(UserProfile userProfile) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        dao.insertUser(userProfile);
        transaction.commit();
        session.close();
    }

    private static SessionFactory getSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url",
                "jdbc:mysql://localhost:3306/users?user=root&password=root&serverTimezone=UTC");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }
}
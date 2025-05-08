package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean register(String username, String password) {
        if (username == null || username.length() < 3 || password == null || password.length() < 3) {
            return false; // Возвращаем false, если длина любого из значений меньше 3 символов
        }
        try {
            User user = new User();
            user.setUsername(username);
            String hashedPassword = hashPassword(password);
            user.setPassword(hashedPassword);
            entityManager.persist(user);
            entityManager.flush();  // Это гарантирует, что если с username что-то не так (например, он не уникален), будет выброшено исключение
            return true;
        } catch (PersistenceException e) {
            // Это может быть связано с нарушением уникальности или другой проблемой при сохранении
            return false;
        }
    }

    @Override
    public boolean login(String username, String password) {
        String hashedPassword = hashPassword(password);
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.password = :password", Long.class)
                .setParameter("username", username)
                .setParameter("password", hashedPassword)
                .getSingleResult();
        return count > 0; // Возвращает true, если пользователь с такими учетными данными найден
    }


    /**
     * Проверяет, существует ли уже пользователь с данным именем.
     *
     * @param username Имя пользователя для проверки
     * @return true, если пользователь с таким именем существует, иначе false.
     */
    @Override
    public int doesUsernameExist(String username) {
        if (username == null || username.length() < 3) {
            return -1; // Возвращаем -1, указывая на недопустимую длину имени пользователя
        }
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0 ? 1 : 0; // Возвращает 1, если имя пользователя существует, 0 - если нет
    }

    // Метод для хеширования пароля с использованием SHA-256
    public static String hashPassword(String password) {
        try {
            // Получение экземпляра MessageDigest с алгоритмом SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Преобразование пароля в массив байтов и хеширование его
            byte[] hashedBytes = md.digest(password.getBytes());

            // Преобразование массива байтов в шестнадцатеричную строку
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Обработка ошибки, если алгоритм хеширования не найден
            e.printStackTrace();
            return null;
        }
    }
}

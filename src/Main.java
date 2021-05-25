import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Напишите программу, которая будет взаимодействовать с API https://jsonplaceholder.typicode.com.
 * Можно пользоваться стандартными возможностями Java (HttpURLConnection),
 * либо познакомиться самостоятельно со сторонними библиотеками (Apache Fluent API, Apache HTTPClient, Jsoup).
 *
 * Задание 1#
 * Программа должна содержать методы для реализации следующего функционала:
 *    1.1 создание нового объекта в https://jsonplaceholder.typicode.com/users.
 * Возможно, вы не увидите обновлений на сайте.
 * Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON,
 * но с полем id со значением на 1 больше, чем самый большой id на сайте.
 *    1.2 обновление объекта в https://jsonplaceholder.typicode.com/users.
 *        Возможно, обновления не будут видны на сайте напрямую.
 *        Будем считать, что метод работает правильно,
 *        если в ответ на запрос придет обновленный JSON (он должен быть точно таким же, какой вы отправляли).
 *    1.3 удаление объекта из https://jsonplaceholder.typicode.com/users.
 *        Здесь будем считать корректным результат - статус из группы 2хх в ответ на запрос.
 *    1.4 получение информации обо всех пользователях https://jsonplaceholder.typicode.com/users
 *    1.5 получение информации о пользователе с определенным id https://jsonplaceholder.typicode.com/users/{id}
 *    1.6 получение информации о пользователе с опредленным username - https://jsonplaceholder.typicode.com/users?username={username}
 *
 * Задание 2#
 * Дополните программу методом, который будет выводить все комментарии к последнему посту определенного пользователя и записывать их в файл.
 * https://jsonplaceholder.typicode.com/users/1/posts Последним будем считать пост с наибольшим id.
 * https://jsonplaceholder.typicode.com/posts/10/comments
 * Файл должен называться "user-X-post-Y-comments.json", где Х - номер пользователя, Y - номер поста.
 *
 * Задание 3#
 * Дополните программу методом, который будет выводить все открытые задачи для пользователя Х.
 * https://jsonplaceholder.typicode.com/users/1/todos.
 * Открытыми считаются все задачи, у которых completed = false.
 */
public class Main {

    private static final int CUSTOMER_ID = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
        //Задание 1
        //1.1 создание нового объекта
        User newUser = createNewUser();
        User createdUser = HttpUtil.sendPost(newUser);
        System.out.println("Задание 1");
        System.out.println("1.1 создание нового объекта - " + createdUser);
        System.out.println("*".repeat(21));

        //1.2 обновление объекта
        System.out.println("1.2 обновление объекта");
        User user = HttpUtil.sendGet(CUSTOMER_ID);
        System.out.println("user before update " + user);
        user.setName("Nikolay");
        user.setUsername("Kolynich");
        user = HttpUtil.sendPut(user.getId(), user);
        System.out.println("user after update " + user);
        System.out.println("*".repeat(21));

        //1.3 удаление объекта
        int statusResponse = HttpUtil.sendDelete(CUSTOMER_ID);
        System.out.println("1.3 удаление объекта");
        System.out.println("Status of deleting : " + statusResponse);
        System.out.println("*".repeat(21));

        //1.4 получение информации обо всех пользователях
        List<User> users = HttpUtil.sendGetListAllUsers();
        System.out.println("1.4 получение информации обо всех пользователях");
        users.forEach(System.out::println);
        System.out.println("*".repeat(21));

        //1.5 получение информации о пользователе с определенным id
        User userById = HttpUtil.sendGetUserById(CUSTOMER_ID);
        System.out.println("1.5 получение информации о пользователе с определенным id");
        System.out.println(" User info with id " + CUSTOMER_ID + " - " + userById);
        System.out.println("*".repeat(21));

        //1.6 получение информации о пользователе с опредленным username
        User user1 = HttpUtil.sendGetUserByName(userById.getName());
        System.out.println("1.6 получение информации о пользователе с опредленным username");
        System.out.println("User info with name "+ userById.getName() + user1);
        System.out.println("*".repeat(21));
        System.out.println("*".repeat(21));

        //Задание 2
        System.out.println("Задание 2");

        String allCommentsOfLastPost = HttpUtil.getAllCommentsOfLastPost(user);
        System.out.println(allCommentsOfLastPost);
        System.out.println("*".repeat(21));
        System.out.println("*".repeat(21));

        //Задание 3
        System.out.println("Задание 3");

        List<Task> allOpenedTask = HttpUtil.getAllOpenedTask(user);
        allOpenedTask.forEach(System.out::println);
    }

    private static User createNewUser(){
        return new User.Builder()
                    .name("Denis")
                    .username("Denchil")
                    .email("denis@denis.com")
                    .address(new Address.Builder()
                            .street("Lenina")
                            .suite("12")
                            .city("Dnepr")
                            .zipcode("49000")
                            .geo(new Geo.Builder()
                                    .lat(-12.555)
                                    .lng(55.121)
                                    .build())
                            .build())
                    .phone("055-555-55-55")
                    .website("goit.com")
                    .company(new Company.Builder()
                            .name("Epam")
                            .catchPhrase("HelloWorld")
                            .bs("bs")
                            .build())
                    .build();
        }


}

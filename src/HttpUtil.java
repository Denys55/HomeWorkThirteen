import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.sound.midi.Patch;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {

    public static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static final Gson GSON = new Gson();
    public static final String HOST = "https://jsonplaceholder.typicode.com;";
    public static final String END_POINT_USERS = "/users";
    public static final String GET_USER_NAME = "?users?username";
    public static final String POST = "/posts";
    public static final String COMMENTS = "/comments";
    public static final String TODOS = "/todos";

    public static User sendPost(User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", HOST, END_POINT_USERS, user)))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(),User.class);
    }

    public static User sendPut(Integer id, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .header("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(),User.class);
    }

    public static User sendGet(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static int sendDelete(int id) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", HOST, END_POINT_USERS, id)))
                .header("Content-type", "application/json")
                .DELETE()
                .build();

        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> sendGetListAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", HOST, END_POINT_USERS)))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> users = GSON.fromJson(response.body(), new TypeToken<List<User>>(){}.getType());
        return users;
    }

    public static User sendGetUserById(Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", HOST, END_POINT_USERS, id)))
                .header("Content-type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(),User.class);
    }

    public static User sendGetUserByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s%s%s", HOST, END_POINT_USERS, GET_USER_NAME, name)))
                .header("Content-type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(),User.class);
    }

    public static Post getLastPost(User user) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(String.format("%s%s/%d%s", HOST, END_POINT_USERS, user.getId(), POST))))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Post> posts = GSON.fromJson(response.body(),new TypeToken<List<Post>>(){}.getType());
        return Collections.max(posts, Comparator.comparingInt(Post::getId));
    }

    public static String getAllCommentsOfLastPost(User user) throws IOException, InterruptedException {
        Post lastPost = getLastPost(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", HOST, POST, lastPost.getId(), COMMENTS)))
                .GET()
                .build();

        String fileName = "user-"+ user.getId() + "- post-" + lastPost.getId() +"-comments.json";
        HttpResponse<Path> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
        return "File with comments " + response.body();
    }

    public static List<Task> getAllOpenedTask (User user) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d%s", HOST, END_POINT_USERS, user.getId(), TODOS)))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> allTasks = GSON.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        return allTasks.stream()
                .filter(todo -> !todo.isCompleted())
                .collect(Collectors.toList());
    }
}



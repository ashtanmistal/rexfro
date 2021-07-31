package persistence;

import model.Queue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public Queue read() throws IOException {

        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseQueue(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    private Queue parseQueue(JSONObject jsonObject) {
        Queue queue = new Queue();
        addElements(queue, jsonObject);
        return queue;
    }

    private void addElements(Queue queue, JSONObject jsonObject) {
        JSONArray jsonArrayFind = jsonObject.getJSONArray("Find operations");
        JSONArray jsonArrayReplace = jsonObject.getJSONArray("Replace operations");
        JSONArray jsonArrayBool = jsonObject.getJSONArray("Replace All operations");
        for (Object json : jsonArrayFind) {
            queue.addToFindQueue(json.toString());
        }

        for (Object json : jsonArrayReplace) {
            queue.addToReplaceQueue(json.toString());
        }

        for (Object json : jsonArrayBool) {
            queue.addToReplaceAllQueue(json.toString());
        }
    }

}

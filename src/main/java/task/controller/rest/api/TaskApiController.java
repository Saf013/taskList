package task.controller.rest.api;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import task.controller.dto.TaskListDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskApiController {

    private List<Map<String, Boolean>> taskList = new ArrayList<>();

    @PostMapping(value = "/add-task", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addTask(@RequestBody TaskListDto listDto) {
        JSONObject jsonObject = new JSONObject();
        if (listDto.getTask().isEmpty()) {
            jsonObject.put("status", 403);
        } else {
            Map<String, Boolean> list = new HashMap<>();
            list.put(listDto.getTask(), listDto.isFlag());
            taskList.add(list);
            jsonObject.put("status", 200);
        }
        return jsonObject.toString();
    }

    @GetMapping(value = "/show/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Boolean>> showTaskList() {
        return taskList;
    }

    @GetMapping(value = "/find/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Boolean> findById(@PathVariable("taskId") int id) {
        return taskList.get(id);
    }

    @DeleteMapping(value = "/delete/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Boolean> deleteTaskById(@PathVariable("taskId") int id) {
        return taskList.remove(id);
    }

}

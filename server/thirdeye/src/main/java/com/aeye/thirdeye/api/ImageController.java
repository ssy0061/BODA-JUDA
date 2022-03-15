package com.aeye.thirdeye.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final RestTemplate restTemplate;

    @PostMapping("/image/test")
    public ResponseEntity<?> test(@RequestPart(value = "image", required = false) MultipartFile file,
                                  @RequestBody Map<String, String> input){

        return null;
    }

    @PostMapping("/actions")
    public ResponseEntity<?> testtest(){
        // "{" +                 "}";
        String strJson =
                "{\n" +
                "  \"attachments\": [\n" +
                "    {\n" +
                "      \"pretext\": \"This is the attachment pretext.\",\n" +
                "      \"text\": \"This is the attachment text.\",\n" +
                "      \"actions\": [\n" +
                "        {\n" +
                "          \"id\": \"message\",\n" +
                "          \"name\": \"Ephemeral Message\",\n" +
                "          \"integration\": {\n" +
                "            \"url\": \"http://localhost:8080/actions/test1\",\n" +
                "            \"context\": {\n" +
                "              \"action\": \"do_something_ephemeral\"\n" +
                "            }\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"id\": \"update\",\n" +
                "          \"name\": \"Update\",\n" +
                "          \"integration\": {\n" +
                "            \"url\": \"http://localhost:8080/actions/test2\",\n" +
                "            \"context\": {\n" +
                "              \"action\": \"do_something_update\"         \n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(strJson);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> entity = new HttpEntity<>(strJson, headers);
            restTemplate.postForEntity("https://meeting.ssafy.com/hooks/3mwi6urfribuf8hbfnzwh17knw", entity, String.class);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.OK).body("크허허허허");
    }

    @PostMapping("/actions/test1")
    public ResponseEntity<?> test1(){
        System.out.println("test1test1");
        return ResponseEntity.status(HttpStatus.OK).body("크허허허허");
    }

    @PostMapping("/actions/test2")
    public ResponseEntity<?> test2(){
        System.out.println("test2test2");

        return null;
    }


}

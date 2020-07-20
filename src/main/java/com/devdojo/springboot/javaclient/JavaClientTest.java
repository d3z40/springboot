package com.devdojo.springboot.javaclient;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaClientTest {
    public static void main(String[] args) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String userName = "bruce.wayne";
        String password = "batman";

        try {
            URL url = new URL("http://localhost:8080/v1/user/students/5");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String basicGenerated = JavaClientTest.encodeUserNamePassword(userName, password);
            System.out.println(basicGenerated);

            connection.addRequestProperty("Authorization", "Basic " + basicGenerated);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder jsonB = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonB.append(line);
            }

            System.out.println(jsonB);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            if (connection != null)
                connection.disconnect();
        }
    }

    public static String encodeUserNamePassword(String userName, String password) {
        String userPassword = userName + ":" + password;

        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }
}

package system.notification;

import java.io.*;
import java.util.Date;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.UnknownHostException;
import javax.net.ssl.SSLPeerUnverifiedException;

public class LINENotify {
    private final String token;

    public LINENotify(String token) {
        this.token = token;
    }

    public boolean sendMessage(String message) {
        boolean result = false;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL("https://notify-api.line.me/api/notify");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "Bearer " + token);

            OutputStream os = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            writer.print("message=\n" + message + "\n" + (new Date()).toString());
            writer.close();
            connection.connect();

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                result = true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public boolean verifyAccessToken() {
        boolean result = false;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL("https://notify-api.line.me/api/status");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", "Bearer " + token);
            connection.connect();

            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                result = true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
}
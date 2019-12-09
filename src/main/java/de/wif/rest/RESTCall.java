package de.wif.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a REST HTTP call.
 */
abstract public class RESTCall {

    // HTTP verbs go here
    final static String GET  = "GET";
    final static String POST = "POST";

    private URL baseAddress;
    private Gson gson;

    /**
     * Creates an instance of RESTCall.
     * @param base Base URL of the endpoint
     */
    public RESTCall(URL base) {
        this.baseAddress = base;
        this.gson = new Gson();
    }

    /**
     * Executes the call and returns the T of requires class.
     * @param method HTTP verb (GET, POST, ...)
     * @param path Path relative to base address
     * @param tClass Class to expect
     * @param <T> Returned type
     * @return Returns an object of T if successful
     */
    protected <T> T call(String method, String path, Class<T> tClass) {
        try {
            URL url = new URL(baseAddress.toString() + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.connect();
            int status = con.getResponseCode();
            StringBuffer content = new StringBuffer();
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
            con.disconnect();

            T obj =  gson.fromJson(content.toString(), tClass);
            return obj;
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Executes the call and returns the T of requires class.
     * @param method HTTP verb (GET, POST, ...)
     * @param path Path relative to base address
     * @param tClass Class to expect
     * @param <T> Returned type
     * @return Returns an object of T if successful
     */
    protected <T,R> T call(String method, String path, R msg,  Class<T> tClass) {
        try {
            URL url = new URL(baseAddress.toString() + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);

            if (method.equals(POST)) {
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String body = gson.toJson(msg);
                con.setRequestProperty("Content-Length", String.valueOf(body.getBytes("UTF-8").length));
                con.setDoOutput(true);
                con.getOutputStream().write(body.getBytes("UTF-8"));
            }

            con.connect();
            int status = con.getResponseCode();
            StringBuffer content = new StringBuffer();
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
            con.disconnect();

            T obj =  gson.fromJson(content.toString(), tClass);
            return obj;
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    /**
     * Executes the call and returns the T of requires class.
     * @param method HTTP verb (GET, POST, ...)
     * @param path Path relative to base address
     * @param <T> Returned type
     * @return Returns an object of T if successful
     */
    protected <T> List<T> call(String method, String path) {
        try {
            URL url = new URL(baseAddress.toString() + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.connect();
            int status = con.getResponseCode();
            StringBuffer content = new StringBuffer();
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }
            con.disconnect();

            Type listType = new TypeToken<ArrayList<T>>(){}.getType();
            List<T> objs =  gson.fromJson(content.toString(), listType);
            return objs;
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
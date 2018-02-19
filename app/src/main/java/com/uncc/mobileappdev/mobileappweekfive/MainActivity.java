package com.uncc.mobileappdev.mobileappweekfive;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()) {
                    //new GetAsyncData().execute("http://api.theappsdr.com/json/");
                    //new GetSAXAsyncData().execute("http://api.theappsdr.com/xml/");
                    new GetPullAsyncData().execute("http://api.theappsdr.com/xml/");
                }
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetAsyncData extends AsyncTask <String, Void, ArrayList<Person>>{

        @Override
        protected ArrayList<Person> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Person> result = new ArrayList<>();

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray persons = root.getJSONArray("persons");
                    for(int i=0; i < persons.length(); i++){
                        JSONObject personObject = persons.getJSONObject(i);

                        Person person = new Person();
                        person.setName(personObject.getString("name"));
                        person.setId(personObject.getLong("id"));
                        person.setAge(personObject.getInt("age"));

                        JSONObject addressObject = personObject.getJSONObject("address");

                        Address address = new Address();
                        address.setLine1(addressObject.getString("line1"));
                        address.setCity(addressObject.getString("city"));
                        address.setState(addressObject.getString("state"));
                        address.setZip(addressObject.getString("zip"));

                        person.setAddress(address);
                        result.add(person);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected  void onPostExecute(ArrayList<Person> result){
            if(result != null && !result.isEmpty()){
                for(Person p : result){
                    Log.d("Demo", p.getName());
                }
            } else {
                Log.d("Demo", "NULL result!");
            }
        }

    }

    private class GetSAXAsyncData extends AsyncTask <String, Void, ArrayList<Person>>{

        @Override
        protected ArrayList<Person> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Person> result = null;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    result = PersonsParser.PersonsPullParser.parsePersons(connection.getInputStream());

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected  void onPostExecute(ArrayList<Person> result){
            if(result != null && !result.isEmpty()){
                    Log.d("Demo", result.toString());
            } else {
                Log.d("Demo", "NULL result!");
            }
        }

    }

    private class GetPullAsyncData extends AsyncTask <String, Void, ArrayList<Person>>{

        @Override
        protected ArrayList<Person> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Person> result = null;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    result = PersonsParser.PersonsSAXParser.parsePersons(connection.getInputStream());

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected  void onPostExecute(ArrayList<Person> result){
            if(result != null && !result.isEmpty()){
                Log.d("Demo", result.toString());
            } else {
                Log.d("Demo", "NULL result!");
            }
        }

    }

}

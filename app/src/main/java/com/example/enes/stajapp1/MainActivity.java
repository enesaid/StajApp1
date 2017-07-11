package com.example.enes.stajapp1;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static EditText emailEditText;
    public static EditText passEditText;
    ProgressDialog pd;
    Gson gsonTo;
    Credantial obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);


    }

    public void checkLogin(View view) throws MalformedURLException {

       String email = emailEditText.getText().toString();
       String pass = passEditText.getText().toString();

        /*


        if (!isValidEmail(email)) {

            emailEditText.setError("Geçersiz Email");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {

            passEditText.setError("Şifre Boş olamaz...");
        }

        if(isValidEmail(email) && isValidPassword(pass))
        {
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("bilgi",email);
            startActivity(intent);
        }
        */
        if (email.equals(""))
        {
            emailEditText.setError("Bu alan boş bırakılamaz");
        }



        gsonTo = new Gson();
        obj = new Credantial(email,pass);
        gsonTo.toJson(obj);
       // String jsonInString = gson.toJson(obj);
        //http://192.168.2.245:4043/api/userinfo

        URL url = new URL("http://169.254.36.12:4043/api/userinfo");
        new Task().execute(url);


    }

 /*   // validating email id
    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }
    */

    private class Task extends AsyncTask<URL,Void,String>{

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Bağlanıyor");
            pd.setMessage("Login için API kullanılıyor.");
            pd.setIndeterminate(false);
            pd.show();


        }

        @Override
        protected String doInBackground(URL... params) {


            try {

                HttpURLConnection urlConnection = (HttpURLConnection) params[0].openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);



                try {

                    BufferedWriter bufwriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));
                    String jsonInString = gsonTo.toJson(obj);
                    bufwriter.write(jsonInString);
                    bufwriter.flush();
                    bufwriter.close();


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    if(urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }

                }
            }
            catch(Exception e) {

                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response == null)
            {
                response = "THERE WAS AN ERROR";
            }

            Gson gsonFrom = new Gson();
            AuthResult auth = gsonFrom.fromJson(response,AuthResult.class);// JSON to Java object, read it from a Json String.

            if (AuthResult.getErrorCode == 1)
            {
                Toast.makeText(MainActivity.this,"Kullanıcı girişi yapıldı",Toast.LENGTH_SHORT).show();
            }
            else if (AuthResult.getErrorCode == 0)
            {
                Toast.makeText(MainActivity.this,"Kullanıcı girişi yapılamadı",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MainActivity.this,"Bağlantı kurulamadı",Toast.LENGTH_SHORT).show();

            /*
            JsonElement json = gsonFrom.fromJson(response,JsonElement.class); //// JSON to JsonElement, convert to String later.
            String sonuc = gsonFrom.toJson(json);
            if (sonuc != sonuc)
            {
                Toast.makeText(MainActivity.this,sonuc,Toast.LENGTH_SHORT).show();
            }
            */

           pd.hide();
            Log.i("INFO", response);

        }
    }
}
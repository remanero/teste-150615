package com.nutrichecker.br.nutrichecker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Resgister extends ActionBarActivity implements View.OnClickListener{

    Button bRegister;
    EditText etName, etUsername, etPassword;
    TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        tvLoginLink = (TextView)findViewById(R.id.tvLoginLink);

        bRegister.setOnClickListener(this);
        tvLoginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:
                /*
                String nome = etName.getText().toString();
                String email = etUsername.getText().toString();
                String senha = etPassword.getText().toString();

                Usuario usuario = new Usuario(nome, email, senha);

                registerUsuario(usuario);
                */

                //new HttpAsyncTask().execute("http://10.0.0.102:8080//spring/service/usuario/registerLogin");
                new HttpAsyncTask().execute("http://192.168.0.100:8080//spring/service/usuario/registerLogin");

                break;
            case R.id.tvLoginLink:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

    private void registerUsuario(Usuario usuario) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUsuarioDataInBackground(usuario, new GetUsuarioCallBack() {
            @Override
            public void done(Usuario returnedUsuario) {
                startActivity(new Intent(Resgister.this, Login.class));
            }
        });

    }

    Usuario usuario;

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            usuario = new Usuario();
            usuario.setNome(etName.getText().toString());
            usuario.setEmail(etUsername.getText().toString());
            usuario.setSenha(etPassword.getText().toString());

            //return POST(urls[0],usuario);

            String test = POST(urls[0],usuario);

            startActivity(new Intent(Resgister.this, Login.class));

            return  test;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    public static String POST(String url, Usuario usuario){
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("nome", usuario.getNome());
            jsonObject.accumulate("email", usuario.getEmail());
            jsonObject.accumulate("senha", usuario.getSenha());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.i("JSON", jsonObject.toString());
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Ocorreu um erro!!!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }
}

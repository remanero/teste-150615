package com.nutrichecker.br.nutrichecker;

import android.app.AlertDialog;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Login extends ActionBarActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;
    UsuarioLocalStore usuarioLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        usuarioLocalStore = new UsuarioLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                /*
                String nome = etUsername.getText().toString();
                String senha = etPassword.getText().toString();
                Usuario usuario = new Usuario(nome,senha);
                autheticate(usuario);
                */

                //new HttpAsyncTask().execute("http://10.0.0.102:8080/spring/service/usuario/doLoginEmail");
                new HttpAsyncTask().execute("http://192.168.0.100:8080/spring/service/usuario/doLoginEmail");

                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Resgister.class));
                break;
        }
    }

    private void autheticate(Usuario usuario) {
        ServerRequests serverRequests =  new ServerRequests(this);
        serverRequests.fetchUsuarioDataInBackground(usuario, new GetUsuarioCallBack() {
            @Override
            public void done(Usuario returnedUsuario) {
                if (returnedUsuario == null) {
                    showErrorMessage();
                } else {
                    logUsuarioIn(returnedUsuario);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Incorreto usuario details");
        builder.setPositiveButton("Ok", null);
        builder.show();
    }

    private void logUsuarioIn(Usuario returnedUsuario) {
        usuarioLocalStore.storeUsuarioData(returnedUsuario);
        usuarioLocalStore.setUsuarioLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
    }

    Usuario usuario;
    //private class HttpAsyncTask extends AsyncTask<String, Void, String> {
    private class HttpAsyncTask extends AsyncTask<String, Void, Usuario> {
        @Override
        //protected String doInBackground(String... urls) {
        protected Usuario doInBackground(String... urls) {

            usuario = new Usuario();
            usuario.setNome(etUsername.getText().toString());
            usuario.setSenha(etPassword.getText().toString());

            Usuario userTest = POST(urls[0],usuario);
            Log.i("teste", userTest.getNome());
            if (userTest == null) {
                showErrorMessage();
            } else {
                logUsuarioIn(userTest);
            }

            return userTest;

            //return POST(urls[0],usuario);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        //protected void onPostExecute(String result) {
        protected void onPostExecute(Usuario result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

        }
    }

    //public String POST(String url, Usuario usuario){
    public Usuario POST(String url, Usuario usuario){
        InputStream inputStream = null;
        Usuario returnedUsuario = null;
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


            HttpEntity entity = httpResponse.getEntity();
            String result2 = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result2);
            Log.i("json", jObject.toString());
            if(jObject.length() == 0) {
                returnedUsuario = null;
            } else {
                String email = jObject.getString("email");
                returnedUsuario = new Usuario(usuario.nome, email, usuario.senha);
            }


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
        //return  result;
        return returnedUsuario;
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

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

    private Boolean camposValidate(){
        if(etUsername.getText().length() > 0 && etPassword.getText().length() > 0){
            return true;
        } else {
            return  false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                if(camposValidate()) {
                    new HttpAsyncTask().execute(ServerRequests.SERVER_ADRESS + "usuario/doLoginEmail");
                } else {
                    Toast.makeText(getBaseContext(), "Preencha os campos obrigatorios", Toast.LENGTH_LONG).show();
                }
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
    private class HttpAsyncTask extends AsyncTask<String, Void, Usuario> {
        @Override
        protected Usuario doInBackground(String... urls) {

            usuario = new Usuario();
            usuario.setNome(etUsername.getText().toString());
            usuario.setSenha(etPassword.getText().toString());

            Usuario userTest = POST(urls[0],usuario);

            if (userTest == null) {
                showErrorMessage();
            } else {
                logUsuarioIn(userTest);
            }
            return userTest;
        }

        @Override
        protected void onPostExecute(Usuario result) {
            Toast.makeText(getBaseContext(), "Usuário encontrado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    public Usuario POST(String url, Usuario usuario){
        InputStream inputStream = null;
        Usuario returnedUsuario = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("nome", usuario.getNome());
            jsonObject.accumulate("senha", usuario.getSenha());

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);

            HttpEntity entity = httpResponse.getEntity();
            String result2 = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result2);
            if(jObject.length() == 0) {
                returnedUsuario = null;
            } else {
                String email = jObject.getString("email");
                returnedUsuario = new Usuario(usuario.nome, email, usuario.senha);
            }

            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Ocorreu um erro!!!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
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

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
                if(camposValidate()) {
                    new HttpAsyncTask().execute(ServerRequests.SERVER_ADRESS + "usuario/registerLogin");
                } else {
                    Toast.makeText(getBaseContext(), "Preencha os campos obrigatorios", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tvLoginLink:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }

    private Boolean camposValidate(){
        if(etName.getText().length() > 0 && etUsername.getText().length() > 0 && etPassword.getText().length() > 0){
            return true;
        } else {
            return  false;
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

            String test = POST(urls[0],usuario);
            startActivity(new Intent(Resgister.this, Login.class));
            return  test;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Usuario cadastrado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    public static String POST(String url, Usuario usuario){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("nome", usuario.getNome());
            jsonObject.accumulate("email", usuario.getEmail());
            jsonObject.accumulate("senha", usuario.getSenha());

            json = jsonObject.toString();
            Log.i("JSON", jsonObject.toString());

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Ocorreu um erro!!!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
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

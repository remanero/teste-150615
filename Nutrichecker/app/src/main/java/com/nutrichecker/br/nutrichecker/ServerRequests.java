package com.nutrichecker.br.nutrichecker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by renato on 14/06/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIME = 1000 * 15;
    public static final String SERVER_ADRESS = "http://10.0.0.102:8080/spring/service/";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processando");
        progressDialog.setMessage("Por favor, aguarde...");
    }

    public void storeUsuarioDataInBackground(Usuario usuario, GetUsuarioCallBack usuarioCallBack){
        progressDialog.show();
        new StoreUsuarioDataAsyncTask(usuario, usuarioCallBack).execute();
    }

    public void fetchUsuarioDataInBackground(Usuario usuario, GetUsuarioCallBack usuarioCallBack){
        progressDialog.show();
        new fetchUsuarioDataAsyncTask(usuario,usuarioCallBack).execute();
    }

    public class StoreUsuarioDataAsyncTask extends AsyncTask<Void, Void, Void> {

        Usuario usuario;
        GetUsuarioCallBack usuarioCallBack;

        public StoreUsuarioDataAsyncTask(Usuario usuario, GetUsuarioCallBack usuarioCallBack){
            this.usuario = usuario;
            this.usuarioCallBack = usuarioCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("nome",usuario.nome));
            dataToSend.add(new BasicNameValuePair("email",usuario.email));
            dataToSend.add(new BasicNameValuePair("senha", usuario.senha));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADRESS +"usuario/registerLogin");
        
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            usuarioCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchUsuarioDataAsyncTask extends AsyncTask<Void, Void, Usuario> {

        Usuario usuario;
        GetUsuarioCallBack usuarioCallBack;

        public fetchUsuarioDataAsyncTask(Usuario usuario, GetUsuarioCallBack usuarioCallBack) {
            this.usuario = usuario;
            this.usuarioCallBack = usuarioCallBack;
        }

        @Override
        protected Usuario doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("nome",usuario.nome));
            dataToSend.add(new BasicNameValuePair("senha", usuario.senha));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADRESS +"usuario/doLogin");

            Usuario returnedUsuario = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse =  client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                Log.i("json", jObject.toString());

                if(jObject.length() == 0) {
                    returnedUsuario = null;
                } else {
                    String email = jObject.getString("email");

                    returnedUsuario = new Usuario(usuario.nome, email, usuario.senha);
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return returnedUsuario;
        }

        @Override
        protected void onPostExecute(Usuario returnedUsuario) {
            progressDialog.dismiss();
            usuarioCallBack.done(returnedUsuario);
            super.onPostExecute(returnedUsuario);
        }
    }
    }

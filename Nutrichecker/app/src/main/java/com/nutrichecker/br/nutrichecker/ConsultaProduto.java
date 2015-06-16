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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ConsultaProduto extends ActionBarActivity implements View.OnClickListener {

    Button bConsulta;
    EditText etCodigoBarra;
    ProdutoLocalStore produtoLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_produto);

        etCodigoBarra = (EditText)findViewById(R.id.etCodigoBarra);
        bConsulta = (Button)findViewById(R.id.bConsultaTeste);

        bConsulta.setOnClickListener(this);

        produtoLocalStore = new ProdutoLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bConsultaTeste:
                //startActivity(new Intent(this, MainActivity.class));

                //new HttpAsyncTask().execute("http://10.0.0.102:8080/spring/service/produto/consultaProduto");
                new HttpAsyncTask().execute("http://192.168.0.100:8080/spring/service/produto/consultaProduto");

                break;
        }
    }

    Produto  produto;
    private class HttpAsyncTask extends AsyncTask<String, Void, Produto> {
        @Override
        //protected String doInBackground(String... urls) {
        protected Produto doInBackground(String... urls) {

            produto = new Produto();
            produto.setCodigoBarra(etCodigoBarra.getText().toString());


            Produto prodTest = POST(urls[0],produto);
            Log.i("json", prodTest.getDescricao());
            if (prodTest == null) {
                showErrorMessage();
            } else {
                logProdutoIn(prodTest);
            }

            return prodTest;

            //return POST(urls[0],usuario);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        //protected void onPostExecute(String result) {
        protected void onPostExecute(Produto result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    //public String POST(String url, Usuario usuario){
    public Produto POST(String url, Produto produto1){
        InputStream inputStream = null;
        Produto returnedProduto = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("codigoBarra", produto1.getCodigoBarra());

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
                returnedProduto = null;
            } else {
                Integer id = Integer.parseInt(jObject.getString("id"));
                String descricao = jObject.getString("descricao");

                List<Restricao> listaRestricoes = new ArrayList<Restricao>();
                JSONArray values = jObject.getJSONArray("restricoes");
                for (int i = 0; i < values.length(); i++) {
                    JSONObject restricao = values.getJSONObject(i);
                    int restricaoId = restricao.getInt("id");
                    String descricaoRestricao = restricao.getString("descricao");
//
                    Restricao restricaoObj = new Restricao();
                    restricaoObj.setId(restricaoId);
                    restricaoObj.setDescricao(descricaoRestricao);
//
                    listaRestricoes.add(restricaoObj);
                    Log.i("json",descricaoRestricao);
                }
//                returnedProduto.setRestricoes(restricoes);

                //returnedProduto = new Produto(id, descricao, produto1.getCodigoBarra());

                returnedProduto = new Produto();
                returnedProduto.setId(id);
                returnedProduto.setDescricao(descricao);
                returnedProduto.setCodigoBarra(produto1.getCodigoBarra());
                returnedProduto.setRestricoes(listaRestricoes);
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

        //Log.i("json","size: "+returnedProduto.getRestricoes().size()+"");

        return returnedProduto;
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

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaProduto.this);
        builder.setMessage("Incorreto produto details");
        builder.setPositiveButton("Ok", null);
        builder.show();
    }

    private void logProdutoIn(Produto returnedProduto) {
        produtoLocalStore.storeUsuarioData(returnedProduto);
        produtoLocalStore.setProdutoLoggedIn(true);
        startActivity(new Intent(this, DetalheProduto.class));
    }
}

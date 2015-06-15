package com.nutrichecker.br.nutrichecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class DetalheProduto extends ActionBarActivity implements View.OnClickListener {

    EditText etDescricao, etCodigoBarra;
    TextView tvVoltarMain;
    ProdutoLocalStore produtoLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);

        etDescricao = (EditText)findViewById(R.id.etDescricaoDetalhe);
        etCodigoBarra = (EditText)findViewById(R.id.etCodigoBarraDetalhe);
        tvVoltarMain = (TextView)findViewById(R.id.tvVoltarMain);

        tvVoltarMain.setOnClickListener(this);

        produtoLocalStore = new ProdutoLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate() == true){
            displayProdutoDetails();
        } else {
            startActivity(new Intent(DetalheProduto.this, Login.class));
        }
    }

    private boolean authenticate(){
        return produtoLocalStore.getProdutoLoggedIn();
    }

    private void displayProdutoDetails(){
        Produto produto = produtoLocalStore.getLoggedInProduto();

        etDescricao.setText(produto.getDescricao());
        etCodigoBarra.setText(produto.getCodigoBarra());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvVoltarMain:
                produtoLocalStore.clearProdutoData();
                produtoLocalStore.setProdutoLoggedIn(false);

                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

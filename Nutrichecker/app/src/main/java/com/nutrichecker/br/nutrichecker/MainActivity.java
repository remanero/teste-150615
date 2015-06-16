package com.nutrichecker.br.nutrichecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity  implements View.OnClickListener{

    Button bLogout;
    EditText etName, etUsername;
    UsuarioLocalStore usuarioLocalStore;
    TextView tvConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        bLogout = (Button) findViewById(R.id.bLogout);
        tvConsultar = (TextView)findViewById(R.id.tvConsultar);

        bLogout.setOnClickListener(this);
        tvConsultar.setOnClickListener(this);

        usuarioLocalStore = new UsuarioLocalStore(this);
}

    @Override
    protected void onStart() {
        super.onStart();
        if(authenticate() == true){
            displayUsuarioDetails();
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private boolean authenticate(){
        return usuarioLocalStore.getUsuarioLoggedIn();
    }

    private void displayUsuarioDetails(){
        Usuario usuario = usuarioLocalStore.getLoggedInUsuario();

        etName.setText(usuario.nome);
        etUsername.setText(usuario.email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                usuarioLocalStore.clearUsuarioData();
                usuarioLocalStore.setUsuarioLoggedIn(false);

                startActivity(new Intent(this, Login.class));
                break;
            case R.id.tvConsultar:
                startActivity(new Intent(this, ConsultaProduto.class));
                break;
        }
    }
}

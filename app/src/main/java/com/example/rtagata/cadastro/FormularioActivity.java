package com.example.rtagata.cadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rtagata.cadastro.dao.AlunoDAO;
import com.example.rtagata.cadastro.extras.Extras;
import com.example.rtagata.cadastro.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    public static final String ALUNO_SELECIONADO = "alunoSelecionado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);




        Intent intent = getIntent();
        final Aluno alunoParaSerAlterado = (Aluno) intent.getSerializableExtra(Extras.ALUNO_SELECIONADO);

        this.helper = new FormularioHelper(this);

        Button botao = (Button) findViewById(R.id.menu_formulario_ok);
        if(alunoParaSerAlterado != null){
            helper.colocaNoFormulario(alunoParaSerAlterado);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegaAlunoDoFormulario();
                AlunoDAO dao = new AlunoDAO(FormularioActivity.this);
                if(aluno.getId() == null){
                    dao.insere(aluno);
                }else{
                    dao.altera(aluno);
                }
                dao.close();
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

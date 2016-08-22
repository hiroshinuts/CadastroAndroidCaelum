package com.example.rtagata.cadastro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.File;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    public static final String ALUNO_SELECIONADO = "alunoSelecionado";
    private String localArquivoFoto;
    private static final int TIRA_FOTO = 123;

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

        Button foto = helper.getFotoButton();
        foto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis()+".jpg";
                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri localFoto = Uri.fromFile(new File(localArquivoFoto));
                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                startActivityForResult(irParaCamera, TIRA_FOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TIRA_FOTO){
            if(resultCode == Activity.RESULT_OK){
                helper.carregaImagem(this.localArquivoFoto);
            }else{
                this.localArquivoFoto = null;
            }
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

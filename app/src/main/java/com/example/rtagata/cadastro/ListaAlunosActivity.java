package com.example.rtagata.cadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        String[] alunos = {"Auei", "Buiei", "Cuei"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        if (listaAlunos != null) {
            listaAlunos.setAdapter(adapter);
        }

        //CLICK NORMAL no NOME
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
                Toast.makeText(ListaAlunosActivity.this, "Posição selecionada: " + posicao, Toast.LENGTH_LONG).show();
            }
        });

        //CLICK LONGO
        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {

                String aluno = (String) adapter.getItemAtPosition(posicao);
                Toast.makeText(ListaAlunosActivity.this, "Clique longo: " + aluno, Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        Button botaoAdiciona = (Button) findViewById(R.id.lista_alunos_floating_button);
        if (botaoAdiciona != null) {
            botaoAdiciona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}

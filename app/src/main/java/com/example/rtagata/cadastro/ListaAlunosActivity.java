package com.example.rtagata.cadastro;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rtagata.cadastro.adapter.ListaAlunosAdapter;
import com.example.rtagata.cadastro.dao.AlunoDAO;
import com.example.rtagata.cadastro.extras.Extras;
import com.example.rtagata.cadastro.modelo.Aluno;

import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;
    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        AlunoDAO dao = new AlunoDAO(this);
        alunos = dao.getLista();
        dao.close();
        ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        if (listaAlunos != null) {
            listaAlunos.setAdapter(adapter);
        }

        //CLICK NORMAL no NOME
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(posicao);
                edicao.putExtra(Extras.ALUNO_SELECIONADO, aluno);
                startActivity(edicao);
            }
        });

        //CLICK LONGO
        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicao, long id) {

                Aluno aluno = (Aluno) adapter.getItemAtPosition(posicao);
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

        //AVISANDO ONCREATE Q A LISTA TEM CONTEXTMENU
        registerForContextMenu(listaAlunos);


    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);

        MenuItem ligar = menu.add("Ligar");
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel: " + alunoSelecionado.getTelefone()));
        ligar.setIntent(intentLigar);

        MenuItem sms = menu.add("Enviar SMS");
        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms: " + alunoSelecionado.getTelefone()));
        sms.setIntent(intentSms);

        MenuItem acharNoMapa =  menu.add("Achar no Mapa");
        Intent intentMapa  = new Intent(Intent.ACTION_VIEW);
        String endereco = alunoSelecionado.getEndereco();
        intentMapa.setData(Uri.parse("geo: 0,0?z=14&q=" + Uri.encode(endereco)));
        acharNoMapa.setIntent(intentMapa);

        MenuItem site = menu.add("Navegar no site");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String siteString = alunoSelecionado.getSite();
        if(!siteString.startsWith("http://")){
            siteString = "http:// " + siteString;
        }
        intent.setData(Uri.parse("http: " + alunoSelecionado.getSite()));
        site.setIntent(intent);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja Mesmo Deletar ?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                dao.deletar(alunoSelecionado);
                                dao.close();
                                carregaLista();
                            }
                        }).setNegativeButton("Nao", null).show();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        this.listaAlunos.setAdapter(adapter);
    }
}

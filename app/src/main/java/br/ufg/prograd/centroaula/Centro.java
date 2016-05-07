package br.ufg.prograd.centroaula;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.prograd.centroaula.controle.CentroHttp;

public class Centro extends Activity {

  CentroTask horarioTask;

  List<String[]> lCentro;

  GridView gvCentro;

  TextView tvMensagem;

  ProgressBar barraProgresso;

  ArrayAdapter<String[]> celulaAdpater;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.centro);
    tvMensagem = (TextView) findViewById(R.id.tvMensagem);
    barraProgresso = (ProgressBar) findViewById(R.id.barraProgresso);
    gvCentro = (GridView) findViewById(R.id.gvCentro);

    if (lCentro == null) {
      lCentro = new ArrayList<>();
    }

    celulaAdpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lCentro);
    gvCentro.setAdapter(celulaAdpater);

    if (horarioTask == null) {
      if (CentroHttp.temConexao(this)) {
        iniciarDownload();
      } else {
        tvMensagem.setText("Sem conexão!");
      }
    } else if (horarioTask.getStatus() == AsyncTask.Status.RUNNING) {
      exibirProgresso(true);
    }

  }

  private void exibirProgresso(boolean exibir) {
    if (exibir) {
      tvMensagem.setText("Baixando informações do Centro de Aula!");
    }

    tvMensagem.setVisibility((exibir ? View.VISIBLE : View.GONE));
    barraProgresso.setVisibility((exibir ? View.VISIBLE : View.GONE));
  }

  public void iniciarDownload() {
    if (horarioTask == null || horarioTask.getStatus() != AsyncTask.Status.RUNNING) {
      horarioTask = new CentroTask();
      horarioTask.execute();
    }
  }

class CentroTask extends AsyncTask<Void, Void, List<String[]>> {

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    exibirProgresso(true);
  }

  @Override
  protected List<String[]> doInBackground(Void... params) {
    CentroHttp centroHttp = new CentroHttp();
    return centroHttp.carregarCentro(1);
  }

  @Override
  protected void onPostExecute(List<String[]> centro) {
    super.onPostExecute(centro);
    exibirProgresso(false);

    if (centro != null) {
      lCentro.clear();
      lCentro.addAll(centro);
      celulaAdpater.notifyDataSetChanged();
    } else {
      tvMensagem.setText("Falha ao receber informações do Centro de Aula!");
    }
  }
}
}

package br.ufg.prograd.centroaula.controle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

/**
 * Responsável pela apresentação das informações dos centro de aula dentro do GridView.
 */
public class CentroAdpater extends ArrayAdapter<String[]> {

  private Context mContext;
  private List<String[]> conteudo;

  public CentroAdpater(Context c, List<String[]> eHorario) {
    super(c, 0, eHorario);
  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public String[] getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView textView;
    if (convertView == null) {
      textView = new TextView(mContext);
    } else {
      textView = (TextView) convertView;
    }

    Integer colunas = ((GridView) parent).getNumColumns();
    Integer celula = position % colunas;
    Integer linha = position / colunas;

    textView.setText(conteudo.get(linha)[celula]);
    return textView;
  }

  public void setConteudo(List<String[]> conteudo) {
    this.conteudo = conteudo;
  }
}

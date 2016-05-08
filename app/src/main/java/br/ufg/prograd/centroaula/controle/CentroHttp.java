package br.ufg.prograd.centroaula.controle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.ufg.prograd.centroaula.entidade.ECentro;

/**
 * Classe responsável pela comunicação com o webservice da UFG. Buscando as informações em formato
 * texto tabulado e convertendo para uma lista de objeto.
 */
public class CentroHttp {
  // private static final String HTTPS_URL =
  // "https://projetos.extras.ufg.br/DSU/SiDS/?ModuleName=Rooms&Action=TVTable&Building=1&Period=2016.1&WeekDay=1";
  private static final String HTTPS_URL = "http://projetos.extras.ufg.br/DSU/SiDS/?ModuleName=Rooms&Action=TVTable";

  private static final String SEPARADOR_DADOS = "\t";
  private static List<String> rotuloColunas;

//  private List<String[]> conteudo;

  /**
   * Faz a conexão com o webservice.<br>
   *   Monta a URL de conexão com o webservice fazendo a verificação do semestre atual,
   *   do dia da semana atual e passando o código do centro de aula.
   * @param predio códido do centro de aula a ser usado para a consulta dos horários de aula.
   * @return a conexão estabelecida.
   * @throws IOException caso ocorra um problema com a conexão.
   */
  private HttpURLConnection conectar(Integer predio) throws IOException {
    Integer diaSemana;

    final int ano = Calendar.getInstance().get(Calendar.YEAR);

    final int periodo = (Calendar.getInstance().get(Calendar.MONTH) <= 6) ? 1 : 2;

    diaSemana = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

    final URL url = new URL(HTTPS_URL + "&Building=" + predio + "&Period=" + ano + "." + periodo + "&WeekDay="
            + diaSemana);

    return (HttpURLConnection) url.openConnection();
  }

  /**
   * Verfica se existe um serviço de rede ativo e se a conexão foi realizada.
   * @param context contexto onde a app está rodando
   * @return true - se tem um serviço d rede ativo e conseguiu conexão, caso contrario retorna false.
   */
  public static boolean temConexao(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo informacaoRede = cm.getActiveNetworkInfo();
    return (informacaoRede != null && informacaoRede.isConnected());
  }

  /**
   * Solicita a conexão com o servidor do SiDS. Recebe as informações e faz o tratamento da mesma.
   * @param predio códido do centro de aula a ser usado para a consulta dos horários de aula.
   * @return As informações das aulas do centro de aula formatado e agrupado.
   */
  public List<String> carregarCentro(int predio) {

    List<ECentro> resultadoConsulta = new ArrayList<>();

    try {
      HttpURLConnection conexao = conectar(predio);

      if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        String linhaBruta;
        String[] linhaTratada;
        int contadorLinha = 0;
        rotuloColunas = new ArrayList<>();

        while (null != (linhaBruta = br.readLine())) {
//          linhaBruta = new String(linhaBruta.getBytes("ISO-8859-1"), "UTF-8");

          switch (contadorLinha) {
            case 0:
              break;
            case 1:
              linhaTratada = linhaBruta.split(SEPARADOR_DADOS);
              linhaTratada[0] = "Horário/Salas";
              rotuloColunas = Arrays.asList(linhaTratada);
              break;
            default:
              linhaTratada = linhaBruta.split(SEPARADOR_DADOS, rotuloColunas.size());

              final ECentro eCentro = new ECentro();

              final String[] horario = linhaTratada[0].split("-");
              final Calendar dataInicio = Calendar.getInstance();
              final Calendar dataFim = Calendar.getInstance();

              dataInicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario[0].substring(0, 2)));
              dataInicio.set(Calendar.MINUTE, Integer.parseInt(horario[0].substring(3, 5)));

              dataFim.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario[1].substring(0, 2)));
              dataFim.set(Calendar.MINUTE, Integer.parseInt(horario[1].substring(3, 5)));

              eCentro.setDataInicio(dataInicio.getTime());
              eCentro.setDataFim(dataFim.getTime());

              final String[] sala = Arrays.copyOfRange(linhaTratada, 1, linhaTratada.length);

              eCentro.setDisciplinaSala(sala);
              resultadoConsulta.add(eCentro);
              break;
          }

          contadorLinha++;
        }
        br.close();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return prepararTabela(resultadoConsulta);
  }

  /**
   * Faz o tratamento das informações em formato de objeto para uma lista com strings.
   * @param lista lista das informações dos horário de aula
   * @return lista preparada para ser usada em componentes de GridView, ListView e outros.
   */
  public static List<String> prepararTabela(List<ECentro> lista) {
    List<String> conteudo = new ArrayList<>();
    final DateFormat formatadorData = new SimpleDateFormat("HH:mm");

      int fim = lista.size();

    final String[] colunasCabecalho = new String[fim];

    colunasCabecalho[0] = rotuloColunas.get(0);

    for (int contador = 0; contador < fim; contador++) {
      colunasCabecalho[contador] = rotuloColunas.get(contador);
    }

    conteudo.addAll(Arrays.asList(colunasCabecalho));

    for (int contador1 = 0; contador1 < lista.size(); contador1++) {

      final String[] conteudoAux = new String[colunasCabecalho.length];

      conteudoAux[0] = formatadorData.format(lista.get(contador1).getDataInicio()) + " - "
        + formatadorData.format(lista.get(contador1).getDataFim());

      for (int contador2 = 1; contador2 < fim; contador2++) {
        conteudoAux[contador2] = lista.get(contador1).getDisciplinaSala()[contador2];
      }

      conteudo.addAll(Arrays.asList(conteudoAux));
    }

    return conteudo;
  }

}

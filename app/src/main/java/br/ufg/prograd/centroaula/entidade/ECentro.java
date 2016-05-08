package br.ufg.prograd.centroaula.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class ECentro implements Serializable {

  private static final long serialVersionUID = -2591154974918066513L;

  private Integer id;

  private Date dataInicio;

  private Date dataFim;

  private String[] disciplinaSala;

  public Integer getId() {
    return this.id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Date getDataInicio() {
    return this.dataInicio;
  }

  public void setDataInicio(final Date dataInicio) {
    this.dataInicio = dataInicio;
  }

  public Date getDataFim() {
    return this.dataFim;
  }

  public void setDataFim(final Date dataFim) {
    this.dataFim = dataFim;
  }

  public String[] getDisciplinaSala() {
    return this.disciplinaSala;
  }

  public void setDisciplinaSala(final String[] sala) {
    this.disciplinaSala = sala;
  }
}
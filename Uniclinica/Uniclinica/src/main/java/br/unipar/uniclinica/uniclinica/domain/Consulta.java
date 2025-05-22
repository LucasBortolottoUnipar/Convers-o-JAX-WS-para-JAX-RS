package br.unipar.uniclinica.uniclinica.domain;

import br.unipar.uniclinica.uniclinica.dto.ConsultaAgendarRequestDTO;

import java.time.LocalDateTime;

public class Consulta {
    private Integer id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private boolean ativo;
    private String motivoCancelamento;
    private LocalDateTime dataCancelamento;

    public Consulta() {
        this.ativo = true;
    }

    public Consulta(ConsultaAgendarRequestDTO dto) {
        this.dataHora = LocalDateTime.parse(dto.getDataHora());
        this.medico = new Medico();
        this.medico.setId(dto.getMedicoId());
        this.paciente = new Paciente();
        this.paciente.setId(dto.getPacienteId());
        this.ativo = true;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
}

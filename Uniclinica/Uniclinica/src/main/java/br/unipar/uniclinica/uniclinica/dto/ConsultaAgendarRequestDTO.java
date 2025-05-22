package br.unipar.uniclinica.uniclinica.dto;

public class ConsultaAgendarRequestDTO {
    private Integer pacienteId;
    private Integer medicoId; // opcional
    private String dataHora;

    public ConsultaAgendarRequestDTO() {}

    public Integer getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Integer pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Integer getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Integer medicoId) {
        this.medicoId = medicoId;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {

        if (dataHora == null || dataHora.isBlank()) {
            throw new IllegalArgumentException("O campo dataHora deve ser preenchido no formato ISO (ex: 2025-04-07T02:00)");
        }
        this.dataHora = dataHora;
    }
}

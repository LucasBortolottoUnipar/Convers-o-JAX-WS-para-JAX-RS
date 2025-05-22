package br.unipar.uniclinica.uniclinica.dto;

public class ConsultaCancelarRequestDTO {

    private Integer consultaId;
    private String motivo;

    public ConsultaCancelarRequestDTO() {}

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

package br.unipar.uniclinica.uniclinica.dto;

import br.unipar.uniclinica.uniclinica.domain.Endereco;

public class MedicoListarRequestDTO {

    private String nome;
    private String email;
    private String crm;
    private String especialidade;

    public MedicoListarRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}

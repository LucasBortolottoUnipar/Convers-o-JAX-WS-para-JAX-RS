package br.unipar.uniclinica.uniclinica.domain;

import br.unipar.uniclinica.uniclinica.dto.MedicoRegistroRequestDTO;

public class Medico {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    private String especialidade;
    private Endereco endereco;
    private boolean ativo;

    public Medico () {

    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Medico (MedicoRegistroRequestDTO mregistroDTO){
        this.nome = mregistroDTO.getNome();
        this.email = mregistroDTO.getEmail();
        this.telefone = mregistroDTO.getTelefone();
        this.crm = mregistroDTO.getCrm();
        this.especialidade = mregistroDTO.getEspecialidade();
        this.endereco = mregistroDTO.getEndereco();
        this.ativo = true;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }


}

package br.unipar.uniclinica.uniclinica.dto;

import br.unipar.uniclinica.uniclinica.domain.Endereco;

public class MedicoAtualizarRequestDTO {

    private String nome;
    private String telefone;
    private Endereco endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}

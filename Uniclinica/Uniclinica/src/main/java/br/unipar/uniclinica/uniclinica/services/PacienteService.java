package br.unipar.uniclinica.uniclinica.services;

import br.unipar.uniclinica.uniclinica.domain.Paciente;
import br.unipar.uniclinica.uniclinica.dto.PacienteAtualizarRequestDTO;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.repositories.PacienteRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class PacienteService {
    private PacienteRepository pacienteRepository;

    public PacienteService() {
        pacienteRepository = new PacienteRepository();
    }

    public Paciente inserir(Paciente paciente) throws BusinessException {

        if (paciente.getNome() == null || paciente.getNome().isEmpty()) {
            throw new BusinessException("O nome é obrigatório.");
        }

        if (paciente.getNome().length() > 100) {
            throw new BusinessException("O nome deve possuir no máximo 100 caracteres.");
        }

        if (!Validacoes.validaEmail(paciente.getEmail())) {
            throw new BusinessException("Por favor, insira um e-mail válido.");
        }

        if (!Validacoes.validaTelefone(paciente.getTelefone())) {
            throw new BusinessException("Por favor, insira um número de telefone válido.");
        }

        if (!Validacoes.validaCPF(paciente.getCpf())) {
            throw new BusinessException("Por favor, insira um CPF válido.");
        }

        if (paciente.getEndereco() == null) {
            throw new BusinessException("Você deve informar os campos do endereço.");
        }

        if (!Validacoes.validarLogradouro(paciente.getEndereco().getLogradouro())) {
            throw new BusinessException("Por favor, informe um logradouro válido.");
        }

        if (!Validacoes.validarBairro(paciente.getEndereco().getBairro())) {
            throw new BusinessException("Por favor, informe um bairro válido.");
        }

        if (!Validacoes.validarCidade(paciente.getEndereco().getCidade())) {
            throw new BusinessException("Por favor, informe uma cidade válida.");
        }

        if (!Validacoes.validarUF(paciente.getEndereco().getUf())) {
            throw new BusinessException("Por favor, informe uma sigla válida.");
        }

        if (!Validacoes.validarCep(paciente.getEndereco().getCep())) {
            throw new BusinessException("Por favor, informe um cep válido.");
        }

        try {
            return pacienteRepository.inserir(paciente);
        } catch (Exception e) {
            throw new BusinessException("Erro ao inserir paciente: " + e.getMessage());
        }
    }

    public Paciente editar(Integer id, PacienteAtualizarRequestDTO pacienteDTO) throws BusinessException {

        if (pacienteDTO.getNome() == null || pacienteDTO.getNome().isEmpty()) {
            throw new BusinessException("O nome é obrigatório.");
        }

        if (pacienteDTO.getNome().length() > 100) {
            throw new BusinessException("O nome deve possuir no máximo 100 caracteres.");
        }

        if (!Validacoes.validaTelefone(pacienteDTO.getTelefone())) {
            throw new BusinessException("Por favor, insira um número de telefone válido.");
        }

        if (pacienteDTO.getEndereco() == null) {
            throw new BusinessException("Você deve informar os campos do endereço.");
        }

        if (!Validacoes.validarLogradouro(pacienteDTO.getEndereco().getLogradouro())) {
            throw new BusinessException("Por favor, informe um logradouro válido.");
        }

        if (!Validacoes.validarBairro(pacienteDTO.getEndereco().getBairro())) {
            throw new BusinessException("Por favor, informe um bairro válido.");
        }

        if (!Validacoes.validarCidade(pacienteDTO.getEndereco().getCidade())) {
            throw new BusinessException("Por favor, informe uma cidade válida.");
        }

        if (!Validacoes.validarUF(pacienteDTO.getEndereco().getUf())) {
            throw new BusinessException("Por favor, informe uma sigla válida.");
        }

        if (!Validacoes.validarCep(pacienteDTO.getEndereco().getCep())) {
            throw new BusinessException("Por favor, informe um cep válido.");
        }

        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNome(pacienteDTO.getNome());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEndereco(pacienteDTO.getEndereco());

        try {
            return pacienteRepository.editar(paciente);
        } catch (Exception e) {
            throw new BusinessException("Erro ao editar paciente: " + e.getMessage());
        }
    }

    public List<Paciente> buscarTodos() throws BusinessException {
        try {
            return pacienteRepository.buscarTodos();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar pacientes. Entre em contato com o suporte do WebService");
        }
    }

    public Paciente deletar(Integer id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("Id do paciente é obrigatório para exclusão.");
        }

        try {
            Paciente paciente = pacienteRepository.buscarPorId(id);

            if (paciente == null) {
                throw new BusinessException("Paciente não encontrado para o ID informado.");
            }

            // Prefixa o nome com "(Inativo) " se ainda não tiver
            if (!paciente.getNome().startsWith("(Inativo) ")) {
                paciente.setNome("(Inativo) " + paciente.getNome());
            }

            return pacienteRepository.deletar(paciente);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao inativar paciente. Entre em contato com o suporte do WebService.");
        }
    }


    public Paciente buscarPorId(int id) throws BusinessException {
        try {
            return pacienteRepository.buscarPorId(id);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar paciente. Entre em contato com o suporte do WebService");
        }
    }
}

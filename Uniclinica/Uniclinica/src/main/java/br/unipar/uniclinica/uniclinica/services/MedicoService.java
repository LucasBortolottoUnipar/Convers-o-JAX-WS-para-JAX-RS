package br.unipar.uniclinica.uniclinica.services;

import br.unipar.uniclinica.uniclinica.domain.Medico;
import br.unipar.uniclinica.uniclinica.dto.MedicoAtualizarRequestDTO;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.repositories.MedicoRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class MedicoService {

    private MedicoRepository medicoRepository;

    public MedicoService() { //Para garantir que toda vez que MedicoService for instanciado se crie um repository
        medicoRepository = new MedicoRepository();
    }

    public Medico inserir(Medico medico) throws BusinessException {

        if (medico.getNome() == null || medico.getNome().isEmpty()){
            throw new BusinessException("O nome é obrigatório.");
        }

        if (medico.getNome().length() > 100) {
            throw new BusinessException("O nome deve possuir no máximo 100 caracteres.");
        }

        if(!Validacoes.validaEmail(medico.getEmail())){
            throw new BusinessException("Por favor, insira um e-mail válido.");
        }

        if(!Validacoes.validaTelefone(medico.getTelefone())){
            throw new BusinessException("Por favor, insira um número de telefone válido.");
        }

        if(!Validacoes.validaCRM(medico.getCrm())){
            throw new BusinessException("Por favor, insira um CRM válido.");
        }

        if(!medico.getEspecialidade().equalsIgnoreCase("Cardiologia") &&
                !medico.getEspecialidade().equalsIgnoreCase("Ortopedia")&&
                !medico.getEspecialidade().equalsIgnoreCase("Ginecologia")&&
                !medico.getEspecialidade().equalsIgnoreCase("Dermatologia")){

            throw new BusinessException("Por favor, insira uma especialidade válida.");
        }

        if (medico.getEndereco() == null) {
            throw new BusinessException("Você deve informar os campos do endereço.");
        }

        if(!Validacoes.validarLogradouro(medico.getEndereco().getLogradouro())){
            throw new BusinessException("Por favor, informe um logradouro válido.");
        }

        if(!Validacoes.validarBairro(medico.getEndereco().getBairro())){
            throw new BusinessException("Por favor, informe um bairro válido.");
        }

        if(!Validacoes.validarCidade(medico.getEndereco().getCidade())){
            throw new BusinessException("Por favor, informe uma cidade válida.");
        }

        if(!Validacoes.validarUF(medico.getEndereco().getUf())){
            throw new BusinessException("Por favor, informe uma sigla válida.");
        }

        if(!Validacoes.validarCep(medico.getEndereco().getCep())){
            throw new BusinessException("Por favor, informe um cep válido.");
        }

        try {
            return medicoRepository.inserir(medico);
        } catch (Exception e){
            throw new BusinessException("Erro ao inserir medico." + e.getMessage());
        }

    }

    public Medico editar(Integer id, MedicoAtualizarRequestDTO medicoDTO) throws BusinessException{
        if (medicoDTO.getNome() == null || medicoDTO.getNome().isEmpty()){
            throw new BusinessException("O nome é obrigatório.");
        }

        if (medicoDTO.getNome().length() > 100) {
            throw new BusinessException("O nome deve possuir no máximo 100 caracteres.");
        }

        if(!Validacoes.validaTelefone(medicoDTO.getTelefone())){
            throw new BusinessException("Por favor, insira um número de telefone válido.");
        }

        if (medicoDTO.getEndereco() == null) {
            throw new BusinessException("Você deve informar os campos do endereço.");
        }

        if(!Validacoes.validarLogradouro(medicoDTO.getEndereco().getLogradouro())){
            throw new BusinessException("Por favor, informe um logradouro válido.");
        }

        if(!Validacoes.validarBairro(medicoDTO.getEndereco().getBairro())){
            throw new BusinessException("Por favor, informe um bairro válido.");
        }

        if(!Validacoes.validarCidade(medicoDTO.getEndereco().getCidade())){
            throw new BusinessException("Por favor, informe uma cidade válida.");
        }

        if(!Validacoes.validarUF(medicoDTO.getEndereco().getUf())){
            throw new BusinessException("Por favor, informe uma sigla válida.");
        }

        if(!Validacoes.validarCep(medicoDTO.getEndereco().getCep())){
            throw new BusinessException("Por favor, informe um cep válido.");
        }

        Medico medico = new Medico();
        medico.setId(id);
        medico.setNome(medicoDTO.getNome());
        medico.setTelefone(medicoDTO.getTelefone());
        medico.setEndereco(medicoDTO.getEndereco());

        try {
            return medicoRepository.editar(medico);
        } catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("Erro ao editar medico.");
        }

    }

    public List<Medico> listar() throws BusinessException{
        try{
            return medicoRepository.listar();
        } catch (SQLException | NamingException e){
            e.printStackTrace();
            throw new BusinessException("Erro ao listar medicos.");
        }
    }

    public Medico excluir(Integer id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("Por favor, insira um id válido.");
        }

        try {

            Medico medico = medicoRepository.buscarPorId(id);

            if (medico == null) {
                throw new BusinessException("Médico não encontrado para o ID informado.");
            }

            if (!medico.getNome().startsWith("(Inativo) ")) {
                medico.setNome("(Inativo) " + medico.getNome());
            }

            return medicoRepository.excluir(medico);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao inativar médico.");
        }
    }
}

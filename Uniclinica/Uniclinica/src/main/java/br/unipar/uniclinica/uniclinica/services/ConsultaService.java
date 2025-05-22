package br.unipar.uniclinica.uniclinica.services;

import br.unipar.uniclinica.uniclinica.domain.Consulta;
import br.unipar.uniclinica.uniclinica.domain.Medico;
import br.unipar.uniclinica.uniclinica.domain.MotivoCancelamento;
import br.unipar.uniclinica.uniclinica.domain.Paciente;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.repositories.ConsultaRepository;
import br.unipar.uniclinica.uniclinica.repositories.MedicoRepository;
import br.unipar.uniclinica.uniclinica.repositories.PacienteRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class ConsultaService {

    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final PacienteRepository pacienteRepository = new PacienteRepository();

    public Consulta agendar(Consulta consulta) throws BusinessException {
        LocalDateTime dataHora = consulta.getDataHora();
        LocalDateTime dataFim = dataHora.plusHours(1);

        List<Consulta> consultasExistentes = consultaRepository.buscarMedico(consulta.getMedico().getId());

        for(Consulta c : consultasExistentes){
            if (!c.isAtivo()) continue;

            LocalDateTime comecoConsulta = c.getDataHora();
            LocalDateTime fimConsulta = comecoConsulta.plusHours(1);

            boolean conflito = dataHora.isBefore(fimConsulta) && dataFim.isAfter(comecoConsulta);

            if (conflito){
                throw new BusinessException("Este médico ja possui uma consulta agendada para este horário.");
            }
        }


        if (dataHora.getHour() < 7 || dataHora.getHour() >= 19 || dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BusinessException("Consultas devem ser entre 07:00 e 19:00, de segunda a sábado.");
        }

        if (LocalDateTime.now().plusMinutes(30).isAfter(dataHora)) {
            throw new BusinessException("A consulta deve ser agendada com no mínimo 30 minutos de antecedência.");
        }

        try {
            Paciente paciente = pacienteRepository.buscarPorId(consulta.getPaciente().getId());
            if (paciente == null || !paciente.isAtivo()) {
                throw new BusinessException("Paciente inativo ou não encontrado.");
            }

            if (consultaRepository.temConsultaNoMesmoDia(paciente.getId(), dataHora.toLocalDate())) {
                throw new BusinessException("Este paciente já possui uma consulta marcada para este dia.");
            }

            Medico medico;
            Integer medicoId = (consulta.getMedico() != null) ? consulta.getMedico().getId() : null;

            if (medicoId != null && medicoId > 0) {
                medico = medicoRepository.buscarPorId(medicoId);
                if (medico == null || !medico.isAtivo()) {
                    throw new BusinessException("Médico inativo ou não encontrado.");
                }
            } else {
                List<Medico> disponiveis = medicoRepository.buscarMedicosDisponiveis(dataHora);
                if (disponiveis.isEmpty()) {
                    throw new BusinessException("Nenhum médico disponível para esse horário.");
                }
                medico = disponiveis.get(new Random().nextInt(disponiveis.size()));
            }


            consulta.setPaciente(paciente);
            consulta.setMedico(medico);


            Consulta consultaSalva = consultaRepository.inserir(consulta);


            consultaSalva.setPaciente(paciente);
            consultaSalva.setMedico(medico);
            consultaSalva.setDataHora(consulta.getDataHora());

            return consultaSalva;

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao agendar consulta.");
        }
    }

    public void cancelar(Consulta consulta, MotivoCancelamento motivo) throws BusinessException {
        if (motivo == null) {
            throw new BusinessException("Motivo do cancelamento é obrigatório.");
        }

        try {
            Consulta consultaDb = consultaRepository.buscarPorId(consulta.getId());

            if (consultaDb == null) {
                throw new BusinessException("Consulta não encontrada.");
            }

            if (ChronoUnit.HOURS.between(LocalDateTime.now(), consultaDb.getDataHora()) < 24) {
                throw new BusinessException("Cancelamentos devem ser feitos com no mínimo 24h de antecedência.");
            }

            consultaRepository.cancelar(consultaDb, motivo);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao cancelar consulta.");
        }
    }


}

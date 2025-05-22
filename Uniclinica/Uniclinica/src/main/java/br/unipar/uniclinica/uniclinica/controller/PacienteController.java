package br.unipar.uniclinica.uniclinica.controller;
import br.unipar.uniclinica.uniclinica.domain.Paciente;
import br.unipar.uniclinica.uniclinica.dto.ExceptionDTO;
import br.unipar.uniclinica.uniclinica.dto.PacienteRegistroRequestDTO;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.services.PacienteService;
import br.unipar.uniclinica.uniclinica.dto.PacienteAtualizarRequestDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService = new PacienteService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> listarTodos() throws BusinessException {
        return pacienteService.buscarTodos();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Paciente buscarPorId(@PathParam("id") int id) throws BusinessException {
        return pacienteService.buscarPorId(id);
    }


    //Ver com o Anderson se podew deixar Paciente paciente ou se tem que mudar o codigo para pacienteDTO
    //Vou ter que mudar o insert no PacienteService e PacienteRepository

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inserir(PacienteRegistroRequestDTO dto) {
        try {
            Paciente paciente = new Paciente(dto);
            Paciente novoPaciente = pacienteService.inserir(paciente);

            return Response.status(Response.Status.CREATED)
                    .entity(novoPaciente)
                    .build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDTO(e.getMessage()))
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ExceptionDTO("Ocorreu um erro interno."))
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Paciente editar(@PathParam("id") Integer id, PacienteAtualizarRequestDTO dto) throws BusinessException {
        return pacienteService.editar(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void deletar(@PathParam("id") Integer id) throws BusinessException {
        pacienteService.deletar(id);
    }
}



package br.unipar.uniclinica.uniclinica.controller;

import br.unipar.uniclinica.uniclinica.domain.Medico;
import br.unipar.uniclinica.uniclinica.dto.ExceptionDTO;
import br.unipar.uniclinica.uniclinica.dto.MedicoRegistroRequestDTO;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.services.MedicoService;
import br.unipar.uniclinica.uniclinica.dto.MedicoAtualizarRequestDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
public class MedicoController {
    private final MedicoService medicoService = new MedicoService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> listarTodos() throws BusinessException {
        return medicoService.listar();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inserir(MedicoRegistroRequestDTO dto) {
        try {
            Medico medico = new Medico(dto);
            Medico novoMedico = medicoService.inserir(medico);

            return Response.status(Response.Status.CREATED)
                    .entity(novoMedico)
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
    @Path("{id}")
    public Medico editar(@PathParam("id") Integer id, MedicoAtualizarRequestDTO dto) throws BusinessException {
        return medicoService.editar(id, dto);
    }

    @DELETE
    @Path("{id}")
    public void deletar(@PathParam("id") Integer id) throws BusinessException {
        medicoService.excluir(id);
    }
}

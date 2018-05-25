package rest;

import domain.Part;
import domain.Comm;
import domain.PartsResponse;
import domain.service.PartService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/parts")
@Stateless
public class PartResources {

    private PartService db = new PartService();

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Part> getAll() {

        return em.createNamedQuery("part.all", Part.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Part part) {
        em.persist(part);
        return Response.ok(part.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {

        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", id)
                .getSingleResult();

        if (result == null) {

            return Response.status(404).build();
        }
        return Response.ok(result).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Part p) {

        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", id)
                .getSingleResult();

        if (result == null) {
            return Response.status(404).build();
        }

        if (p.getName() != null)
            result.setName(p.getName());

        if (p.getPrice() != 0)
            result.setPrice(p.getPrice());

        if (p.getCategory() != null)
            result.setCategory(p.getCategory());

        em.persist((result));
        return Response.ok().build();

    }
    void flushAndClear() {
        em.flush();
        em.clear();
    }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {

        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", id)
                .getSingleResult();

        if (result == null) {
            return Response.status(404).build();
        }


        em.remove(result);
        flushAndClear();

        return Response.ok().build();
    }
    //Wyswietlanie komentarzy
    @GET
    @Path("/{partId}/comments")
    @Produces(MediaType.APPLICATION_JSON)

    public List<Comm> getComments(@PathParam("partId") int partId) {


        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", partId)
                .getSingleResult();

        if (result == null) {
            return null;
        }
        return result.getComments();
    }
    //Dodawanie komentarzy
    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int partId, Comm comm) {

        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", partId)
                .getSingleResult();

        if (result == null)
            return Response.status(404).build();
        result.getComments().add(comm);
        comm.setPart(result);
        em.persist(comm);
        return Response.ok().build();
    }


    //Usuwanie komentarzy
    @DELETE
    @Path("/{id}/comments/{commentId}")
    public Response delete(@PathParam("id") int partId,
                           @PathParam("commentId") int commentId) {

        Part result = em.createNamedQuery("part.id", Part.class)
                .setParameter("partId", partId)
                .getSingleResult();

        if(result == null) {
            return Response.status(404).build();
        }

        Comm comment = em.createNamedQuery("comment.id", Comm.class)
                .setParameter("commentId", commentId)
                .getSingleResult();

        if(comment == null || comment.getPart().getId() != partId) {
            return Response.status(404).build();
        }

        result.getComments().remove(comment);
        em.remove(comment);

        return Response.ok().build();
    }


    //Wyszukiwanie produktu po zakresie cen
    @GET
    @Path("/price/{priceLow}/{priceHigh}")
    public Response getSortedByPrice(@PathParam("priceLow") int priceLow, @PathParam("priceHigh") int priceHigh) {

        List<Part> result = em.createNamedQuery("part.price", Part.class)
                .setParameter("priceLow", priceLow)
                .setParameter("priceHigh", priceHigh)
                .getResultList();


        if (result == null) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PartsResponse partsResponse = new PartsResponse();
        partsResponse.setParts(result);
        return Response.ok(partsResponse).build();
    }

    //Wyszukiwanie produktu po kategorii
    @GET
    @Path("/category/{category}")
    public Response getSortedByCategory(@PathParam("category") String category) {

        List<Part> result = em.createNamedQuery("part.category", Part.class)
                .setParameter("category", category)

                .getResultList();


        if (result == null) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PartsResponse partsResponse = new PartsResponse();
        partsResponse.setParts(result);
        return Response.ok(partsResponse).build();

    }
        //Wyszukiwanie kategorii po nazwie
        @GET
        @Path("/name/{name}")
        public Response getSortedByName(@PathParam("name") String name) {

            List<Part> result = em.createNamedQuery("part.name", Part.class)
                    .setParameter("name", name)
                    .getResultList();


            if (result == null) {

                return Response.status(Response.Status.NOT_FOUND).build();
            }

            PartsResponse partsResponse = new PartsResponse();
            partsResponse.setParts(result);
            return Response.ok(partsResponse).build();

    }
}

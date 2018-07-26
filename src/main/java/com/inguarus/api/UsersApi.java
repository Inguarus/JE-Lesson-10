package com.inguarus.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inguarus.dao.UserDao;
import com.inguarus.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UsersApi {

    private Gson gson = new GsonBuilder().create();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewUser(@FormParam("first_name") String firstName,
                           @FormParam("second_name") String secondName,
                           @FormParam("age") int age) {
        User user = new User(firstName, secondName, age);
        UserDao.getInstance().addUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        return gson.toJson(UserDao.getInstance().getUsers());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(String jsonUser) {
        try {
            User user = gson.fromJson(jsonUser, User.class);
            if (UserDao.getInstance().update(user)) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{firstName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@PathParam("first_name") String firstName) {
        if (UserDao.getInstance().remove(firstName)) {
            String json = "{\"result\" : \"Removed user with first name: " + firstName + "\"}";
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            String json = "{\"result\" : \"User not found: " + firstName + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(json).build();
        }
    }
}

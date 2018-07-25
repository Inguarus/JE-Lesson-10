package com.inguarus.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inguarus.dao.UserDao;
import com.inguarus.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Path("/users")
public class UsersApi {

    private Gson gson = new GsonBuilder().create();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewUser(@FormParam("first_name") String firstName,
                           @FormParam("second_name") String secondName,
                           @FormParam("age") int age) {
        User user = new User(firstName, secondName, age);

        String path = System.getProperty("java.io.tmpdir");
        System.out.println("UserApiTag path: " + path);

        File file = new File(path, "operations.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(user));
            System.out.println("UserApiTag file location: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("UserApiTag error: " + String.valueOf(e));
        }

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
            String json = "{\"result\" : \"Removed user with name: " + firstName + "\"}";
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            String json = "{\"result\" : \"User not found: " + firstName + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(json).build();
        }
    }
}

package se.ifmo.rest;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.ifmo.dto.ErrorResponse;
import se.ifmo.dto.TokenResponse;
import se.ifmo.dto.UserRequest;
import se.ifmo.entities.Users;
import se.ifmo.services.UserService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @EJB
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            userService = (UserService) ctx.lookup("java:module/UserService");
            if (userService == null) {
                logger.info("UserService is null in JNDI lookup.");
            } else {
                logger.info("UserService successfully found in JNDI.");
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(UserRequest request) {
        logger.info("Login request received");
        if (userService == null){
            logger.info("userService is null");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Users user = userService.authenticate(request.getUsername(), request.getPassword());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(new ErrorResponse("Invalid credentials"))
                           .build();
        }
        String token = JwtUtil.generateToken(request.getUsername());
        return Response.ok(new TokenResponse("Login successful", token)).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserRequest request) {
        logger.info("Register request received");

        if (request.getUsername() == null || request.getUsername().isEmpty() ||
            request.getPassword() == null || request.getPassword().isEmpty() ){
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorResponse("Username and password must not be empty"))
                           .build(); 
         }          
        boolean success = userService.registerUser(request.getUsername(), request.getPassword());
        if (!success) {
            return Response.status(Response.Status.CONFLICT)
            .entity(new ErrorResponse("Username already taken"))
            .build();
        }
        logger.info("User " + request.getUsername() + " registered");
        return Response.status(Response.Status.CREATED).build();
    }

}

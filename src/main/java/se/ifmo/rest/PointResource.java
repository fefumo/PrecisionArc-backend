package se.ifmo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.ifmo.dto.ErrorResponse;
import se.ifmo.dto.PointRequest;
import se.ifmo.dto.PointResponse;
import se.ifmo.models.Point;
import se.ifmo.models.Users;
import se.ifmo.services.PointService;
import se.ifmo.services.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@Path("/points")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointResource {

    @EJB
    private PointService pointService;

    @EJB
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(PointResource.class);


    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            userService = (UserService) ctx.lookup("java:module/UserService");
            pointService = (PointService) ctx.lookup("java:module/PointService");
            if (userService == null || pointService == null) {
                logger.info("UserService or PointService is null in JNDI lookup.");
            } else {
                logger.info("Services successfully found in JNDI.");
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPoint(PointRequest pointRequest, @HeaderParam("Authorization") String authToken) {
        long startTime = System.currentTimeMillis(); // Start time tracking

        try {
            logger.info("Received POST request: " + pointRequest);

            if (authToken == null || !authToken.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity(new ErrorResponse("Invalid or missing Authorization header"))
                               .build();
            }
            String token = authToken.substring(7);
            if (!JwtUtil.validateToken(token)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity(new ErrorResponse("Invalid token"))
                               .build();
            }

            String username = JwtUtil.extractUsername(token);
            Users user = userService.findUserByUsername(username);

            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity(new ErrorResponse("User not found"))
                               .build();
            }

            logger.info("Adding point for user: {}", username);

            Point point = pointService.savePoint(pointRequest, user);

            // Calculate elapsed time
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            String currentTimeStr = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(currentTime));

            PointResponse pointResponse = new PointResponse(
                point.getId(),
                point.getxCoord(),
                point.getyCoord(),
                point.getResult(),
                currentTimeStr,
                String.valueOf(elapsedTime)
            );

            return Response.status(Response.Status.CREATED).entity(pointResponse).build();
        } catch (Exception e) {
            logger.info("Error adding point: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid point data")).build();
        }
    }


    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserPoints(@HeaderParam("Authorization") String authToken) {
        try {
            if (authToken == null || !authToken.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ErrorResponse("Invalid or missing Authorization header"))
                            .build();
            }
            String token = authToken.substring(7);
            if (!JwtUtil.validateToken(token)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ErrorResponse("Invalid token"))
                            .build();
            }

            String username = JwtUtil.extractUsername(token);
            Users user = userService.findUserByUsername(username);

            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ErrorResponse("User not found"))
                            .build();
            }

            List<Point> points = pointService.getUserPoints(user);

            List<PointResponse> responseList = points.stream()
                .map(point -> new PointResponse(
                    point.getId(),
                    point.getxCoord(),
                    point.getyCoord(),
                    point.getResult(),
                    null, 
                    null  
                ))
                .collect(Collectors.toList());

            return Response.ok(responseList).build();
        } catch (Exception e) {
            logger.info("Error fetching points: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Error fetching points")).build();
        }
    }
}

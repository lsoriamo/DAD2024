package es.us.lsi.dad;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	private Map<Integer, UserEntity> users = new HashMap<Integer, UserEntity>();
	private Gson gson;

	public void start(Promise<Void> startFuture) {
		// Creating some synthetic data
		createSomeData(25);

		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		// Defining the router object
		Router router = Router.router(vertx);

		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/users* or /api/users/*
		router.route("/api/users*").handler(BodyHandler.create());
		router.get("/api/users").handler(this::getAllWithParams);
		router.get("/api/users/:userid").handler(this::getOne);
		router.post("/api/users").handler(this::addOne);
		router.delete("/api/users/:userid").handler(this::deleteOne);
		router.put("/api/users/:userid").handler(this::putOne);
	}

	@SuppressWarnings("unused")
	private void getAll(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new UserEntityListWrapper(users.values())));
	}

	private void getAllWithParams(RoutingContext routingContext) {
		final String name = routingContext.queryParams().contains("name") ? routingContext.queryParam("name").get(0) : null;
		final String surname = routingContext.queryParams().contains("surname") ? routingContext.queryParam("surname").get(0) : null;
		final String username = routingContext.queryParams().contains("username") ? routingContext.queryParam("username").get(0) : null;
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new UserEntityListWrapper(users.values().stream().filter(elem -> {
					boolean res = true;
					res = res && name != null ? elem.getName().equals(name) : true;
					res = res && surname != null ? elem.getSurname().equals(surname) : true;
					res = res && username != null ? elem.getUsername().equals(username) : true;
					return res;
				}).collect(Collectors.toList()))));
	}

	private void getOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("userid"));
		if (users.containsKey(id)) {
			UserEntity ds = users.get(id);
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(ds));
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}

	private void addOne(RoutingContext routingContext) {
		final UserEntity user = gson.fromJson(routingContext.getBodyAsString(), UserEntity.class);
		users.put(user.getIdusers(), user);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(user));
	}

	private void deleteOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("userid"));
		if (users.containsKey(id)) {
			UserEntity user = users.get(id);
			users.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(user));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}

	private void putOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("userid"));
		UserEntity ds = users.get(id);
		final UserEntity element = gson.fromJson(routingContext.getBodyAsString(), UserEntity.class);
		ds.setName(element.getName());
		ds.setSurname(element.getSurname());
		ds.setBirthdate(element.getBirthdate());
		ds.setPassword(element.getPassword());
		ds.setUsername(element.getUsername());
		users.put(ds.getIdusers(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}

	private void createSomeData(int number) {
		Random rnd = new Random();
		IntStream.range(0, number).forEach(elem -> {
			int id = rnd.nextInt();
			users.put(id, new UserEntity(id, "Nombre_" + id, "Apellido_" + id,
					new Date(Calendar.getInstance().getTimeInMillis() + id), "Username_" + id, "Password_" + id));
		});
	}

}

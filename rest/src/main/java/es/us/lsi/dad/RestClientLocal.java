package es.us.lsi.dad;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class RestClientLocal extends AbstractVerticle {

	// Web client instance
	private WebClient client;
	
	// Instantiating a Gson serialize object using specific date format
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public void start(Promise<Void> startFuture) {
		WebClientOptions options = new WebClientOptions().setUserAgent("RestClientApp/2.0.2.1");
		
		// Optimization for servlet-based connection (state-less)
		options.setKeepAlive(false);
		client = WebClient.create(vertx, options);

		/*
		 * Get resource list operation
		 */
		Promise<UserEntityListWrapper> resList = Promise.promise();
		resList.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource list obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource list not obtained");
				System.out.println(complete.cause().toString());
			}
		}).onSuccess(success -> {
			// System.out.println(success.toString());
		}).onFailure(failure -> {
			// System.out.println(failure.toString());
		});
		
		getRequest(8080, "http://localhost", "api/users", UserEntityListWrapper.class, resList);

		/*
		 * Post resource operation
		 */
		Promise<UserEntity> resPost = Promise.promise();
		resPost.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource added");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not added");
				System.out.println(complete.cause().toString());
			}
		});

		postRequest(8080, "http://localhost", "api/users",
				new UserEntity(3, "Nuevo", "Usuario", Calendar.getInstance().getTime(), "nuevo_usuario", "pass"),
				UserEntity.class, resPost);

		/*
		 * Get single resource operation
		 */
		Promise<UserEntity> res = Promise.promise();
		res.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not obtained");
				System.out.println(complete.cause().toString());
			}
		});

		getRequest(8080, "http://localhost", "api/users/3", UserEntity.class, res);

		/*
		 * Put resource operation
		 */
		Promise<UserEntity> resPut = Promise.promise();
		resPut.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource modified");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not added");
				System.out.println(complete.cause().toString());
			}
		});

		putRequest(8080, "http://localhost", "api/users/3", new UserEntity(3, "Nuevo", "Usuario_modificado",
				Calendar.getInstance().getTime(), "nuevo_usuario", "pass"), UserEntity.class, resPut);

		/*
		 * Get resource operation using params
		 */
		Promise<UserEntityListWrapper> resWithParams = Promise.promise();
		resWithParams.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource with params obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource with params not obtained");
				System.out.println(complete.cause().toString());
			}
		});
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "Nuevo");
		getRequestWithParams(8080, "http://localhost", "api/users", UserEntityListWrapper.class, resWithParams, params);

		/*
		 * Delete resource operation
		 */
		Promise<UserEntity> resDelete = Promise.promise();
		resDelete.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource deleted");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not deleted");
				System.out.println(complete.cause().toString());
			}
		});

		deleteRequest(8080, "http://localhost", "api/users/3", UserEntity.class, resDelete);

		/*
		 * Combining several promises
		 */
		Promise<UserEntity> resPost2 = Promise.promise();
		Promise<UserEntity> resPost3 = Promise.promise();
		Promise<UserEntity> resPost4 = Promise.promise();
		postRequest(8080, "http://localhost", "api/users",
				new UserEntity(3, "Nuevo3", "Usuario3", Calendar.getInstance().getTime(), "nuevo_usuario3", "pass"),
				UserEntity.class, resPost2);
		postRequest(8080, "http://localhost", "api/users",
				new UserEntity(4, "Nuevo4", "Usuario4", Calendar.getInstance().getTime(), "nuevo_usuario4", "pass"),
				UserEntity.class, resPost3);
		postRequest(8080, "http://localhost", "api/users",
				new UserEntity(5, "Nuevo5", "Usuario5", Calendar.getInstance().getTime(), "nuevo_usuario5", "pass"),
				UserEntity.class, resPost4);

		CompositeFuture.all(resPost2.future(), resPost3.future(), resPost4.future())
				.onComplete(new Handler<AsyncResult<CompositeFuture>>() {

					@Override
					public void handle(AsyncResult<CompositeFuture> event) {
						System.out.println("-----------------------------------------------------------");
						System.out.println("Composite futures");
						System.out.println(resPost2.future().result());
						System.out.println(resPost3.future().result());
						System.out.println(resPost4.future().result());

					}
				});

	}

	/**
	 * Get request utility
	 * 
	 * @param <T>       Type of result enveloped in JSON response
	 * @param port      Port
	 * @param host      Host address
	 * @param resource  URI where resource is provided
	 * @param classType Type of result enveloped in JSON response
	 * @param promise   Promise to be executed on call finish
	 */
	public <T> void getRequest(Integer port, String host, String resource, Class<T> classType, Promise<T> promise) {
		client.getAbs(host + ":" + port + "/" + resource).send(elem -> {
			if (elem.succeeded()) {
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	/**
	 * Get request utility
	 * 
	 * @param <T>       Type of result enveloped in JSON response
	 * @param port      Port
	 * @param host      Host address
	 * @param resource  URI where resource is provided
	 * @param classType Type of result enveloped in JSON response
	 * @param promise   Promise to be executed on call finish
	 * @param params    Map with key-value entries for call parameters
	 */
	public <T> void getRequestWithParams(Integer port, String host, String resource, Class<T> classType,
			Promise<T> promise, Map<String, String> params) {
		HttpRequest<Buffer> httpRequest = client.getAbs(host + ":" + port + "/" + resource);

		params.forEach((key, value) -> {
			httpRequest.addQueryParam(key, value);
		});

		httpRequest.send(elem -> {
			if (elem.succeeded()) {
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	/**
	 * Post request utility
	 * 
	 * @param <B>       Type of body enveloped in JSON request
	 * @param <T>       Type of result enveloped in JSON response
	 * @param port      Port
	 * @param host      Host address
	 * @param resource  URI where resource is provided
	 * @param classType Type of result enveloped in JSON response
	 * @param promise   Promise to be executed on call finish
	 */
	public <B, T> void postRequest(Integer port, String host, String resource, Object body, Class<T> classType,
			Promise<T> promise) {
		JsonObject jsonBody = new JsonObject(gson.toJson(body));
		client.postAbs(host + ":" + port + "/" + resource).sendJsonObject(jsonBody, elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});
	}

	/**
	 * Put request utility
	 * 
	 * @param <B>       Type of body enveloped in JSON request
	 * @param <T>       Type of result enveloped in JSON response
	 * @param port      Port
	 * @param host      Host address
	 * @param resource  URI where resource is provided
	 * @param classType Type of result enveloped in JSON response
	 * @param promise   Promise to be executed on call finish
	 */
	public <B, T> void putRequest(Integer port, String host, String resource, Object body, Class<T> classType,
			Promise<T> promise) {
		JsonObject jsonBody = new JsonObject(gson.toJson(body));
		client.putAbs(host + ":" + port + "/" + resource).sendJsonObject(jsonBody, elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});
	}

	/**
	 * Delete request utility
	 * 
	 * @param <T>       Type of result enveloped in JSON response
	 * @param port      Port
	 * @param host      Host address
	 * @param resource  URI where resource is provided
	 * @param classType Type of result enveloped in JSON response
	 * @param promise   Promise to be executed on call finish
	 */
	public <T> void deleteRequest(Integer port, String host, String resource, Class<T> classType, Promise<T> promise) {
		client.deleteAbs(host + ":" + port + "/" + resource).send(elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

}

package es.us.lsi.dad;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class RestClient extends AbstractVerticle {

	private WebClient client;

	public void start(Promise<Void> startFuture) {
		WebClientOptions options = new WebClientOptions().setUserAgent("RestClientApp/2.0.2.1");
		options.setKeepAlive(false);
		client = WebClient.create(vertx, options);

		Promise<List> resList = Promise.promise();
		resList.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("GetAll:");
				complete.result().stream().forEach(elem -> {
					System.out.println((new Gson()).fromJson(elem.toString(), UserEntity.class));
				});
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		getRequest(443, "https://extendsclass.com", "mock/rest/2834f21dcd9cb6241c0c5eae0d2128f9/users", List.class,
				resList);

		Promise<UserEntity> res = Promise.promise();
		res.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("GetOne");
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		getRequest(443, "https://extendsclass.com", "mock/rest/2834f21dcd9cb6241c0c5eae0d2128f9/users/123",
				UserEntity.class, res);

		Promise<UserEntity> resWithParams = Promise.promise();
		resWithParams.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});
		Map<String, String> params = new HashMap<String, String>();
		params.put("iduser", "3");
		params.put("otroparam", "123123");
		params.put("otroparam2", "hola");
		getRequestWithParams(443, "https://extendsclass.com", "mock/rest/2834f21dcd9cb6241c0c5eae0d2128f9/users/123",
				UserEntity.class, resWithParams, params);

		Promise<UserEntity> resPost = Promise.promise();
		resPost.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("Post One");
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		postRequest(443, "https://extendsclass.com", "mock/rest/2834f21dcd9cb6241c0c5eae0d2128f9/users",
				new UserEntity(3, "Nuevo", "Usuario", Calendar.getInstance().getTime(), "nuevo_usuario", "pass"),
				UserEntity.class, resPost);

		/*
		 * Promise<PostResponse> resDelete = Promise.promise();
		 * resDelete.future().onComplete(complete -> { if (complete.succeeded()) {
		 * System.out.println(complete.result().toString()); } else {
		 * System.out.println(complete.cause().toString()); } });
		 * 
		 * deleteRequest(443, "https://extendsclass.com",
		 * "mock/rest/2834f21dcd9cb6241c0c5eae0d2128f9/user", new UsuarioImpl(3,
		 * "Nuevo", "Usuario", Calendar.getInstance().getTime(), "nuevo_usuario",
		 * "pass"), PostResponse.class, resDelete);
		 */
	}

	public <T> void getRequest(Integer port, String host, String resource, Class<T> classType, Promise<T> promise) {
		HttpRequest<Buffer> httpRequest = client.getAbs(host + ":" + port + "/" + resource);

		httpRequest.send(elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	public <T> void getRequestWithParams(Integer port, String host, String resource, Class<T> classType,
			Promise<T> promise, Map<String, String> params) {
		HttpRequest<Buffer> httpRequest = client.getAbs(host + ":" + port + "/" + resource);

		params.forEach((key, value) -> {
			httpRequest.addQueryParam(key, value);
		});

		httpRequest.send(elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classType));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	public <B, T> void postRequest(Integer port, String host, String resource, B body, Class<T> classTypeRes,
			Promise<T> promise) {
		Gson gsonConverter = new Gson();
		JsonObject jsonBody = new JsonObject(gsonConverter.toJson(body));
		client.postAbs(host + ":" + port + "/" + resource).sendJsonObject(jsonBody, elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classTypeRes));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	public <B, T> void putRequest(Integer port, String host, String resource, B body, Class<T> classTypeRes,
			Promise<T> promise) {
		Gson gsonConverter = new Gson();
		JsonObject jsonBody = new JsonObject(gsonConverter.toJson(body));
		client.putAbs(host + ":" + port + "/" + resource).sendJsonObject(jsonBody, elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classTypeRes));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

	public <B, T> void deleteRequest(Integer port, String host, String resource, Object body, Class<T> classTypeRes,
			Promise<T> promise) {
		Gson gsonConverter = new Gson();
		JsonObject jsonBody = new JsonObject(gsonConverter.toJson(body));
		client.deleteAbs(host + ":" + port + "/" + resource).sendJsonObject(jsonBody, elem -> {
			if (elem.succeeded()) {
				Gson gson = new Gson();
				promise.complete(gson.fromJson(elem.result().bodyAsString(), classTypeRes));
			} else {
				promise.fail(elem.cause());
			}
		});

	}

}

package es.us.lsi.dad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletSimple extends HttpServlet {

	private static final long serialVersionUID = 861415467074580048L;

	private String message;
	private int number;

	public void init() throws ServletException {
		message = "Hola mundo";
		number = 0;
		//throw new ServletException();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String message2 = Calendar.getInstance().getTime().toString();
		PrintWriter out = response.getWriter();
		number++;
		out.println("<body><h1>" + message2 + "</h1><h2>" + number +
				 "</h2></body>");
	}

	public void destroy() {
		// do nothing.
	}
}
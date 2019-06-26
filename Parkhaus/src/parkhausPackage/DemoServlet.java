package parkhausPackage;

import javax.servlet.ServletContext;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

//@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> autos = new ArrayList<>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if ("sum".equals(request.getParameter("cmd"))) {
			Float sum = getPersistentSum();
			out.println(sum);
			System.out.println("sum = " + sum);
		} else if ("avg".equals(request.getParameter("cmd"))) {
			Float avg = (getPersistentSum() / getPersistentNr());
			out.println(avg);
			System.out.println("avg = " + avg);

		} else if ("left".equals(request.getParameter("cmd"))) {
			Float left = getPersistentNr();
			out.println(left);
			System.out.println("left = " + left);

		} else if ("config".equals(request.getParameter("cmd"))) {
			// Overwrite config parameters
			// Maximum number of cars, open_from, open_to, delay, simulation_speed
			// getApplication().setAttribute("Max",30);
			out.println("50,0,24,100,10");

		} else if ("cars".equals(request.getParameter("cmd"))) {
			if (autos.isEmpty() == true) {
				out.println("");
			} else {
				String carsBack = String.join(",", autos);
				out.println(carsBack);
				System.out.println(carsBack);
			}
		}  else {
			System.out.println("Invalid Command: " + request.getQueryString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Float sum = getPersistentSum();
		Float nr = getPersistentNr();
		String body = getBody(request);
		System.out.println(body);

		String[] params = body.split(",");
		String event = params[0];
		String temp = "";
		if ("enter".equals(event)) {
			for (int i = 1; i < params.length; i++) {
				temp += params[i];
				if (i != params.length - 1) {
					temp += "/";
				}
			}
			autos.add(temp);
		}
		if ("leave".equals(event)) {
			for (int i = 0; i < autos.size(); i++) {
				if(autos.get(i).contains(params[5])) {
					autos.remove(i);
				}
			}
			String priceString = params[4];
			// strip € in front, parse the number behind
			float price = Float.parseFloat(priceString) / 100;
			System.out.println(price);
			sum += price;
			System.out.println(sum);
			nr += 1;
			// store sum persistently in ServletContext
			getApplication().setAttribute("sum", sum);
			getApplication().setAttribute("nr", nr);
		}
	}

	private ServletContext getApplication() {
		return getServletConfig().getServletContext();
	}

	private Float getPersistentSum() {
		Float sum;
		ServletContext application = getApplication();
		sum = (Float) application.getAttribute("sum");
		if (sum == null)
			sum = 0.0f;
		return sum;
	}
	
	

	private Float getPersistentNr() {
		Float nr;
		ServletContext application = getApplication();
		nr = (Float) application.getAttribute("nr");
		if (nr == null)
			nr = 0.0f;
		return nr;
	}

	private static String getBody(HttpServletRequest request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return stringBuilder.toString();
	}

}

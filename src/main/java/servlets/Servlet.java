package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import jdbc.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/controlador")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;

	@Override
	public void init() throws ServletException {
		super.init();
		Conexion.setURL("jdbc:mysql://172.16.0.25/dbalumnos?user=mp7&password=secreto");
		conn = Conexion.getConexion();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer;
		String order;
		String sql;

		order = request.getParameter("order");
		writer = response.getWriter();
		response.setContentType("text/html");

		sql = request.getParameter("sql");

		try (Statement stmt = conn.createStatement()) {
			int	i;
			int columnCount;

			ResultSet rs = stmt.executeQuery(sql);
			columnCount = rs.getMetaData().getColumnCount();

			writer.println("<html><body style=\"background-color:#ffff9d\">");
			writer.println("<center><h2 style=\"color:#00007e\">Usa JDBC para recuperar registros de una tabla</h2></center><hr style=\"color:#00007e\">");
			writer.println("<table style=\"color:#00007e\" border=\"1px solid\"><tr>");

			i = 1;
			while (i <= columnCount)
				writer.println("<th>" + rs.getMetaData().getColumnName(i++) + "</th>");
			writer.println("</tr>");

			while (rs.next())
			{
				writer.println("<tr>");

				i = 1;
				while (i <= columnCount)
					writer.println("<td>" + rs.getString(i++) + "</td>");
				writer.println("</tr>");
			}

			writer.println("</table>");
			writer.println("</body></html>");
		}  catch (SQLException e) {
			writer.println("<html><body style=\"background-color:#ffff9d\"><center><h1 style=\"color:#00007e\">Usa JDBC para recuperar registros de una tabla</h1></center><hr style=\"color:#00007e\"><p>Error al ejecutar la consulta: <strong style=\"color:#FF0000\">" + e.getMessage() + "</strong></p></body></html>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer;
		String order;
		String idStr;
		String name;
		String course;
		String sql;

		order = request.getParameter("order");
		writer = response.getWriter();
		response.setContentType("text/html");

		try {
			if ("Dar de alta".equals(order)) {
				idStr = request.getParameter("id");
				name = request.getParameter("name");
				course = request.getParameter("course");

				sql = "INSERT INTO alumnos (id, curso, nombre) VALUES (?, ?, ?)";

				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setInt(1, Integer.parseInt(idStr));
					ps.setString(2, course);
					ps.setString(3, name);

					int result = ps.executeUpdate();
					writer.println("<html><body style=\"background-color:#ffff9d\">");

					if (result > 0) {
						writer.println("<center><h1 style=\"color:#00007e\">Usa JDBC para grabar un registro en una tabla</h1></center>");
						writer.println("<hr style=\"color:#00007e\"><p style=\"color:#00007e\">Filas afectadas: " + result + "</p>");
					} else {
						writer.println("<h2>Error: No se pudo dar de alta el alumno.</h2>");
					}
					writer.println("</body></html>");
				}
			}
		}
		catch (SQLException e) {
			writer.println("<html><body style=\"background-color:#ffff9d\"><center><h1 style=\"color:#00007e\">Usa JDBC para recuperar registros de una tabla</h1></center><hr style=\"color:#00007e\"><p>Error al ejecutar la consulta: <strong style=\"color:#FF0000\">" + e.getMessage() + "</strong></p></body></html>");
		}
		catch (NumberFormatException e) {
			writer.println("<html><body style=\"background-color:#ffff9d\"><center><h1 style=\"color:#00007e\">Usa JDBC para recuperar registros de una tabla</h1></center><hr style=\"color:#00007e\"><p>Error al ejecutar la consulta: <strong style=\"color:#FF2000\"> " + e.getMessage() + "<br>El ID debe ser un numero.</strong></p><br></body></html>");
		}
	}


}

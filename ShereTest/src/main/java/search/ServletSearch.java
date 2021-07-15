package search;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ServletSearch")
public class ServletSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub]

		response.setContentType("text/html; charset=Windows-31J");
		PrintWriter out = response.getWriter();

		String url = "jdbc:oracle:thin:@DESKTOP-HSUMK1U:1521/xepdb1";
		String user = "MY_USER";
		String pass = "password";
		String strSQL = "Select * From m_MemberList Where";


		if(request.getParameter("no") != null && request.getParameter("no") != "" ){
			strSQL = strSQL + " no Like '%" + request.getParameter("no") + "%' AND";
		}else{
			strSQL = strSQL + " no Like '%' AND";
		}	

		if(request.getParameter("name") != null && request.getParameter("name") != "" ){
			strSQL = strSQL + " CONCAT(CONCAT(CONCAT(sei,mei),sei_furi),mei_furi) Like '%" + request.getParameter("name") + "%' AND";
		}else{
			strSQL = strSQL + " CONCAT(CONCAT(CONCAT(sei,mei),sei_furi),mei_furi) Like '%' AND";
		}

		if(request.getParameter("age_low") != null && request.getParameter("age_low") != "" ){
			strSQL = strSQL + " EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM birthday) - CASE WHEN TO_CHAR(SYSDATE, 'MMDD') < TO_CHAR(birthday, 'MMDD') THEN 1 ELSE 0 END BETWEEN " + request.getParameter("age_low") + " AND";
		}else{
			strSQL = strSQL + " EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM birthday) - CASE WHEN TO_CHAR(SYSDATE, 'MMDD') < TO_CHAR(birthday, 'MMDD') THEN 1 ELSE 0 END BETWEEN 0 AND";
		}

		if(request.getParameter("age_up") != null && request.getParameter("age_up") != "" ){
			strSQL = strSQL + " " + request.getParameter("age_up") + " AND";
		}else{
			strSQL = strSQL + " 99 AND";
		}

		if(request.getParameter("post") != null && request.getParameter("post") != "" ){
			strSQL = strSQL + " post Like '%" + request.getParameter("post") + "%' AND";
		}else{
			strSQL = strSQL + " post Like '%' AND";
		}

		if(request.getParameter("address") != null && request.getParameter("address") != "" ){
			strSQL = strSQL + " address Like '%" + request.getParameter("address") + "%' AND";
		}else{
			strSQL = strSQL + " address Like '%' AND";
		}

		if(request.getParameter("hiredate_low") != null && request.getParameter("hiredate_low") != "" ){
			strSQL = strSQL + " hiredate BETWEEN to_date('" + request.getParameter("hiredate_low") + "', 'yyyy-mm-dd') AND";
		}else{
			strSQL = strSQL + " hiredate BETWEEN to_date('0001-01-01', 'yyyy-mm-dd') AND";
		}

		if(request.getParameter("hiredate_up") != null && request.getParameter("hiredate_up") != "" ){
			strSQL = strSQL + " to_date('" + request.getParameter("hiredate_up") + "', 'yyyy-mm-dd') AND";
		}else{
			strSQL = strSQL + " to_date('9999-12-31', 'yyyy-mm-dd') AND";
		}

		if(request.getParameter("years_low") != null && request.getParameter("years_low") != "" ){
			strSQL = strSQL + " EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM hiredate) - CASE WHEN TO_CHAR(SYSDATE, 'MMDD') < TO_CHAR(hiredate, 'MMDD') THEN 1 ELSE 0 END BETWEEN " + request.getParameter("years_low") + " AND";
		}else{
			strSQL = strSQL + " EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM hiredate) - CASE WHEN TO_CHAR(SYSDATE, 'MMDD') < TO_CHAR(hiredate, 'MMDD') THEN 1 ELSE 0 END BETWEEN 0 AND";
		}

		if(request.getParameter("years_up") != null && request.getParameter("years_up") != "" ){
			strSQL = strSQL + " " + request.getParameter("years_up") + "";
		}else{
			strSQL = strSQL + " 99";
		}

		try (Connection con = DriverManager.getConnection(url, user, pass);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(strSQL);) {

			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div>");
			out.println("<table border=5 align=center>");
			out.println("<tr align=left>");
			out.println("<th>社員番号</th>");
			out.println("<th>氏名</th>");
			out.println("<th>ふりがな</th>");
			out.println("<th>生年月日</th>");
			out.println("<th>年齢</th>");
			out.println("<th>郵便番号</th>");
			out.println("<th>住所</th>");
			out.println("<th>入社日</th>");
			out.println("<th>勤続年数</th>");
			out.println("</tr>");

			SimpleDateFormat birth = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat hire = new SimpleDateFormat("yyyy/MM/dd");
			LocalDate nowdate = LocalDate.now();

			while (rs.next()) {
				String birthday = birth.format(rs.getDate("birthday"));
				String hiredate = hire.format(rs.getDate("hiredate"));

				LocalDate start = LocalDate.of(Integer.parseInt(birthday.substring(0, 4)),Integer.parseInt(birthday.substring(5, 7)),Integer.parseInt(birthday.substring(8, 10)));
				LocalDate end = LocalDate.of(nowdate.getYear(),nowdate.getMonth(),nowdate.getDayOfMonth());
				long age = ChronoUnit.YEARS.between(start, end);
				start = LocalDate.of(Integer.parseInt(hiredate.substring(0, 4)),Integer.parseInt(hiredate.substring(5, 7)),Integer.parseInt(hiredate.substring(8, 10)));
				long work_years = ChronoUnit.YEARS.between(start, end);
				long work_month = ChronoUnit.MONTHS.between(start, end);
				work_month = work_month - (work_years * 12);

				out.println("<tr align=left>");
				out.println("<td>");
				out.println(rs.getInt("no"));
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString("sei") + " " + rs.getString("mei"));
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString("sei_furi") + " " + rs.getString("mei_furi"));
				out.println("</td>");
				out.println("<td>");
				out.println(birthday);
				out.println("</td>");
				out.println("<td>");
				out.println(age);
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString("post"));
				out.println("</td>");
				out.println("<td>");
				out.println(rs.getString("address"));
				out.println("</td>");
				out.println("<td>");
				out.println(hiredate);
				out.println("</td>");
				out.println("<td>");
				out.println(work_years + "年" + work_month + "ヵ月");
				out.println("</td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</body>");
			out.println("</html>");

			
		} catch (Exception e) {
			e.printStackTrace();
			out.println(e);
		}
	}
}
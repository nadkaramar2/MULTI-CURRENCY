package ams.cms.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.JsonObject;

@Component
public class MontraAuthenticationFilter //extends OncePerRequestFilter 
{
	//@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
	{
		System.out.println("Hello Moto");
		try 
		{
			boolean isErrorFree = true;
			if (isErrorFree) 
			{
				 filterChain.doFilter(request, response); 
			}
			else
			{
				JsonObject resObject = new JsonObject();
				resObject.addProperty("code", "E0000");
				resObject.addProperty("message", "Error Occured");
				resObject.addProperty("status", "failed");				 
				
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(resObject);
				out.flush();
			}
			 
			// response.getOutputStream().write(inputStreamBytes);

			 //response.getOutputStream().println(new ObjectMapper().writeValueAsString(data));
			
			//
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}

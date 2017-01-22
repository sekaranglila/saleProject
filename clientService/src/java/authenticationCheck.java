/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import identityConnect.identityConnect;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

/**
 *
 * @author Kris
 */
@WebFilter(filterName = "authenticationCheck", urlPatterns = {"/*"})
public class authenticationCheck implements Filter {
    private ServletContext context;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        this.context.log("RequestLoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            HttpSession sessionHTTP = servletRequest.getSession();
            String URI = servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
            JSONObject obj = null;
            String keyword = servletRequest.getParameter("keyword");
            String typePost = servletRequest.getParameter("searchButtom");
            
            if (sessionHTTP.getAttribute("token") != null)
            {
                if (!"search".equals(typePost) && !"logout".equals(servletRequest.getParameter("logout")))
            {
                //response.getWriter().println(sessionHTTP.getAttribute("token").toString());
                int uid = Integer.parseInt(sessionHTTP.getAttribute("uid").toString());
                String ipAddress = servletRequest.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = servletRequest.getRemoteAddr();
                }
                String uagent = servletRequest.getHeader("User-Agent");
                String gab = ipAddress + "#" + uagent;
                obj = identityConnect.validateToken(sessionHTTP.getAttribute("token").toString(), uid,gab);
                //response.getWriter().println(obj);
                if (!"validToken".equals(obj.get("status").toString())) {
                    sessionHTTP.invalidate();
                    servletRequest.getSession().setAttribute("status", "endSession");
                    servletRequest.getSession().setAttribute("detail", obj.get("detail"));
                    servletResponse.sendRedirect("login.jsp");
                    return;
                }
                else {
                    servletRequest.getSession().setAttribute("token", obj.get("token").toString());
                    servletRequest.getSession().setAttribute("uid", uid);
                    JSONObject ujs = identityConnect.getUser(uid);
                    if ( "/".equals(URI) || "/index.jsp".equals(URI) || "/login.jsp".equals(URI)) {
                        servletResponse.sendRedirect("catalogPage.jsp");
                        return;
                    }
                }
            }
            }
            else {
                if ("GET".equals(servletRequest.getMethod())) {
                    if (!"/login.jsp".equals(URI) && !"/register.jsp".equals(URI)) {
                        servletResponse.sendRedirect("login.jsp");
                        return;
                    }
                }
                else if ("POST".equals(servletRequest.getMethod())) {
                    if (!"/loginServlet".equals(URI) && !"/registerServlet".equals(URI) && !"/login".equals(URI) && !"/register".equals(URI)) {
                        servletResponse.sendRedirect("login.jsp");
                        return;
                    }   
                }
            }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }
    
}

package com.example.ShopAcc.config;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.example.ShopAcc.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Order(1)
public class Filter_Config implements Filter {
    private static final boolean debug = true;
    private FilterConfig filterConfig = null;
    public Filter_Config() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("NewFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("NewFilter:DoAfterProcessing");
        }
    }
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("NewFilter:doFilter()");
        }

        doBeforeProcessing(request, response);
        //--------
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String uri;
        uri = req.getServletPath();
        User user = (User) session.getAttribute("infor");
        if(uri.equals("http://localhost:8080/payment") )
        {
            resp.sendRedirect("http://localhost:8080/payment/create_payment");
            return;
        }
        if(user == null &&(uri.contains("admin")|| uri.contains("profile") || uri.contains("cart") || uri.contains("payment")|| uri.contains("game")))
        {
            resp.sendRedirect("http://localhost:8080/home");
            return;
        }

        if (user != null && (!user.getRoleID().getRoleName().equals("ADMIN") && uri.contains("admin")))
        {
            resp.sendRedirect("http://localhost:8080/home");
            return;
        }
        String id_init = request.getParameter("id");
        if(id_init != null ) {
            if( user != null)
            if (!user.getRoleID().getRoleName().equals("ADMIN") && uri.contains("profile")) {
                int id = 0;
                try {
                    id = Integer.parseInt(id_init);
                } catch (Exception e) {
                    resp.sendRedirect("http://localhost:8080/home");
                    return;
                }
                if (user.getId() != id)
                    resp.sendRedirect("http://localhost:8080/home");
            }
        }



        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);
    }

    public void destroy() {
    }
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("NewFilter:Initializing filter");
            }
        }
    }
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("NewFilter()");
        }
        StringBuffer sb = new StringBuffer("NewFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}

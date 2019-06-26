package parkhausPackage;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

@WebFilter( urlPatterns = "/*" )
public class SimpleCORSFilter implements Filter {
@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws
IOException, ServletException {
	HttpServletResponse response = (HttpServletResponse) res;
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "origin, x-requested-with,content-type");
	chain.doFilter(req, res);
}
@Override
public void destroy() {}
@Override
public void init(FilterConfig arg0) throws ServletException {}
}

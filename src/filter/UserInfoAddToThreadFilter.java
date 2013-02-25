package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import model.user.User;

public class UserInfoAddToThreadFilter implements Filter {

  public static ThreadLocal<User> threadLocal = new ThreadLocal<User>();

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    User user = (User) httpServletRequest.getSession().getAttribute("user");
    if (user == null)
      threadLocal.set(null);
    else
      threadLocal.set(user);
    chain.doFilter(request, response);
    threadLocal.set(null);
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void finalize() throws Throwable {
	threadLocal.set(null);
  }
}

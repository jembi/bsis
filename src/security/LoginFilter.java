package security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import repository.UserRepository;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final String DEFAULT_FILTER_PROCESSES_URL = "/login";
  private static final String POST = "POST";

  @Autowired
  private UserRepository userRepository;
  
  public LoginFilter () {
    super(DEFAULT_FILTER_PROCESSES_URL);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
    HttpServletResponse response) {
    System.out.println("here");
    System.out.println("here1");
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
  
    String username = request.getParameter("j_username");
    String password = request.getParameter("j_password");
  
    if (username == null) {
        username = "";
    }
  
    if (password == null) {
        password = "";
    }
  
    username = username.trim();
  
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
  
    // Allow subclasses to set the "details" property
//    setDetails(request, authRequest);
  
    return this.getAuthenticationManager().authenticate(authRequest);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res,
    FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;
    if(request.getMethod().equals(POST)) {
      // If the incoming request is a POST, then we send it up
      // to the AbstractAuthenticationProcessingFilter.
      super.doFilter(request, response, chain);
    } else {
      // If it's a GET, we ignore this request and send it
      // to the next filter in the chain.  In this case, that
      // pretty much means the request will hit the /login
      // controller which will process the request to show the
      // login page.
      chain.doFilter(request, response);
    }
  }

}
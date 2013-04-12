package interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.user.Permission;
import model.user.Role;
import model.user.User;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInfoInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void postHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler, ModelAndView mv) {
    System.out.println("here");
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    User user = (User) httpServletRequest.getSession().getAttribute("user");
    System.out.println(user);
    if (user == null)
      return;
    System.out.println(user.getUsername());
    Map<String, String> permissions = new HashMap<String, String>();
    for(Role role : user.getRoles()) {
      System.out.println(role.getName());
      for (Permission perm : role.getPermissions()) {
        System.out.println(perm.getName());
        permissions.put(perm.getName(), "allowed");
      }
    }
    mv.addObject("permissions", permissions);
  }
}


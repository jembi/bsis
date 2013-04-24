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
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    User user = (User) httpServletRequest.getSession().getAttribute("user");
    if (user == null)
      return;
    Map<String, String> permissions = new HashMap<String, String>();
    for(Role role : user.getRoles()) {
      for (Permission perm : role.getPermissions()) {
        permissions.put(perm.getName(), "allowed");
      }
    }
    if (mv != null)
      mv.addObject("permissions", permissions);
  }
}

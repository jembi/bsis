package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.user.User;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	private String urlParameterName = "targetUrl";

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = request.getServletPath();
		if (url.equals("/loginUser.html"))
			return true;

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			String query = request.getQueryString();
			ModelAndView modelAndView = new ModelAndView("login");
			if (query != null)
				modelAndView.addObject(urlParameterName, url + "?" + query);
			else
				modelAndView.addObject(urlParameterName, url);
			throw new ModelAndViewDefiningException(modelAndView);
		} else {
			return checkForAdmin(url, user);
		}
	}

	private boolean checkForAdmin(String url, User user)
			throws ModelAndViewDefiningException {
		if (url.startsWith("/admin-")) {
			if (user.getIsAdmin()) {
				return true;
			} else {
				ModelAndView modelAndView = new ModelAndView("adminAccessOnly");
				throw new ModelAndViewDefiningException(modelAndView);
			}
		}
		return true;
	}
}

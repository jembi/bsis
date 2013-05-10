package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import repository.GenericConfigRepository;
import repository.LoginRepository;

@Controller
public class LoginController {

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @RequestMapping("/login")
  public ModelAndView login(HttpServletRequest request) {
    return new ModelAndView("login");
  }

  @RequestMapping("/welcomePage")
  public ModelAndView welcome(HttpServletRequest request, Model m) {
    System.out.println("here");
    System.out.println("user");
    System.out.println(request.getSession().getAttribute("user"));
    System.out.println(request.getUserPrincipal());
    System.out.println(request.getUserPrincipal().getName());
    ModelAndView mv = new ModelAndView("welcomePage");
    m.addAttribute("versionNumber", UtilController.VERSION_NUMBER);
    m.addAttribute("labsetup", genericConfigRepository.getConfigProperties("labsetup"));
    mv.addObject("model", m);
    return mv;
  }

//  @RequestMapping(value="/j_spring_security_check", method=RequestMethod.POST)
//  public ModelAndView addDonor(@RequestParam Map<String, String> params,
//      HttpServletRequest request, HttpServletResponse response)
//      throws IOException {
//
//    String username = params.get("username");
//    String password = params.get("password");
//    String targetUrl = params.get("targetUrl");
//    User user = loginRepository.getUser(username);
//    if (user != null && password.equals(user.getPassword())) {
//      request.getSession().setAttribute("user", user);
//
//      String redirectPage = "/v2v/welcomePage.html";
////      if (firstTimeConfigController.isFirstTimeConfig()) {
////        if (user.getIsAdmin()) {
////          redirectPage = "/v2v/firstTimeConfig.html";
////        }
////        else {
////          redirectPage = "/v2v/firstTimeConfigNotAllowed.html";
////        }
////      }
//      response.sendRedirect(redirectPage);
//    }
//    ModelAndView modelAndView = new ModelAndView("login");
//
//    Map<String, Object> model = new HashMap<String, Object>();
//    model.put("loginFailed", true);
//    modelAndView.addObject("targetUrl", targetUrl);
//    modelAndView.addObject("model", model);
//    return modelAndView;
//  }

  @RequestMapping("/logout")
  public ModelAndView logout(HttpServletRequest request) {

    request.getSession().removeAttribute("user");
    request.getSession().removeAttribute("donor");
    return new ModelAndView("login");
  }

  @RequestMapping("/adminAccessOnly")
  public ModelAndView adminAccessError(HttpServletRequest request) {

    return new ModelAndView("adminAccessOnly");
  }
}

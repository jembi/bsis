package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public ModelAndView login(HttpServletRequest request,
      @RequestParam(value="error", required=false) boolean error) {
    ModelAndView mv = new ModelAndView("login");
    if (error)
      mv.addObject("login_error", true);
    return mv;
  }

  @RequestMapping("/welcomePage")
  public ModelAndView welcomePage(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("welcomePage");
    // for showing the version number on the home page. see welcomePage.jsp and topPanel.jsp.
    mv.addObject("versionNumber", UtilController.VERSION_NUMBER);
    mv.addObject("labsetup", genericConfigRepository.getConfigProperties("labsetup"));
    return mv;
  }
}

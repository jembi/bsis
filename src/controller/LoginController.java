package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
  public ModelAndView welcome(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("welcomePage");
    mv.addObject("versionNumber", UtilController.VERSION_NUMBER);
    mv.addObject("labsetup", genericConfigRepository.getConfigProperties("labsetup"));
    return mv;
  }

  @RequestMapping("/adminAccessOnly")
  public ModelAndView adminAccessError(HttpServletRequest request) {

    return new ModelAndView("adminAccessOnly");
  }
}

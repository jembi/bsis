package org.jembi.bsis.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mangofactory.swagger.annotations.ApiIgnore;

@RestController
@RequestMapping("login")
public class LoginController {

  @RequestMapping(method = RequestMethod.GET)
  @ApiIgnore
  public ModelAndView login(HttpServletRequest request,
                            @RequestParam(value = "error", required = false) boolean error) {
    ModelAndView mv = new ModelAndView("login");
    if (error)
      mv.addObject("login_error", true);
    return mv;
  }
}
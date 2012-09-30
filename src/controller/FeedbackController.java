package controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import repository.FeedbackRepository;

@Controller
public class FeedbackController {

  @Autowired
  private FeedbackRepository feedbackRepository;

  public FeedbackController() {
  }

  @RequestMapping(value = "/submitFeedback", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> addDonor(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    String comments = params.get("comments");
    Feedback feedback = new Feedback(comments);
    feedbackRepository.saveFeedback(feedback);

    Map<String, Object> model = new HashMap<String, Object>();
    model.put("feedbackSaved", true);
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", true);
    m.put("errMsg", "Error");

    return m;
  }

}

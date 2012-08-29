package controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.FeedbackRepository;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;


    public FeedbackController() {
    }

    @RequestMapping("/feedbackPage")
    public ModelAndView getFeedbackPage(HttpServletRequest request) {


        ModelAndView modelAndView = new ModelAndView("feedbackPage");
        Map<String, Object> model = new HashMap<String, Object>();
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/submitFeedback")
    public ModelAndView addDonor(@RequestParam Map<String, String> params,HttpServletRequest request) {

        String comments = params.get("comments");
        Feedback feedback = new Feedback(comments);
        feedbackRepository.saveFeedback(feedback);

        ModelAndView modelAndView = new ModelAndView("feedbackPage");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("feedbackSaved", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

}

package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/admin-userLandingPage")
	public ModelAndView users(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("userLandingPage");
		Map<String, Object> model = new HashMap<String, Object>();
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-addUser")
	public ModelAndView addUserPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("addUser");
		Map<String, Object> model = new HashMap<String, Object>();
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-updateUser")
	public ModelAndView updateUserPage(HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-createNewUser")
	public ModelAndView addUser(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		String username = params.get("username");
		User existingUser = userRepository.findUser(username);
		Map<String, Object> model = new HashMap<String, Object>();
		ModelAndView modelAndView = new ModelAndView("updateUser");
		if (existingUser == null) {
			User user = new User(username, params.get("password"),
					params.get("type"), params.get("name"),
					params.get("contactNumber"), params.get("emailId"),
					Boolean.FALSE, params.get("comments"));
			userRepository.saveUser(user);
			model.put("userAdded", true);
			model.put("userDetails", user);
			model.put("hasUserDetails", true);
		} else {
			modelAndView = new ModelAndView("addUser");
			model.put("userExists", true);
			model.put("username", username);
			User user = new User(username, params.get("password"),
					params.get("type"), params.get("name"),
					params.get("contactNumber"), params.get("emailId"),
					Boolean.FALSE, params.get("comments"));
			model.put("hasUserDetails", true);
			model.put("userDetails", user);
		}
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-findUser")
	public ModelAndView findUser(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		String username = params.get("username");
		User user = userRepository.findUser(username);
		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		if (user != null) {
			model.put("userFound", true);
			model.put("userDetails", user);
			model.put("hasUserDetails", true);
		} else {
			modelAndView = new ModelAndView("addUser");
			model.put("userNotFound", true);
			model.put("username", username);
		}
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-userTable")
	public ModelAndView showAllUsers(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		List<User> allUsers = userRepository.getAllUsers();
		ModelAndView modelAndView = new ModelAndView("usersTable");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("allUsers", allUsers);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/findUser")
	public ModelAndView findSelfUser(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		String username = params.get("username");
		User sessionUser = (User) request.getSession().getAttribute("user");
		if (!sessionUser.getUsername().equals(username)) {
			return new ModelAndView("adminAccessOnly");
		}
		User user = userRepository.findUser(username);
		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		if (user != null) {
			model.put("userFound", true);
			model.put("userDetails", user);
			model.put("hasUserDetails", true);
			model.put("selfUser", true);
		} else {
			throw new RuntimeException("current user was deleted from system");
		}
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/updateSelfUser")
	public ModelAndView updateUser(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		String username = params.get("username");
		User sessionUser = (User) request.getSession().getAttribute("user");
		if (!sessionUser.getUsername().equals(username)) {
			return new ModelAndView("adminAccessOnly");
		}
		User user = new User(params.get("username"), params.get("password"),
				params.get("type"), params.get("name"),
				params.get("contactNumber"), params.get("emailId"),
				Boolean.FALSE, params.get("comments"));
		userRepository.updateUser(user);
		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userUpdated", true);
		model.put("userDetails", user);
		model.put("hasUserDetails", true);
		model.put("selfUser", true);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-updateExistingUser")
	public ModelAndView updateAdminUser(
			@RequestParam Map<String, String> params, HttpServletRequest request) {

		User user = new User(params.get("username"), params.get("password"),
				params.get("type"), params.get("name"),
				params.get("contactNumber"), params.get("emailId"),
				Boolean.FALSE, params.get("comments"));
		userRepository.updateUser(user);
		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userUpdated", true);
		model.put("userDetails", user);
		model.put("hasUserDetails", true);
		modelAndView.addObject("model", model);
		return modelAndView;
	}

	@RequestMapping("/admin-deleteUser")
	public ModelAndView deleteUser(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		userRepository.deleteUser(params.get("username"));
		ModelAndView modelAndView = new ModelAndView("updateUser");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("userDeleted", true);
		model.put("usernameDeleted", params.get("username"));
		modelAndView.addObject("model", model);
		return modelAndView;
	}

}

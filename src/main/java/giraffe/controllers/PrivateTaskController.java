package giraffe.controllers;

import giraffe.domain.activity.household.PrivateTask;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Controller
@RequestMapping("/private-task")
@ExposesResourceFor(PrivateTask.class)
final public class PrivateTaskController { }

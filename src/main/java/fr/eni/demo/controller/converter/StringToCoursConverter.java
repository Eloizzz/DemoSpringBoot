package fr.eni.demo.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import fr.eni.demo.bll.CoursService;
import fr.eni.demo.bo.Cours;

@Component // Définir le converter en tant que bean Spring
public class StringToCoursConverter implements Converter<String, Cours> {

	private CoursService service;

	@Autowired
	public void setCoursService(CoursService service) {
		System.out.println("StringToCoursConverter - setCoursService");
		this.service = service;
	}

	@Override
	public Cours convert(String id) {
		System.out.println("Convert - " + id);
		Integer theId = Integer.parseInt(id);
		return service.findById(theId);
	}
}

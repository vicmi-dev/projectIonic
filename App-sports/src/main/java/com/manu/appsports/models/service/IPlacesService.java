package com.manu.appsports.models.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.manu.appsports.models.entity.Places;
@Component
public interface IPlacesService {

	public List<Places> findAll(); 

	public Places save(Places place); 

	public Places findById(Long id); 

	public void delete(Long id); 

}

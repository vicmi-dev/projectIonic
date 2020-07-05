package com.manu.appsports.models.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manu.appsports.models.DAO.IPlaces;
import com.manu.appsports.models.entity.Places;

@Component
public class PlacesServiceImpl implements IPlacesService{
	
	@Autowired
	private IPlaces placesDao;

	@Override
	public List<Places> findAll() {
		return (List<Places>) placesDao.findAll();
	}

	@Override
	@Transactional
	public Places save(Places place) {
		return placesDao.save(place);		
	}

	@Override
	public Places findById(Long id) {
		return placesDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		placesDao.deleteById(id);		
	}

	

}

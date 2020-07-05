package com.manu.appsports.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manu.appsports.models.entity.Places;
import com.manu.appsports.models.service.IPlacesService;




@CrossOrigin(origins= {"http://localhost:8100"})

@RestController

@RequestMapping("/api")
public class PlacesRestController {
	
	@Autowired
	private IPlacesService placesService;

	@GetMapping("/offered_places")
	public List<Places> index() {
		return placesService.findAll();
	}
	
	@GetMapping("/offered_places/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Places place = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			place = placesService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(place == null) {
			response.put("mensaje", "El sitio ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Places>(place, HttpStatus.OK);
	}

	@PostMapping("/offered_places")
	public ResponseEntity<?> create(@Valid @RequestBody Places place, BindingResult result) {
		
		Places placeNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			placeNew = placesService.save(place);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El sitio ha sido creado con éxito!");
		response.put("offered_place", placeNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/offered_places/{id}")
	public ResponseEntity<?> update( @RequestBody Places place, BindingResult result, @PathVariable Long id) {

		Places placeActual = placesService.findById(id);

		Places placeUpdated = null;

		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (placeActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el sitio con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			placeActual.setTitle(place.getTitle());
			placeActual.setImage_url(place.getImage_url());
			placeActual.setDescripcion(place.getDescripcion());
			placeActual.setAvailable_to(place.getAvailable_to());
			placeActual.setAvailable_from(place.getAvailable_from());
			placeActual.setUser_num(place.getUser_num());
			placeActual.setPrice(place.getPrice());
			
			

			placeUpdated = placesService.save(placeActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el sitio en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El sitio ha sido actualizado con éxito!");
		response.put("offered_places", placeUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/offered_places/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    placesService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el sitio de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El sitio fue eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}

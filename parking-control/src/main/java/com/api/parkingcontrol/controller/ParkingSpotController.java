package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){

        if(parkingSpotService.existsByLicencePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CONFLICT: License Plate Car is already in use!");
        }

        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CONFLICT: Parking Spot Number is already in use!");
        }

        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CONFLICT: Parking Spot is already registered for this apartment/block!");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDate.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingModels(@PageableDefault(page=0, size=10, sort="id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingModel(@PathVariable (value="id")UUID id){
        var parkingSpotModel = parkingSpotService.findById(id);
        return parkingSpotModel.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel.get()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSport(@PathVariable (value="id") UUID id){
        var parkingSpotModel = parkingSpotService.findById(id);
        if(parkingSpotModel.isPresent()) {
            parkingSpotService.delete(parkingSpotModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Parking spot deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpotModel(@PathVariable (value="id") UUID id,
                                                         @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        var parkingSpotModel = parkingSpotService.findById(id);
        if(parkingSpotModel.isPresent()){
            ParkingSpotModel psm = new ParkingSpotModel();
            BeanUtils.copyProperties(parkingSpotDto, psm);
            psm.setId(parkingSpotModel.get().getId());
            psm.setRegistrationDate(parkingSpotModel.get().getRegistrationDate());
            parkingSpotService.save(psm);
            return ResponseEntity.status(HttpStatus.OK).body(psm);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
    }



}

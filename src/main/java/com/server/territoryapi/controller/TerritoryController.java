package com.server.territoryapi.controller;

import com.server.territoryapi.model.TerritoryDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TerritoryController {

    @GetMapping("/regions")
    public List<TerritoryDTO> getAvailableRegions() {
        List<TerritoryDTO> territories = new ArrayList<>();
        
        // тестовые территории
        
        territories.add(new TerritoryDTO(
            "plot_1", 100, 64, 100, 120, 70, 120, 
            "world", 2100.0, 441
        ));
        
        territories.add(new TerritoryDTO(
            "plot_2", -50, 64, -50, -30, 70, -30, 
            "world", 900.0, 441
        ));
        
        territories.add(new TerritoryDTO(
            "plot_3", 200, 64, 200, 230, 70, 230, 
            "world", 3300.0, 961
        ));
        
        return territories;
    }
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Territory API is running!";
    }

    @GetMapping("/regions/markers")
    public Map<String, Object> getRegionMarkers() {
        Map<String, Object> markers = new HashMap<>();
        Map<String, Object> sets = new HashMap<>();
        Map<String, Object> regionsSet = new HashMap<>();
        
        List<TerritoryDTO> availableRegions = getAvailableRegions();
        
        availableRegions.forEach(region -> {
            Map<String, Object> marker = new HashMap<>();
            marker.put("label", "Участок #" + region.getId().split("_")[1]);
            marker.put("markup", false);
            marker.put("fillColor", "#00FF0044");
            marker.put("fillOpacity", 0.3);
            marker.put("strokeColor", "#00FF00");
            marker.put("strokeOpacity", 1);
            marker.put("strokeWeight", 2);
            
            List<Integer> xCoords = Arrays.asList(
                (int) region.getMinX(), (int) region.getMaxX(), 
                (int) region.getMaxX(), (int) region.getMinX()
            );
            List<Integer> zCoords = Arrays.asList(
                (int) region.getMinZ(), (int) region.getMinZ(),
                (int) region.getMaxZ(), (int) region.getMaxZ()
            );
            
            marker.put("x", xCoords);
            marker.put("z", zCoords);
            marker.put("y", Arrays.asList(64, 64, 64, 64));
            
            regionsSet.put("region_" + region.getId(), marker);
        });
        
        // Структура совместимая с Dynmap API
        sets.put("regions", regionsSet);
        markers.put("sets", sets);
        
        return markers;
    }
    
    @PostMapping("/regions/buy")
    public Map<String, Object> buyTerritory(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String regionId = request.get("regionId");
        String playerUsername = request.get("playerUsername");
        
        // Проверка существования территории
    Optional<TerritoryDTO> territory = getAvailableRegions().stream()
        .filter(r -> r.getId().equals(regionId))
        .findFirst();
    
    if (!territory.isPresent()) {
        response.put("success", false);
        response.put("error", "Территория не найдена");
        return response;
    }
    
    // Здесь реальная логика покупки (база данных, экономика и т.д.)
    response.put("success", true);
    response.put("message", "Территория " + regionId + " куплена игроком " + playerUsername);
    response.put("region", territory.get());
        
        return response;
    }
}

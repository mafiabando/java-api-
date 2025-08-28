package com.server.territoryapi.model;

public class TerritoryDTO {
    private String id;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;
    private String world;
    private double price;
    private int size;
    private int width;
    private int length;

    // Конструкторы
    public TerritoryDTO() {}
    
    public TerritoryDTO(String id, int minX, int minY, int minZ, int maxX, 
                       int maxY, int maxZ, String world, double price, int size) {
        this.id = id;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.world = world;
        this.price = price;
        this.size = size;
        this.width = maxX - minX + 1;
        this.length = maxZ - minZ + 1;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getMinX() { return minX; }
    public void setMinX(int minX) { this.minX = minX; }
    
    public int getMinY() { return minY; }
    public void setMinY(int minY) { this.minY = minY; }
    
    public int getMinZ() { return minZ; }
    public void setMinZ(int minZ) { this.minZ = minZ; }
    
    public int getMaxX() { return maxX; }
    public void setMaxX(int maxX) { this.maxX = maxX; }
    
    public int getMaxY() { return maxY; }
    public void setMaxY(int maxY) { this.maxY = maxY; }
    
    public int getMaxZ() { return maxZ; }
    public void setMaxZ(int maxZ) { this.maxZ = maxZ; }
    
    public String getWorld() { return world; }
    public void setWorld(String world) { this.world = world; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
}

package InventoryData;

import java.util.LinkedHashMap;
import java.util.Map;

import MenuClass.Player;

public interface InventoryObjects {
    public abstract String getDes();
    public abstract String getName();
    public abstract String getType();
    public abstract LinkedHashMap<String, Map.Entry<String, Integer>> getSpecial();
    public abstract String getObType();
}

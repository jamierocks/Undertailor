package me.scarlet.undertailor.manager;

import me.scarlet.undertailor.Undertailor;
import me.scarlet.undertailor.wrappers.RoomDataWrapper;
import ninja.leaping.configurate.json.JSONConfigurationLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    
    public static final String MANAGER_TAG = "bellboy";
    
    private Map<String, RoomDataWrapper> rooms;
    
    public RoomManager() {
        this.rooms = new HashMap<>();
    }
    
    public void loadRooms(File directory) {
        loadRooms(directory, null);
        Undertailor.instance.log(MANAGER_TAG, rooms.keySet().size() + " room(s) currently loaded");
    }
    
    public void loadRooms(File dir, String heading) {
        String dirPath = dir.getAbsolutePath();
        if(!dir.exists()) {
            Undertailor.instance.warn(MANAGER_TAG, "could not load rooms directory " + dirPath + " (did not exist)");
            return;
        }
        
        if(!dir.isDirectory()) {
            Undertailor.instance.warn(MANAGER_TAG, "could not load rooms directory " + dirPath + " (not a directory)");
            return;
        }
        
        Undertailor.instance.log(MANAGER_TAG, "searching for rooms in " + dirPath);
        if(heading == null) {
            heading = "";
        }
        
        for(File file : dir.listFiles(file -> {
            return file.isDirectory() || file.getName().endsWith(".lua");
        })) {
            if(file.isDirectory()) {
                loadRooms(file, heading + (heading.isEmpty() ? "" : ".") + file.getName() + ".");
                continue;
            }
            
            String roomName = file.getName().substring(0, file.getName().length() - 4);
            File mapDataFile = new File(dir, roomName + ".roommap");
            if(!mapDataFile.exists()) {
                Undertailor.instance.warn(MANAGER_TAG, "ignoring room " + roomName + " (no map file)");
                continue;
            }
            
            if(!mapDataFile.isFile()) {
                Undertailor.instance.warn(MANAGER_TAG, "ignoring room " + roomName + " (bad map file)");
                continue;
            }
            
            JSONConfigurationLoader loader = JSONConfigurationLoader.builder().setFile(mapDataFile).build();
            try {
                rooms.put(roomName, new RoomDataWrapper(file, loader.load()));
            } catch(Exception e) {
                Undertailor.instance.error(MANAGER_TAG, "could not load room " + roomName + ": " + e.getMessage(), e.getStackTrace());
            }
        }
    }
    
    public RoomDataWrapper getRoom(String name) {
        if(rooms.containsKey(name)) {
            return rooms.get(name);
        }
        
        Undertailor.instance.warn(MANAGER_TAG, "system requested non-existing room (" + name + ")");
        return null;
    }
}
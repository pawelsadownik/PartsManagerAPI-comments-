package domain.service;

import domain.Part;

import java.util.ArrayList;
import java.util.List;

public class PartService {

    private static List<Part> db = new ArrayList<Part>();
    private static int currentId = 1;
    public List<Part> getAll(){
        return db;
    }
    public Part get(int id){
        for(Part p : db){
            if(p.getId() == id)
                return p;
        }
        return null;
    }
    public void add(Part p){
        p.setId(++currentId);
        db.add(p);
    }
    public void update(Part part){
        for(Part p : db){
            if(p.getId()==part.getId()){
                p.setName(part.getName());
                p.setPrice(part.getPrice());
                p.setCategory(part.getCategory());
            }
        }
    }
}

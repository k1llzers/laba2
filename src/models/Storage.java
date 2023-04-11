package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Storage implements Serializable {
    private ArrayList<Group> groups = new ArrayList<>();

    public void add(Group group) throws Exception {
        for (Group g:groups)
            if (g.getName().equals(group.getName()))
                throw new Exception("Група з такою назвою вже існує!");
        groups.add(group);
    }

    public void delete(String name){
        for (Group g:groups)
            if (g.getName().equals(name)){
                groups.remove(g);
                return;
            }
    }

    public void edit(String name, Group group){
        for (Group g:groups)
            if (g.getName().equals(name)){
                if (!group.getName().equals(""))
                    g.setName(group.getName());
                if (!group.getDescription().equals(""))
                    g.setDescription(group.getDescription());
                return;
            }
        throw new RuntimeException("Група з такою назвою не існує");
    }

    public ArrayList<Group> getAllGroups(){
        return groups;
    }

    public ArrayList<Goods> getAllGoods(){
        ArrayList<Goods> goods = new ArrayList<>();
        for (Group group:groups)
            goods.addAll(group.getAll());
        return goods;
    }
}

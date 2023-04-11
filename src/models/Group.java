package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String name;
    private String description;
    private ArrayList<Goods> goods = new ArrayList<>();

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void add(Goods g){
        goods.add(g);
    }

    public void delete(String name){
        for(Goods g:goods){
            if(g.getName().equals(name)){
                goods.remove(g);
                return;
            }
        }
        throw new RuntimeException("Товара з такою назвою не існує!");
    }

    public void edit(String name, Goods g){
        for(Goods good:goods){
            if(name.equals(good.getName())){
                if(!g.getName().equals("")){
                    good.setName(g.getName());
                }
                if(!g.getDescription().equals("")){
                    good.setDescription(g.getDescription());
                }
                if(!g.getProducer().equals("")){
                    good.setProducer(g.getProducer());
                }
                if(g.getPrice()!=0){
                    good.setPrice(g.getPrice());
                }
                return;
            }
        }
        throw new RuntimeException("Товара з такою назвою не існує!");
    }

    public ArrayList<Goods> getAll(){
        return goods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " , опис: " + description;
    }
}

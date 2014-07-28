package villagers;

import java.util.ArrayList;
import objects.IObject;


class Merchant {
	private String name;
	private ArrayList<IObject> invo;
	private int level;

	//TODO: Work on the merchant
	public Merchant(String name, int lvl) {
		this.name = name;
		level = lvl;
		invo = new ArrayList<IObject>();
	}

	public String getName() {
		return name;
	}
	
	public IObject[] makeInvo(){
		for(int i = 0; i < 7; i++){
			
		}
		return null;
	}
}

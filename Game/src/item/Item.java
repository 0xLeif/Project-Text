package item;

import objects.IObject;

public class Item implements IObject {

	private String name, effect, objectType = "item";
	private int power;
	private double weight;

	public Item(String name, String effect, int power, double weight) {
		this.name = name;
		this.effect = effect;
		this.power = power;
		this.weight = weight;
	}

	public String toString() {
		String summary = name + ", " + effect + ", " + power + ", " + weight;
		return summary;
	}

	public String getName() {
		return name;
	}

	public int getPower() {
		return power;
	}

	public double getWeight() {
		return weight;
	}

	public String getEffect() {
		return effect;
	}

	@Override
	public String getObjectType() {
		return objectType;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public int getDmg() {
		return 0;
	}

	@Override
	public int getDef() {
		return 0;
	}

	@Override
	public int getLevel() {
		return 0;
	}

}

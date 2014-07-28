package monsters;

import objects.IObject;
import protection.Armor;
import weapons.Weapon;

public class Monster {
	private String name;
	private int level;
	private double health,mana;
	private IObject rightHand, leftHand;
	private Armor feet, legs, torso, head, back;

	public Monster(String name, double health, int level) {
		this.name = name;
		this.health = health;
		mana = 100;
		this.level = level;
		rightHand = new Weapon("Stick", "Melee", 2, 2.0, 1);
		leftHand = null;
		feet = new Armor("Boots", "Feet", 1, 2.0, 1);
		legs = new Armor("Rags", "Legs", 0, 1.0, 1);
		torso = new Armor("Goblin Mail", "Torso", 2, 5.0, 1);
		head = null;
		back = new Armor("Backpack", "Back", 0, 3.0, 1);
	}

	public Monster() {
		name = "Dummy";
		health = 10;
		level = 0;
	}

	public Monster(String name, double health, double mana, int level,
			IObject rightHand, IObject leftHand, Armor feet, Armor legs,
			Armor torso, Armor head, Armor back) {
		this.name = name;
		this.health = health;
		this.mana = mana;
		this.level = level;
		this.rightHand = rightHand;
		this.leftHand = leftHand;
		this.feet = feet;
		this.legs = legs;
		this.torso = torso;
		this.head = head;
		this.back = back;

	}

	public String toString() {
		int def = 0;
		if (legs != null) {
			def += legs.getDef();
		}
		if (head != null) {
			def += head.getDef();
		}
		if (torso != null) {
			def += torso.getDef();
		}
		if (feet != null) {
			def += feet.getDef();
		}
		if (back != null) {
			def += back.getDef();
		}
		int att = 0;
		if (rightHand != null) {
			if (rightHand.getObjectType().equals("weapon")) {
				att += rightHand.getDmg();
			} else {
				def += rightHand.getDef();
			}
		}
		if (leftHand != null) {
			if (leftHand.getObjectType().equals("weapon")) {
				att += leftHand.getDmg();
			} else {
				def += rightHand.getDef();
			}
		}
		String summary = "Name: " + name;
		summary += "\nHealth: " + health;
		summary += "\nMana: " + mana;
		summary += "\nAttack: " + att;
		summary += "\nDefence: " + def;
		summary += "\nLevel: " + level;
		return summary;
	}

	public String equipment() {
		String right = "";
		String left = "";
		String head = "";
		String torso = "";
		String legs = "";
		String feet = "";
		String back = "";
		if (rightHand == null) {
			right = "Nothing";
		} else {
			right = "" + rightHand;
		}
		if (leftHand == null) {
			left = "Nothing";
		} else {
			left = "" + leftHand;
		}
		if (this.head == null) {
			head = "Nothing";
		} else {
			head = "" + this.head;
		}
		if (this.torso == null) {
			torso = "Nothing";
		} else {
			torso = "" + this.torso;
		}
		if (this.legs == null) {
			legs = "Nothing";
		} else {
			legs = "" + this.legs;
		}
		if (this.feet == null) {
			feet = "Nothing";
		} else {
			feet = "" + this.feet;
		}
		if (this.back == null) {
			back = "Nothing";
		} else {
			back = "" + this.back;
		}
		String summary = "Equipment:";
		summary += "\nRight Hand: " + right;
		summary += "\nLeft Hand: " + left;
		summary += "\nHead: " + head;
		summary += "\nTorso: " + torso;
		summary += "\nLegs: " + legs;
		summary += "\nFeet: " + feet;
		summary += "\nBack " + back;
		return summary;
	}

	// Getters
	public String getName() {
		return name;
	}

	public double getHealth() {
		return health;
	}

	public double getMana() {
		return mana;
	}

	public IObject getRightHand() {
		return rightHand;
	}

	public IObject getLeftHand() {
		return leftHand;
	}

	public Armor getFeet() {
		return feet;
	}

	public Armor getLegs() {
		return legs;
	}

	public Armor getTorso() {
		return torso;
	}

	public Armor getHead() {
		return head;
	}

	public int getLevel() {
		return level;
	}

	public int getDmg() {
		int att = 0;
		if (rightHand != null) {
			if (rightHand.getObjectType().equals("weapon")) {
				att += rightHand.getDmg();
			} else {
				// Do nothing
			}
		}
		if (leftHand != null) {
			if (leftHand.getObjectType().equals("weapon")) {
				att += leftHand.getDmg();
			} else {
				// Do nothing
			}
		}
		return att;
	}

	public int getDef() {
		int def = 0;
		if (legs != null) {
			def += legs.getDef();
		}
		if (head != null) {
			def += head.getDef();
		}
		if (torso != null) {
			def += torso.getDef();
		}
		if (feet != null) {
			def += feet.getDef();
		}
		if (back != null) {
			def += back.getDef();
		}
		int att = 0;
		if (rightHand != null) {
			if (rightHand.getObjectType().equals("weapon")) {
				// Do nothing
			} else {
				def += rightHand.getDef();
			}
		}
		if (leftHand != null) {
			if (leftHand.getObjectType().equals("weapon")) {
				// Do nothing
			} else {
				def += rightHand.getDef();
			}
		}
		return def;
	}

	public int getWt() {
		int wt = 0;
		if (legs != null) {
			wt += legs.getWeight();
		}
		if (head != null) {
			wt += head.getWeight();
		}
		if (torso != null) {
			wt += torso.getWeight();
		}
		if (feet != null) {
			wt += feet.getWeight();
		}
		if (back != null) {
			wt += back.getWeight();
		}
		int att = 0;
		if (rightHand != null) {
			if (rightHand.getObjectType().equals("weapon")) {
				wt += ((Weapon) rightHand).getWeight();
			} else {
				wt += ((Armor) rightHand).getWeight();
			}
		}
		if (leftHand != null) {
			if (leftHand.getObjectType().equals("weapon")) {
				wt += ((Weapon) leftHand).getWeight();
			} else {
				wt += ((Armor) leftHand).getWeight();
			}
		}
		return wt;
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public void setMana(double mana) {
		this.mana = mana;
	}

	public void setRightHand(IObject rightHand) {
		this.rightHand = rightHand;
	}

	public void setLeftHand(IObject leftHand) {
		this.leftHand = leftHand;
	}

	public void setFeet(Armor boots) {
		this.feet = boots;
	}

	public void setLegs(Armor legs) {
		this.legs = legs;
	}

	public void setTorso(Armor shirt) {
		this.torso = shirt;
	}

	public void setHead(Armor head) {
		this.head = head;
	}
}

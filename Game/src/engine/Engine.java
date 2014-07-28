package engine;

import gui.Client;
import item.Item;
import maps.Point;
import methods.Invo;
import monsters.Monster;
import objects.IObject;
import protection.Armor;
import weapons.Weapon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import battle.Fight;

public class Engine implements KeyListener {
	private User user;
	private double expLimit;
	private ArrayList<String> map;
	private Point location;
	private char curLoc;
	private Client game;
	private ArrayList<Point> pastLocs;
	private String[][] cMap;
	private ArrayList<IObject> invo, worldObjects, mapObjects;
	private int Heading, startX, pickupItem, story, startY, level;
	private boolean end, level2Lever, pickup, moved;
	private ArrayList<String> regMap;

	/**
	 * Starts the game
	 * 
	 * @throws IOException
	 */
	public Engine() throws IOException {// TODO Make space attack
		level = 1;
		loadMap("./src/engine/maps/chapter1.txt");
		mapStart();
		loadObjects("./src/engine/items/chapter1.txt");
		loadWorldObjects();
		game = new Client();
		game.getFrame().setVisible(true);
		game.getGT().addKeyListener(this);
		story = 0;
		user = new User();
		story();
		Heading = 3;
		invo = new ArrayList<IObject>();
		location = new Point(startX, startY);
		pastLocs = new ArrayList<Point>();
		regMap = map;
		curLoc = map.get((int) location.getY()).toString()
				.charAt((int) (location.getX()));
		expLimit = user.getLevel() * 1000;
	}

	/**
	 * All the commands that a user can type in
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		checker();
		mapChecker();
		repaint();
		game.getGT().requestFocus();
	}

	private void attack() {
		long startTime = System.currentTimeMillis();
		char c = map.get((int) location.getY() - 1).toString()
				.charAt((int) (location.getX()));
		char letter;
		String line = "";
		regMap = map;
		ArrayList<String> attackNorth = new ArrayList<String>();
		for (int i = 0; i < map.size(); i++) {
			if (i != location.getY() - 1) {
				attackNorth.add(map.get(i));
			} else {
				for (int j = map.get(i).toString().length() - 1; j >= 0; j--) {
					if (j != location.getX()) {
						letter = map.get((int) location.getY()).toString()
								.charAt(j);
					} else {
						letter = '|';
					}
					line = letter + line;
				}
				attackNorth.add(line);
			}
		}
		map = attackNorth;
		System.out.println("Time: " + (System.currentTimeMillis() - startTime));
	}

	private void pullLever() {
		if (curLoc == 'l') {
			if (!level2Lever) {
				removeCurLoc();
				level2Lever = true;
			}
		}
		char letter;
		String line = "";
		ArrayList<String> newMap = new ArrayList<String>();
		for (int i = 0; i < map.size(); i++) {
			for (int j = map.get(i).toString().length() - 1; j >= 0; j--) {
				if (map.get(i).toString().charAt(j) != 'L') {
					letter = map.get(i).toString().charAt(j);
				} else {
					letter = 'o';
				}
				line = letter + line;
			}
			newMap.add(line);
			line = "";
		}
		map = newMap;
	}

	private void equipItem(String item) {
		String equip = "";
		Scanner in = new Scanner(System.in);
		for (int j = 0; j < invo.size(); j++) {
			// When the item they entered equals a items name
			// in their invo
			if (item != null) {
				if (item.toLowerCase().equals(
						invo.get(j).getName().toLowerCase())) {
					System.out.println();
					if (invo.get(j).getLevel() > user.getLevel()) {
						System.out.println("You are to low level for that!");
					} else if (invo.get(j).getObjectType().equals("weapon")) {
						if (invo.get(j).getType().equals("Range")) {
							if (user.getLeftHand() != null) {
								invo.add(user.getLeftHand());
							}
							if (user.getRightHand() != null) {
								invo.add(user.getRightHand());
							}
							user.setRightHand((invo.get(j)));
						} else {
							while (true) {
								// Set their right hand to that weapon
								System.out
										.println("Would you like to equip to your right or left hand?");
								String option = in.nextLine().toLowerCase();
								if (option.contains("right")) {
									if (user.getRightHand() != null) {
										invo.add(user.getRightHand());
									}
									user.setRightHand((invo.get(j)));
									break;
								}
								if (option.contains("left")) {
									if (user.getLeftHand() != null) {
										invo.add(user.getLeftHand());
									}
									user.setLeftHand((invo.get(j)));
									break;
								}
							}
						}
						// Change the text
						equip = (invo.get(j)).getName() + " equipped";
						// Remove the item from the invo
						invo.remove(j);
						j = invo.size();
						break;
						// If it is armor
					} else if (invo.get(j).getObjectType().equals("armor")) {
						String type = invo.get(j).getType();
						// Check to see what type of armor it is
						if (type.equals("Back")) {
							invo.add(user.getBack());
							user.setBack((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Legs")) {
							invo.add(user.getLegs());
							user.setLegs((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Torso")) {
							invo.add(user.getTorso());
							user.setTorso((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Head")) {
							invo.add(user.getHead());
							user.setHead((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Feet")) {
							invo.add(user.getFeet());
							user.setFeet((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						}
					}

					// If it is not a Weapon or Armor then
					// it must be an item
				} else {
					equip = "You can not equip that item!";
				}
			}
		}
		System.out.println(equip);
	}

	/**
	 * 1 = south 2 = east 3 = north 4 = west m = monster e = end t = treasure x
	 * = forest or wall
	 */
	private void look() {
		char front;
		if (Heading == 1) {
			front = map.get((int) location.getY() + 1).toString()
					.charAt((int) (location.getX()));
		} else if (Heading == 3) {
			front = map.get((int) location.getY() - 1).toString()
					.charAt((int) (location.getX()));
		} else if (Heading == 2) {
			front = map.get((int) location.getY()).toString()
					.charAt((int) (location.getX() + 1));
		} else {
			front = map.get((int) location.getY()).toString()
					.charAt((int) (location.getX() - 1));
		}
		if (front == 'm') {
			System.out.println("There is a monster ahead!");
		} else if (front == 'x') {
			System.out.println("A dense forest lies ahead...");
		} else if (front == 'o') {
			System.out.println("You see a trail.");
		} else if (front == 'l') {
			System.out.println("You see a lever ahead!");
		} else {
			System.out.println();
		}
	}

	private void mapChecker() {
		// ALL MONSTER LOCATION//
		curLoc = map.get((int) location.getY()).toString()
				.charAt((int) (location.getX()));
		if (Character.isDigit(curLoc)) {
			location = checkLocs(new Point(location.getX(), location.getY()));
		} else if (curLoc == 'n') {
			story();
		} else if (curLoc == 'e') {// e == End Area
			level++;
			newLevel(level);
			System.out.println("You enter a new area");
		} else if (curLoc == 'l') {// l == Lever
			System.out.println("You are by a lever.");
		} else if (curLoc == 'a') {
			armorDraw();
			pickup = true;
		} else if (curLoc == 'w') {
			weaponDraw();
			pickup = true;
		} else if (curLoc == 'i') {
			itemDraw();
			pickup = true;
		}
		if (curLoc == 'g') {
			Monster goblin = new Monster("Goblin", 25, 1);
			Fight goblinFight = new Fight(user, goblin, this);
			System.out.println(goblinFight.battle());
			if (goblinFight.isPlayerDead()) {
				goSouth();
				goblinFight.respawn();
			}
			if (goblinFight.playerWin()) {
				removeCurLoc();
				goblinFight.restart();
				checker();
			}
			if (goblinFight.playerFlee()) {
				goSouth();
				goblinFight.respawn();
			}
		} else if (curLoc == 't') {
			Monster troll = new Monster("Troll", 100, 2);
			Fight trollFight = new Fight(user, troll, this);
			System.out.println(trollFight.battle());
			if (trollFight.isPlayerDead()) {
				goSouth();
				trollFight.respawn();
			}
			if (trollFight.playerWin()) {
				removeCurLoc();
				trollFight.restart();
				checker();
			}
			if (trollFight.playerFlee()) {
				goSouth();
				trollFight.respawn();
			}
		} else if (curLoc == 'd') {
			if (user.getRightHand() != null || user.getLeftHand() != null) {
				Monster first = new Monster();
				Fight firstFight = new Fight(user, first, this);
				System.out.println(firstFight.battle());
				if (firstFight.isPlayerDead()) {
					goSouth();
					firstFight.respawn();
				}
				if (firstFight.playerWin()) {
					removeCurLoc();
					firstFight.restart();
					checker();
					story();
				}
			} else {
				System.out
						.println("Dad: You need to equip your sword before you attack the dummy.");
				goSouth();
			}
		}
	}

	private void story() {
		if (story == 0) {
		} else if (story == 1) {
		} else if (story == 2) {
			newLevel(1);
		} else if (story == 3) {
			newLevel(2);
			user.setRightHand(mapObjects.get(4));
			user.setLegs((Armor) mapObjects.get(5));
			user.setTorso((Armor) mapObjects.get(6));
			user.setFeet((Armor) mapObjects.get(7));
		} else if (story == 4) {
			newLevel(3);
		}
		story++;
	}

	private void itemDraw() {
		int counter = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("item")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					counter++;
				}
			}
		}
		Integer[] numbers = new Integer[counter];
		int count = 0;
		int item = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("item")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					numbers[count] = item;
					count++;
				}
			}
			item++;
		}
		int itemNum = 0;
		boolean breakNow = false;
		while (!breakNow) {
			itemNum = (int) ((Math.random() * mapObjects.size()) + 1);
			for (Integer i : numbers) {
				if (i == itemNum) {
					breakNow = true;
					break;
				}
			}
		}
		pickupItem = itemNum;
		System.out.println("You found " + mapObjects.get(itemNum));
		removeCurLoc();
	}

	private void weaponDraw() {
		int counter = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("weapon")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					counter++;
				}
			}
		}
		Integer[] numbers = new Integer[counter];
		int count = 0;
		int weapon = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("weapon")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					numbers[count] = weapon;
					count++;
				}
			}
			weapon++;
		}
		int weaponNum = 0;
		boolean breakNow = false;
		while (!breakNow) {
			weaponNum = (int) ((Math.random() * mapObjects.size()) + 1);
			for (Integer i : numbers) {
				if (i == weaponNum) {
					if (!mapObjects.get(weaponNum).getName().equals("Null")) {
						breakNow = true;
						break;
					}
				}
			}
		}
		pickupItem = weaponNum;
		System.out.println("You found " + mapObjects.get(weaponNum));
		removeCurLoc();
	}

	private void armorDraw() {
		int counter = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("armor")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					counter++;
				}
			}
		}
		Integer[] numbers = new Integer[counter];
		int count = 0;
		int armor = 0;
		for (IObject a : mapObjects) {
			if (a.getObjectType().equals("armor")) {
				if (a.getLevel() <= (user.getLevel() + 2)) {
					numbers[count] = armor;
					count++;
				}
			}
			armor++;
		}
		int armNum = 0;
		boolean breakNow = false;
		while (!breakNow) {
			armNum = (int) ((Math.random() * mapObjects.size()) + 1);
			for (Integer i : numbers) {
				if (i == armNum) {
					breakNow = true;
					break;
				}
			}
		}
		pickupItem = armNum;
		System.out.println("You found " + mapObjects.get(armNum));
		removeCurLoc();
	}

	private Point checkLocs(Point p) {
		for (int y = 0; y < map.size(); y++) {
			for (int x = 0; x < map.get(y).length(); x++) {
				if (y != p.getY() && x != p.getX()) {
					if (map.get(y).charAt(x) == map.get(p.getY()).charAt(
							p.getX())) {
						System.out.println(true);
						return new Point(x, y);
					}
				} else {
					System.out.println(false);
				}
			}
		}
		return p;
	}

	private void removeCurLoc() {
		char letter;
		String line = "";
		ArrayList<String> newMap = new ArrayList<String>();
		for (int i = 0; i < map.size(); i++) {
			if (i != location.getY()) {
				newMap.add(map.get(i));
			} else {
				for (int j = map.get(i).toString().length() - 1; j >= 0; j--) {
					if (j != location.getX()) {
						letter = map.get((int) location.getY()).toString()
								.charAt(j);
					} else {
						letter = 'o';
					}
					line = letter + line;
				}
				newMap.add(line);
			}
		}
		map = newMap;
	}

	/**
	 * Player types in equip then the console asks the user what they want to
	 * equip
	 */
	private void equip() {
		String equip = "";
		Scanner in = new Scanner(System.in);
		// Prints all the items in your equipment
		for (int i = 0; i < invo.size(); i++) {
			System.out.println(invo.get(i).getName());
		}
		System.out.print("Equip what: ");
		String item = in.nextLine();
		// Look for the item they entered and see if
		// it is an item, weapon, or armor
		for (int j = 0; j < invo.size(); j++) {
			// When the item they entered equals a items name
			// in their inventory
			if (item != null) {
				if (item.toLowerCase().equals(
						invo.get(j).getName().toLowerCase())) {
					System.out.println();
					if (invo.get(j).getLevel() > user.getLevel()) {
						System.out.println("You are to low level for that!");
					} else if (invo.get(j).getObjectType().equals("weapon")) {
						if (invo.get(j).getType().equals("Range")) {
							if (user.getLeftHand() != null) {
								invo.add(user.getLeftHand());
							}
							if (user.getRightHand() != null) {
								invo.add(user.getRightHand());
							}
							user.setRightHand(invo.get(j));
						} else {
							// Set their right hand to that weapon
							while (true) {
								System.out
										.println("Would you like to equip to your right or left hand?");
								String option = in.nextLine().toLowerCase();
								if (option.contains("right")) {
									if (user.getRightHand() != null) {
										invo.add(user.getRightHand());
									}
									user.setRightHand(invo.get(j));
									break;
								}
								if (option.contains("left")) {
									if (user.getLeftHand() != null) {
										invo.add(user.getLeftHand());
									}
									user.setLeftHand(invo.get(j));
									break;
								}
							}
						}
						// Change the text
						equip = invo.get(j).getName() + " equipped";
						// Remove the item from the invo
						invo.remove(j);
						j = invo.size();
						break;
						// If it is armor
					} else if (invo.get(j).getObjectType().equals("armor")) {
						String type = invo.get(j).getType();
						// Check to see what type of armor it is
						if (type.equals("Back")) {
							invo.add(user.getBack());
							user.setBack((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Legs")) {
							invo.add(user.getLegs());
							user.setLegs((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Torso")) {
							invo.add(user.getTorso());
							user.setTorso((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Head")) {
							invo.add(user.getHead());
							user.setHead((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						} else if (type.equals("Feet")) {
							invo.add(user.getFeet());
							user.setFeet((Armor) check(invo.get(j).getName()));
							equip = invo.get(j).getName() + " equipped";
							invo.remove(j);
							j = invo.size();
							break;
						}
					}
					// If it is not a Weapon or Armor then
					// it must be an item
				} else {
					equip = "You can not equip that item!";
				}
			}
		}
		System.out.println(equip);
	}

	/**
	 * moves forward one adds or subtracts x or y depending on which way they
	 * are facing
	 */
	public void goNorth() {
		// north == 3
		// east == 2
		// south == 1
		// west == 4
		Heading = 3;
		Point newLocation = new Point(location.getX(), location.getY() - 1);
		char nextLoc = read((int) newLocation.getX(), (int) newLocation.getY());
		if (nextLoc != 'x' && nextLoc != 'L') {
			location = newLocation;
		}
	}

	public void goWest() {
		Heading = 4;
		Point newLocation = new Point(location.getX() - 1, location.getY());
		char nextLoc = read((int) newLocation.getX(), (int) newLocation.getY());
		if (nextLoc != 'x' && nextLoc != 'L') {
			location = newLocation;
		}
	}

	public void goEast() {
		Heading = 2;
		Point newLocation = new Point(location.getX() + 1, location.getY());
		char nextLoc = read((int) newLocation.getX(), (int) newLocation.getY());
		if (nextLoc != 'x' && nextLoc != 'L') {
			location = newLocation;
		}
	}

	/**
	 * Moves back one adds or subtracts x or y depending on which way they are
	 * facing
	 */
	public void goSouth() {
		Heading = 1;
		Point newLocation = new Point(location.getX(), location.getY() + 1);
		char nextLoc = read((int) newLocation.getX(), (int) newLocation.getY());
		if (nextLoc != 'x' && nextLoc != 'L') {
			location = newLocation;
		}
	}

	/**
	 * Player types in pickup then the console will ask what to pick up
	 */
	private void pickUp() {
		if (pickup) {
			System.out.println("You picked up "
					+ mapObjects.get(pickupItem).getName());
			invo.add(mapObjects.get(pickupItem));
		} else {
			System.out.println("There is nothing to pick up...");
		}
		// else {
		// int i = 0;
		// boolean itemIsValid = false;
		// Scanner in = new Scanner(System.in);
		// System.out.print("Pickup what: ");
		// String item = in.nextLine();
		// for (int j = 0; j < mapObjects.length; j++) {
		// // Makes sure the item is by them and they don't just put in a
		// // random name
		// if (mapObjects[j].getName().toLowerCase()
		// .equals(item.toLowerCase())) {
		// i = j;
		// itemIsValid = true;
		// }
		// }
		// if (itemIsValid) {
		// invo.add(mapObjects[i]);
		// System.out.println("You picked up " + mapObjects[i]);
		// } else {
		// System.out.println("You can not pick that up.");
		// }
		// }
	}

	/**
	 * Saves the item to the invo (autosave)
	 * 
	 * @param acqs
	 *            Array of invo
	 */
	public void saveItem(String path, Invo[] acqs) {
		BufferedWriter file;

		try {
			file = new BufferedWriter(new FileWriter(path));
			for (int i = 0; i < acqs.length; i++) {
				file.write(acqs[i].getItem() + "\n");
			}
			file.write("\n");
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lists of commands that the user name use
	 */
	private void commands() {
		String commands = "Commands:\n-help";
		commands += "\n-forward";
		commands += "\n-back";
		commands += "\n-left";
		commands += "\n-right";
		commands += "\n-look around";
		commands += "\n-location";
		commands += "\n-map";
		commands += "\n-status";
		commands += "\n-pickup";
		commands += "\n-equipment";
		commands += "\n-invo";
		commands += "\n-exit";
		System.out.println(commands);
	}

	/**
	 * Help and promotion message
	 */
	private void help() {
		String help = "Help:\n";
		help += "This game is a text adventure created by";
		help += "\nZach Eriksen, if you need to know your";
		help += "\nsurrounding's description again please type";
		help += "\n'look around' or if you need to know what you";
		help += "\ncan type for commands please type 'commands'";
		help += "\nif you have any more questions, ideas, or just";
		help += "\nwant to say somthing please email me at: ";
		help += "\ntoorealc@yahoo.com";
		System.out.println(help);
	}

	/**
	 * Checks the name to see if it is a game item
	 * 
	 * @param name
	 *            name of the the item they, most likely, want to pickup
	 * @return the real item's info
	 */
	private IObject check(String name) {
		for (IObject o : worldObjects) {
			if (o.getName().toLowerCase().equals(name.toLowerCase())) {
				return o;
			}
		}
		return null;
	}

	/**
	 * @return prints out all of the Items and Weapons in the users Invo
	 */
	private String invo() {
		String invoList = "";
		invoList += "Weapons:";
		invoList += "\n";
		for (IObject a : invo) {
			System.out.println(a);
			if (a.getObjectType().equals("weapon")) {
				invoList += "\t" + a.getName();
				invoList += "\n";
			}
		}
		invoList += "Armor:";
		invoList += "\n";
		for (IObject a : invo) {
			if (a.getObjectType().equals("armor")) {
				invoList += "\t" + a.getName();
				invoList += "\n";
			}
		}
		invoList += "Items:";
		invoList += "\n";
		for (IObject a : invo) {
			if (a.getObjectType().equals("item")) {
				invoList += "\t" + a.getName();
				invoList += "\n";
			}
		}
		return invoList;
	}

	/**
	 * Returns items for the fight method
	 */
	public ArrayList<Item> fightInvo() {
		ArrayList<Item> itemInvo = new ArrayList<Item>();
		for (IObject a : invo) {
			if (a.getObjectType().equals("item")) {
				itemInvo.add((Item) a);
			}
		}
		return itemInvo;
	}

	private void mapStart() {
		for (int i = 0; i < map.size(); i++) {
			for (int j = 0; j < map.get(i).length(); j++) {
				if (map.get(i).toString().charAt(j) == 's') {
					startX = j;
					startY = i;
				}
			}
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < map.size(); i++) {
			if (map.get(i).length() > max) {
				max = map.get(i).length();
			}
		}
		cMap = new String[map.size()][max];
		for (int y = 0; y < cMap.length; y++) {
			for (int x = 0; x < cMap[0].length; x++) {
				cMap[y][x] = "  ";
			}
		}
	}

	private void newLevel(int level) {
		if (level == 1) {
			loadMap("./src/engine/maps/chapter1dream.txt");
			mapStart();
			location.setX(startX);
			location.setY(startY);
		} else {
			loadMap("./src/engine/maps/chapter" + level + ".txt");
			if (level == 2) {
				mapStart();
				location.setX(startX);
				location.setY(startY);
			} else if (level == 3) {
				mapStart();
				location.setX(startX);
				location.setY(startY);
			}
			Heading = 3;
			loadObjects("./src/engine/items/chapter" + level + ".txt");
		}
	}

	private char read(int x, int y) {
		return map.get(y).toString().charAt(x);
	}

	private void checker() {
		if (!end) {
			for (Weapon a : user.getInvo()) {
				if (a.getName().equals("Null")) {
					user.getInvo().remove(a);
				}
			}
			expLimit = user.getLevel() * 1000;
			if (user.getExp() > expLimit) {
				user.addLevel();
				System.out.println("You Leved up... you are now "
						+ user.getLevel());
				checker();
			}
			for (int i = 0; i < invo.size(); i++) {
				if (invo.get(i) == null) {
					invo.remove(i);
				}
			}
			if (Heading > 4) {
				Heading = 1;
			} else if (Heading < 1) {
				Heading = 4;
			}
		}
	}

	private void showMap() throws IOException {
		long startTime = System.currentTimeMillis();
		game.clear();
		game.hideText();
		if (!pastLocs.contains(new Point(location.getX(), location.getY())))
			pastLocs.add(new Point(location.getX(), location.getY()));
		for (Point p : pastLocs) {
			for (int y = p.getY() - 1; y < p.getY() + 2; y++) {
				for (int x = p.getX() - 1; x < p.getX() + 2; x++) {
					try {
						if (y == location.getY() && x == location.getX()) {
							cMap[y][x] = "c";
						} else if (map.get(y).toString().charAt(x) == 'x'
								|| map.get(y).toString().charAt(x) == 'L') {
							cMap[y][x] = "x";
						} else if (Character.isDigit(map.get(y).toString()
								.charAt(x))) {
							cMap[y][x] = "O";
						} else if (map.get(y).toString().charAt(x) == 'o'
								|| map.get(y).toString().charAt(x) == 's') {
							cMap[y][x] = "o";
						} else if (map.get(y).toString().charAt(x) == '|') {
							cMap[y][x] = "|";
						} else {
							cMap[y][x] = "  ";
						}
					} catch (Exception e) {
					}
				}
			}
		}
		for (int y = 0; y < cMap.length; y++) {
			for (int x = 0; x < cMap[0].length; x++) {
				game.addText(cMap[y][x]);
			}
			game.addText("\n");
		}
		game.showText();
		System.err.println("Time: " + (System.currentTimeMillis() - startTime));
	}

	private void loadObjects(String path) {
		try {
			mapObjects = new ArrayList<IObject>();
			mapObjects.add(new Weapon("Null", "Null", 0, 0, 0));
			// Open file
			BufferedReader in = new BufferedReader(new FileReader(path));
			// Read the file
			String line;
			Boolean weapon = false;
			Boolean armor = false;
			Boolean item = false;
			while ((line = in.readLine()) != null) {
				if (item) {
					String text = "";
					String name = null;// 0
					String effect = null;// 1
					int pwr = 0;// 2
					double weight;// 3
					int counter = 0;
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == ',') {
							if (counter == 0) {
								name = text;
								text = "";
								counter++;
							} else if (counter == 1) {
								effect = text;
								text = "";
								counter++;
							} else if (counter == 2) {
								pwr = Integer.parseInt(text);
								text = "";
								counter++;
							} else if (counter == 3) {
								weight = Double.parseDouble(text);
								text = "";
								counter++;
								Item w = new Item(name, effect, pwr, weight);
								mapObjects.add(w);
							}
						} else {
							text += line.charAt(i);
						}

					}
				} else if (armor) {
					String text = "";
					String name = null;// 0
					String type = null;// 1
					int def = 0;// 2
					double weight = 0;// 3
					int level;// 4
					int counter = 0;
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == ',') {
							if (counter == 0) {
								name = text;
								text = "";
								counter++;
							} else if (counter == 1) {
								type = text;
								text = "";
								counter++;
							} else if (counter == 2) {
								def = Integer.parseInt(text);
								text = "";
								counter++;
							} else if (counter == 3) {
								weight = Double.parseDouble(text);
								text = "";
								counter++;
							} else if (counter == 4) {
								level = Integer.parseInt(text);
								text = "";
								counter = 0;
								Armor w = new Armor(name, type, def, weight,
										level);
								mapObjects.add(w);
							}
						} else {
							text += line.charAt(i);
						}

					}
				} else if (weapon) {
					String text = "";
					String name = null;// 0
					String type = null;// 1
					int dmg = 0;// 2
					double weight = 0;// 3
					int level;// 4
					int counter = 0;
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == ',') {
							if (counter == 0) {
								name = text;
								text = "";
								counter++;
							} else if (counter == 1) {
								type = text;
								text = "";
								counter++;
							} else if (counter == 2) {
								dmg = Integer.parseInt(text);
								text = "";
								counter++;
							} else if (counter == 3) {
								weight = Double.parseDouble(text);
								text = "";
								counter++;
							} else if (counter == 4) {
								level = Integer.parseInt(text);
								text = "";
								counter = 0;
								Weapon w = new Weapon(name, type, dmg, weight,
										level);
								mapObjects.add(w);
							}
						} else {
							text += line.charAt(i);
						}
					}
				}
				if (line.equals("Weapons"))
					weapon = true;
				else if (line.equals("Armor"))
					armor = true;
				else if (line.equals("Items"))
					item = true;
			}
			// Loop through remaining lines;
			// storing each line as an answer
			// in the ArrayList of answers
			// Close the file
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadMap(String path) {
		try {
			map = new ArrayList<String>();
			// Open file
			BufferedReader in = new BufferedReader(new FileReader(path));
			// Read the file
			String line;
			while ((line = in.readLine()) != null) {
				// Read the first line as the exam name
				map.add(line);
			}
			// Close the file
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void repaint() {
		try {
			showMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadWorldObjects() {
		for (int p = 1; p < new File("./src/engine/items/").listFiles().length + 1; p++) {
			String path = "./src/engine/items/chapter" + p + ".txt";
			try {
				worldObjects = new ArrayList<IObject>();
				// Open file
				BufferedReader in = new BufferedReader(new FileReader(path));
				// Read the file
				String line;
				Boolean weapon = false;
				Boolean armor = false;
				Boolean item = false;
				while ((line = in.readLine()) != null) {
					if (item) {
						String text = "";
						String name = null;// 0
						String effect = null;// 1
						int pwr = 0;// 2
						double weight;// 3
						int counter = 0;
						// if(String name, String effect, int power, double
						// weight) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ',') {
								if (counter == 0) {
									name = text;
									text = "";
									counter++;
								} else if (counter == 1) {
									effect = text;
									text = "";
									counter++;
								} else if (counter == 2) {
									pwr = Integer.parseInt(text);
									text = "";
									counter++;
								} else if (counter == 3) {
									weight = Double.parseDouble(text);
									text = "";
									counter++;
									Item w = new Item(name, effect, pwr, weight);
									worldObjects.add(w);
								}
							} else {
								text += line.charAt(i);
							}

						}
					} else if (armor) {
						String text = "";
						String name = null;// 0
						String type = null;// 1
						int def = 0;// 2
						double weight = 0;// 3
						int level;// 4
						int counter = 0;
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ',') {
								if (counter == 0) {
									name = text;
									text = "";
									counter++;
								} else if (counter == 1) {
									type = text;
									text = "";
									counter++;
								} else if (counter == 2) {
									def = Integer.parseInt(text);
									text = "";
									counter++;
								} else if (counter == 3) {
									weight = Double.parseDouble(text);
									text = "";
									counter++;
								} else if (counter == 4) {
									level = Integer.parseInt(text);
									text = "";
									counter = 0;
									Armor w = new Armor(name, type, def,
											weight, level);
									worldObjects.add(w);
								}
							} else {
								text += line.charAt(i);
							}

						}
					} else if (weapon) {
						String text = "";
						String name = null;// 0
						String type = null;// 1
						int dmg = 0;// 2
						double weight = 0;// 3
						int level;// 4
						int counter = 0;
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ',') {
								if (counter == 0) {
									name = text;
									text = "";
									counter++;
								} else if (counter == 1) {
									type = text;
									text = "";
									counter++;
								} else if (counter == 2) {
									dmg = Integer.parseInt(text);
									text = "";
									counter++;
								} else if (counter == 3) {
									weight = Double.parseDouble(text);
									text = "";
									counter++;
								} else if (counter == 4) {
									level = Integer.parseInt(text);
									text = "";
									counter = 0;
									Weapon w = new Weapon(name, type, dmg,
											weight, level);
									worldObjects.add(w);
								}
							} else {
								text += line.charAt(i);
							}
						}
					}
					if (line.equals("Weapons"))
						weapon = true;
					else if (line.equals("Armor"))
						armor = true;
					else if (line.equals("Items"))
						item = true;
				}
				// Loop through remaining lines;
				// storing each line as an answer
				// in the ArrayList of answers
				// Close the file
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public IObject getWorldObject(int i) {
		return worldObjects.get(i);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		map = regMap;
		int c = e.getKeyCode();
		if (c == KeyEvent.VK_W)
			goNorth();
		else if (c == KeyEvent.VK_S)
			goSouth();
		else if (c == KeyEvent.VK_D)
			goEast();
		else if (c == KeyEvent.VK_A)
			goWest();
		else if (c == KeyEvent.VK_SPACE)
			attack();
		try {
			run();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
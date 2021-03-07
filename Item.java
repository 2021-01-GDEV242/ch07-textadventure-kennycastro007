/**
 * Represents a single item that the player can pick up
 * 
 * @author Kenny Castro-Monroy
 * @version 03.07.2021
 */
public class Item {

  private String itemName;
  private int weight;

  /**
   * Constructor for a named Item
   * 
   * @param name    the name of the constructed item
   * @param _weight the weight of constructued item
   */
  public Item(String name, int _weight) {
    itemName = name;
    weight = _weight;
  }

  /**
   * @return the name of the item as a string
   */
  public String toString() {
    return itemName + " weighs(oz): " + weight;
  }
}

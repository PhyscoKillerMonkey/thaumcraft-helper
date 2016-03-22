package tree;

import java.util.ArrayList;

public class Node {
  
  private final String name;
  private ArrayList<Node> neighbours;
  
  public Node(String name, ArrayList<Node> neighbours) {
    this.name = name;
    this.neighbours = neighbours;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Node> getNeighbours() {
    return neighbours;
  }
  
  public void addNeighbour(Node neighbour) {
    neighbours.add(neighbour);
  }
}

/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Abstract class representing a Node in a Huffman tree.
 */
public abstract class HuffBaseNode {

  private final int weight;

  /**
   * Constructor.
   *
   * @param weight - the weight of the node
   */
  public HuffBaseNode(int weight) {
    this.weight = weight;
  }

  /**
   * Return the weight of this node (and its children if this
   * instance is a HuffInternalNode).
   */
  public int weight() {
    return weight;
  }

  /**
   * Return true if this node is a leaf.
   */
  public abstract boolean isLeaf();
}

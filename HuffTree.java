/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Encapsulating class representing a Huffman tree. Note that
 * a HuffTree could be just one node without any children.
 */
public class HuffTree implements Comparable<HuffTree> {

  private final HuffBaseNode root;

  /**
   * Constructor for tree when a Leaf node is root.
   *
   * @param element - the element
   * @param weight - the frequency of the element
   */
  public HuffTree(byte element, int weight) {
    root = new HuffLeafNode(weight, element);
  }

  /**
   * Constructor for tree when an Internal node is root.
   *
   * @param left - the left child
   * @param right - the right child
   * @param weight - the sum of the weights' of the left and right children
   */
  public HuffTree(HuffTree left, HuffTree right, int weight) {
    root = new HuffInternalNode(weight, left.root(), right.root());
  }

  /**
   * Return the root of this tree.
   */
  public HuffBaseNode root() {
    return root;
  }

  /**
   * Return the total weight of this tree.
   */
  public int weight() {
    return root.weight();
  }

  @Override
  public int compareTo(HuffTree o) {
    return Integer.compare(root.weight(), o.weight());
  }
}

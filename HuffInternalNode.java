/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Encapsulating class representing an internal node of a HuffTree.
 */
public class HuffInternalNode extends HuffBaseNode {

  private final HuffBaseNode left;
  private final HuffBaseNode right;

  /**
   * Constructor.
   *
   * @param weight - the sum of the two child nodes' weights
   * @param left - the left child
   * @param right - the right child
   */
  public HuffInternalNode(int weight, HuffBaseNode left, HuffBaseNode right) {
    super(weight);

    this.left = left;
    this.right = right;
  }

  /**
   * Return the left child.
   */
  public HuffBaseNode left() {
    return left;
  }

  /**
   * Return the right child.
   */
  public HuffBaseNode right() {
    return right;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }
}

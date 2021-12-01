/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Encapsulating class representing a leaf node of a HuffTree.
 */
public class HuffLeafNode extends HuffBaseNode {

  private final byte element;

  /**
   * Constructor.
   *
   * @param weight - the frequency of the element
   * @param element - the element
   */
  public HuffLeafNode(int weight, byte element) {
    super(weight);

    this.element = element;
  }

  /**
   * Return the element.
   */
  public byte element() {
    return element;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }
}

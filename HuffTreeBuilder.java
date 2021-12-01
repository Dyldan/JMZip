import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Encapsulating class to handle construction of a HuffTree, encoding
 * of a file, and decoding of a compressed file.
 */
public class HuffTreeBuilder {

  private final ArrayList<HuffTree> treeList;
  private final ArrayList<Byte> decodedBytesList; // reduce recursive overhead
  private Iterator<Integer> iterator; // reduce recursive overhead
  private HuffTree finalTree;
  private final HashMap<Byte, Integer> frequencies;
  private final HashMap<Byte, String> encodings; // reduce recursive overhead

  /**
   * Constructor.
   *
   * @param frequencies - the frequency hashmap for each unique byte
   */
  public HuffTreeBuilder(HashMap<Byte, Integer> frequencies) {
    this.frequencies = frequencies;
    treeList = new ArrayList<>();
    decodedBytesList = new ArrayList<>();
    iterator = null;
    finalTree = null;
    encodings = new HashMap<>();
  }

  /**
   * Build the tree using Huffman coding.
   *
   */
  public void buildTree() {
    buildList();

    while (treeList.size() > 1) {
      sort();

      HuffTree left = treeList.get(0);
      HuffTree right = treeList.get(1);
      HuffTree newTree = new HuffTree(left, right, left.weight() + right.weight());

      treeList.remove(left);
      treeList.remove(right);
      treeList.add(newTree);
    }

    finalTree = treeList.get(0);

  }

  /**
   * Entry method for encoding the BitSequence.
   *
   * @param bytes - the bytes to encode
   * @return the created BitSequence
   */
  public BitSequence encodeBitSequence(byte[] bytes) {
    BitSequence encoding = new BitSequence();

    // special case for one byte because that's the only way it works for some reason
    if (bytes.length == 1) {
      encodings.put(bytes[0], "0");

      encoding.appendBits(encodings.get(bytes[0]));
    } else {
      encodeBitSequence(finalTree.root(), ""); // recurse through the tree

      // append each byte's encoding to the BitSequence
      for (byte b : bytes) {
        encoding.appendBits(encodings.get(b));
      }
    }

    return encoding;
  }

  /**
   * Recursive helper method for encoding the BitSequence. Bytes are
   * linked to their encoding with a hashmap.
   *
   * @param tree - the tree to recurse through
   * @param bits - the string of bits
   */
  private void encodeBitSequence(HuffBaseNode tree, String bits) {
    if (tree.isLeaf()) {
      HuffLeafNode leaf = (HuffLeafNode) tree;
      encodings.put(leaf.element(), bits);
      return;
    }

    HuffInternalNode internal = (HuffInternalNode) tree;
    encodeBitSequence(internal.left(), bits + "0");
    encodeBitSequence(internal.right(), bits + "1");
  }

  /**
   * Entry method for decoding a BitSequence.
   *
   * @param encoding - the BitSequence to decode
   * @return an array of bytes equivalent to that of the
   *      original, un-compressed file
   */
  public byte[] decodeBitSequence(BitSequence encoding) {
    iterator = encoding.iterator();

    // special case for one byte because that's the only way it works for some reason
    if (encoding.length() == 1) {
      HuffLeafNode leaf = (HuffLeafNode) finalTree.root();
      decodedBytesList.add(leaf.element());
    } else {
      while (iterator.hasNext()) {
        decodeBitSequence(finalTree.root()); // start from root for each byte
      }
    }

    // copy the ArrayList of bytes to the array
    byte[] bytes = new byte[decodedBytesList.size()];
    for (int i = 0; i < decodedBytesList.size(); i++) {
      bytes[i] = decodedBytesList.get(i);
    }

    return bytes;
  }

  /**
   * Recursive helper method for decoding a BitSequence. Bytes are
   * retrieved by recursing through the HuffTree according to the
   * BitSequence encoding.
   *
   * @param tree - the tree to recurse through
   */
  private void decodeBitSequence(HuffBaseNode tree) {
    if (tree.isLeaf()) {
      HuffLeafNode leaf = (HuffLeafNode) tree;
      decodedBytesList.add(leaf.element());
      return;
    }

    if (iterator.hasNext()) {
      HuffInternalNode internal = (HuffInternalNode) tree;
      if (iterator.next() == 0) {
        decodeBitSequence(internal.left());
      } else {
        decodeBitSequence(internal.right());
      }
    }

  }

  /**
   * Helper method to build an Arraylist of HuffTrees from the
   * frequency HashMap.
   */
  private void buildList() {
    for (Map.Entry<Byte, Integer> entry : frequencies.entrySet()) {
      Byte key = entry.getKey();
      Integer value = entry.getValue();

      HuffTree newTree = new HuffTree(key, value);
      treeList.add(newTree);
    }
  }

  /**
   * Helper method to sort the ArrayList of HuffTrees.
   * Uses Insertion Sort.
   */
  private void sort() {
    for (int i = 1; i < treeList.size(); i++) {
      HuffTree cur = treeList.get(i);
      int j = i - 1;
      while (j >= 0 && cur.compareTo(treeList.get(j)) < 0) {
        treeList.set(j + 1, treeList.get(j));
        j--;
      }

      treeList.set(j + 1, cur);
    }
  }
}

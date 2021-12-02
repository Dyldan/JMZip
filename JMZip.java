import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Program to losslessly compress files using Huffman encoding.
 *
 * @author Dylan Moreno
 * @version 12/1/2021
 */
public class JMZip {

  /**
   * main.
   */
  public static void main(String[] args) {

    // invalid argument checking
    if (args.length < 2) {
      System.out.println("Please provide arguments in the format of:");
      System.out.println("java JMZip FILE.x FILE.jmz");
      return;
    }

    String filename = args[0];
    String zippedFilename = args[1];


    // try to read bytes from file and create the frequency HashMap
    HashMap<Byte, Integer> frequencies = new HashMap<>();
    BufferedInputStream reader;
    byte[] bytes = {};

    try {
      reader = new BufferedInputStream(new FileInputStream(filename));
    } catch (FileNotFoundException f) {
      f.printStackTrace();
      return;
    }
    try {
      bytes = reader.readAllBytes();
    } catch (IOException i) {
      i.printStackTrace();
    }

    for (byte b : bytes) {
      if (!frequencies.containsKey(b)) {
        frequencies.put(b, 1);
      } else {
        Integer newWeight = frequencies.get(b) + 1;
        frequencies.replace(b, newWeight);
      }
    }

    if (bytes.length == 0) {
      HuffmanSave huffmanSave = new HuffmanSave(new BitSequence(), frequencies);
      serialize(huffmanSave, zippedFilename);
      return;
    }


    // create the BitSequence encoding
    HuffTreeBuilder treeBuilder = new HuffTreeBuilder(frequencies);
    treeBuilder.buildTree();
    BitSequence encoding = treeBuilder.encodeBitSequence(bytes);


    // create the HuffmanSave and serialize it
    HuffmanSave huffmanSave = new HuffmanSave(encoding, frequencies);
    serialize(huffmanSave, zippedFilename);

  }

  /**
   * Serialize the HuffmanSave.
   *
   * @param huffmanSave - the save to serialize
   * @param zippedFilename - the name of the compressed file
   */
  private static void serialize(HuffmanSave huffmanSave, String zippedFilename) {
    try {
      FileOutputStream fileOut = new FileOutputStream(zippedFilename);
      ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
      objOut.writeObject(huffmanSave);
      objOut.close();
      fileOut.close();
    } catch (IOException i) {
      i.printStackTrace();
    }
  }
}

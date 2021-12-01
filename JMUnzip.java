import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/*
 * This work complies with the JMU Honor Code.
 * References and Acknowledgments: https://www.tutorialspoint.com/java/java_serialization.htm
 * for serialization help and OpenDSA for Huffman coding help.
 */

/**
 * Program to decompress files that were compressed by JMZip.
 * Performed by Huffman decoding.
 */
public class JMUnzip {

  /**
   * main.
   */
  public static void main(String[] args) {

    // invalid argument checking
    if (args.length < 2) {
      printErrorMessage();
      return;
    }
    if (!args[0].endsWith(".jmz")) {
      printErrorMessage();
      return;
    }


    String zippedFilename = args[0];
    String filename = args[1];


    // deserialize the HuffmanSave
    HuffmanSave huffmanSave;
    try {
      FileInputStream fileIn = new FileInputStream(zippedFilename);
      ObjectInputStream objIn = new ObjectInputStream(fileIn);
      huffmanSave = (HuffmanSave) objIn.readObject();
      objIn.close();
      fileIn.close();
    } catch (IOException | ClassNotFoundException i) {
      i.printStackTrace();
      return;
    }


    // handle empty file
    if (huffmanSave.getEncoding().length() == 0) {
      writeFile(filename, new byte[0]);
      return;
    }


    // rebuild the HuffTree
    HuffTreeBuilder treeBuilder = new HuffTreeBuilder(huffmanSave.getFrequencies());
    treeBuilder.buildTree();


    // deconstruct the BitSequence
    byte[] bytes = treeBuilder.decodeBitSequence(huffmanSave.getEncoding());


    // restore the original file
    writeFile(filename, bytes);
  }

  /**
   * Write the decompressed file.
   */
  private static void writeFile(String filename, byte[] bytes) {
    FileOutputStream writer = null;

    try {
      writer = new FileOutputStream(filename);
    } catch (FileNotFoundException f) {
      f.printStackTrace();
    }

    try {
      assert writer != null;
      writer.write(bytes);
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  /**
   * Prints the error message.
   */
  private static void printErrorMessage() {
    System.out.println("Please provide arguments in the format of:");
    System.out.println("java JMUnzip FILE.jmz FILE.x");
  }
}

package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import eclipsesource.json.Json;
import eclipsesource.json.JsonArray;
import eclipsesource.json.JsonObject;
import eclipsesource.json.JsonObject.Member;
import eclipsesource.json.JsonValue;
import graph.Graph;
import graph.Node;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

  private Graph graph;

  public MainFrame() {
    initGraph();
    System.out.println(graph);
    initUI();
  }

  private void initGraph() {
    // Read the Json
    JsonObject aspects = null;
    try {
      aspects = Json.parse(new InputStreamReader(MainFrame.class.getResourceAsStream("/graph/Aspects.json")))
          .asObject();
    } catch (IOException e) {
      e.printStackTrace();
    }

    graph = new Graph();

    for (Member aspect : aspects) {
      graph.add(new Node(aspect.getName()));
      JsonArray list = aspect.getValue().asArray();
      for (JsonValue a : list) {
        graph.connect(aspect.getName(), a.asString());
      }
    }
  }

  private void initUI() {
    setTitle("Thaumcraft Helper");
    setSize(300, 200);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    ButtonPanel bPanel = new ButtonPanel(graph);
    add(bPanel);

    pack();
  }

  public static void setLookAndFeel(String name) {
    try {
      
      LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();
      for (LookAndFeelInfo info : installed) {
        System.out.println(info.getClassName());
        if (info.getName().contains(name)) {
          UIManager.setLookAndFeel(info.getClassName());
        }
      }
      
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        setLookAndFeel("Nimbus");
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
      }
    });
  }
}
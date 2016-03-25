package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
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
    initUI();
  }

  private void initGraph() {
    // Read the Json
    JsonObject aspects = null;
    try {
      aspects = Json.parse(new InputStreamReader(getClass().getResourceAsStream("/graph/Aspects.json"))).asObject();
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
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Setting the minimum size helps expand the currently empty output pane
    setMinimumSize(new Dimension(600, 0));

    OutputPanel outputPanel = new OutputPanel();
    JScrollPane scrollPane = new JScrollPane(outputPanel);
    add(scrollPane, BorderLayout.CENTER);
    scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10),
        BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(170, 170, 180)),
            BorderFactory.createEmptyBorder(4, 4, 4, 4))));
    // Force the scroll bar to always show
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    // The SmartScroller makes the scrollPane always scroll to the end when new paths are added
    new SmartScroller(scrollPane, SmartScroller.HORIZONTAL, SmartScroller.END);

    Sidebar sidebar = new Sidebar(graph, outputPanel);
    add(sidebar, BorderLayout.LINE_START);

    pack();
    setLocationRelativeTo(null);
  }

  public static void setLookAndFeel(String name) {
    try {

      LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();
      for (LookAndFeelInfo info : installed) {
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

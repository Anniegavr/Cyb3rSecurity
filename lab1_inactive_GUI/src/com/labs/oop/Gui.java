package com.labs.oop;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Gui extends JFrame
{
    private JMenuBar menu;
    private JMenu m1,m2,m3;
    private JMenuItem AddNew,Search, Convert,Delete,Exit, Clear,Help;
    private JButton
            btnAddNew,btnSearch,btnDelete, btnConvert, btnClear,btnExit;
    private JPanel pMain,pSouth,pNorth,pCenter;
    private JTextArea tac;
    private JLabel lbllogo;


    public Gui() {
        //menu bar and menu item initialization//////////////////////////////////////////////
        menu = new JMenuBar();
        m1 = new JMenu("Options");
        m2 = new JMenu("Programs");
        m3 = new JMenu("Help");

        AddNew = new JMenuItem("AddNew"); //add one more file to the others
        Search = new JMenuItem("Search"); //search name through the added files
        Convert = new JMenuItem("Convert");
        Delete = new JMenuItem("Delete"); //delete all files
        Clear = new JMenuItem("Clear"); //remove a file
        Exit = new JMenuItem("Exit");
        Help = new JMenuItem("Help");

        //text area initialization
        tac = new JTextArea(2, 3);

        tac.setText("Files: none");

        tac.setForeground(Color.red);
        tac.setEditable(false);

        //button intialization
        btnAddNew = new JButton("ADDNEW");

        btnSearch = new JButton("SEARCH");

        btnDelete = new JButton("DELETE");

        btnConvert = new JButton("CONVERT");

        btnClear = new JButton("CLEAR");

        btnExit = new JButton("EXIT");

        //Linking buttons to their functionalities///////////////////////////////////////
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnAddNew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser= new JFileChooser();
                chooser.setCurrentDirectory(new File("c:\\temp"));
//                chooser.setFileFilter(new FileNameExtensionFilter("map","MAP"));
                int value = chooser.showOpenDialog(null);
                File f= chooser.getSelectedFile();
                String filename= f.getAbsolutePath();
                try{
                    FileReader reader = new FileReader(filename);
                    BufferedReader br = new BufferedReader(reader);
                    tac.read(br,null);
                    br.close();
                    tac.requestFocus();
                    tac.setText("Selected: "+ filename);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,ex);
                }
            }
        });

        btnConvert.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Main m = new Main();
            }
        });


        //initialization panel///////////////////////////////////////////////////////////////

        pMain = new JPanel();
        pNorth = new JPanel();
        pSouth = new JPanel();
        pCenter = new JPanel();

        lbllogo = new JLabel(new
                ImageIcon("..\\menu(10).png"), JLabel.CENTER); //fake path given

        //add menuitem to scroll-down menu
        m1.add(AddNew);
        m1.add(Search);
        m1.add(Convert);
        m1.add(Delete);
        m1.add(Clear);

        m2.add(Exit);

        m3.add(Help);

        menu.add(m1);
        menu.add(m2);
        menu.add(m3);

        pMain.add(btnAddNew);
        pMain.add(btnSearch);
        pMain.add(btnDelete);
        pMain.add(btnConvert);
        pMain.add(btnClear);
        pMain.add(btnExit);

        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
        pMain.setLayout(new GridLayout(6, 1));
        pMain.setBackground(Color.white);

//        pCenter.setBackground(Color.red);
        pCenter.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pCenter.setLayout(new GridLayout(2, 1));
        pCenter.add(lbllogo);
        pCenter.add(tac);

        pNorth.setBackground(Color.white);

        pNorth.add(menu);

        this.getContentPane().add(pMain, "West");
        this.getContentPane().add(pCenter, "Center");
        this.getContentPane().add(pNorth, "North");

        this.setSize(400, 300);
        this.setResizable(true);
        this.setLocation(150, 150);
        this.setTitle("MENU");
        this.setVisible(true);
    }
    public String getTac(){
        return String.valueOf(tac);
    }

}

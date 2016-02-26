package ru.devsaider.tfmxml;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

/**
 * @author Ruslan Devsaider <me@devsaider.ru>
 */
public class SimpleFetchForm {

    private JButton OKButton;
    private JPanel SimpleFetchForm;
    private JTextField textField1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Devsaider's XML parser");
        SimpleFetchForm sff = new SimpleFetchForm();

        sff.OKButton.addActionListener((ae) -> {
            try {
                String finalXML;
                String mapCodeRaw = sff.textField1.getText().replace("@", "");
                if (StringUtils.isNumeric(mapCodeRaw) && (finalXML = XMLParser.fetchSingle(Integer.parseInt(mapCodeRaw))) != null) {
                    StringSelection selection = new StringSelection(finalXML);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);

                    JOptionPane.showMessageDialog(null, "XML code was copied to your clipboard");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid XML code");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(300, 125));

        frame.setContentPane(sff.SimpleFetchForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

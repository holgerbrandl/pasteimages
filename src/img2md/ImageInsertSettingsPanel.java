/*
 * Created by JFormDesigner on Wed Jun 08 09:12:08 CEST 2016
 */

package img2md;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author unknown
 */
public class ImageInsertSettingsPanel extends JPanel {
    public ImageInsertSettingsPanel() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        panel2 = new JPanel();
        label2 = new JLabel();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {5, 0, 5, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Image Properties"));
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 2, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 5, 0, 5, 0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("File Name");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
            panel1.add(textField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- checkBox1 ----
            checkBox1.setText("Convert white to transparent");
            checkBox1.setSelected(true);
            panel1.add(checkBox1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- checkBox2 ----
            checkBox2.setText("Round corners");
            panel1.add(checkBox2, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        add(panel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Markdown Properties"));
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
            ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- label2 ----
            label2.setText("None yet.");
            panel2.add(label2, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        add(panel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JTextField textField1;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JPanel panel2;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

/*
 * Created by JFormDesigner on Wed Jun 08 09:12:08 CEST 2016
 */

package img2md;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author unknown
 */
public class ImageInsertSettingsPanel extends JPanel {
    public ImageInsertSettingsPanel() {
        initComponents();
    }


    public JCheckBox getWhiteCheckbox() {
        return whiteCheckbox;
    }


    public JCheckBox getRoundCheckbox() {
        return roundCheckbox;
    }


    public JLabel getTargetSizeLabel() {
        return targetSizeLabel;
    }


    public JSpinner getScaleSpinner() {
        return scaleSpinner;
    }


    public JTextField getNameInput() {
        return nameInput;
    }


    public JComboBox<String> getDirectoryField() {
        return directoryField;
    }

    public JComboBox<String> getFormatBox() {
        return formatBox;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        label1 = new JLabel();
        nameInput = new JTextField();
        label3 = new JLabel();
        directoryField = new JComboBox<>();
        label5 = new JLabel();
        formatBox = new JComboBox<>();
        panel4 = new JPanel();
        whiteCheckbox = new JCheckBox();
        roundCheckbox = new JCheckBox();
        label4 = new JLabel();
        targetSizeLabel = new JLabel();
        scaleSpinner = new JSpinner();
        label6 = new JLabel();
        panel2 = new JPanel();
        label2 = new JLabel();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{5, 0, 5, 0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("File Properties"));
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 150, 0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 5, 0, 5, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("File Name");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
            panel1.add(nameInput, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- label3 ----
            label3.setText("Directory Name");
            directoryField.setModel(new DefaultComboBoxModel<>(DirCache.get()));
            directoryField.setEditable(true);
            panel1.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
            panel1.add(directoryField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- label5 ----
            label5.setText("Format");
            panel1.add(label5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- formatBox ----
            formatBox.setModel(new DefaultComboBoxModel<>(new String[]{
                    "PNG",
                    "JPEG"
            }));
            panel1.add(formatBox, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        add(panel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));

        //======== panel4 ========
        {
            panel4.setBorder(new TitledBorder("Image Properties"));
            panel4.setLayout(new GridBagLayout());
            ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0};
            ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
            ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};

            //---- whiteCheckbox ----
            whiteCheckbox.setText("Convert white to transparent");
            whiteCheckbox.setSelected(true);
            panel4.add(whiteCheckbox, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- roundCheckbox ----
            roundCheckbox.setText("Round corners");
            panel4.add(roundCheckbox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- label4 ----
            label4.setText("Rescale Image");
            panel4.add(label4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- targetSizeLabel ----
            targetSizeLabel.setText("text");
            panel4.add(targetSizeLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- scaleSpinner ----
            scaleSpinner.setModel(new SpinnerNumberModel(100, 5, 200, 5));
            panel4.add(scaleSpinner, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- label6 ----
            label6.setText("%");
            panel4.add(label6, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        add(panel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Markdown Properties"));
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- label2 ----
            label2.setText("None yet.");
            panel2.add(label2, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JLabel label1;
    private JTextField nameInput;
    private JLabel label3;
    private JComboBox<String> directoryField;
    private JLabel label5;
    private JComboBox<String> formatBox;
    private JPanel panel4;
    private JCheckBox whiteCheckbox;
    private JCheckBox roundCheckbox;
    private JLabel label4;
    private JLabel targetSizeLabel;
    private JSpinner scaleSpinner;
    private JLabel label6;
    private JPanel panel2;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

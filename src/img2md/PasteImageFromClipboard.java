package img2md;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static img2md.ImageUtils.*;

public class PasteImageFromClipboard extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            Image imageFromClipboard = getImageFromClipboard();
            Image image = whiteToTransparent(toBufferedImage(imageFromClipboard));

            // add option to rescale image on the fly



        } catch (Exception e1) {
            DialogBuilder builder = new DialogBuilder();

            builder.setCenterPanel(new JLabel("Clipboard does not contain any image"));
            builder.setDimensionServiceKey("PasteImageFromClipboard.NoImage");
            builder.setTitle("No image in Clipboard");
            builder.removeAllActions();


            builder.addOkAction();

            builder.show();
            return;
        }

        showDialog();
    }

    // for more examples see
//    http://www.programcreek.com/java-api-examples/index.php?api=com.intellij.openapi.ui.DialogWrapper
    private boolean showDialog() {
        DialogBuilder builder = new DialogBuilder();
        ImageInsertSettingsPanel contentPanel = new ImageInsertSettingsPanel();
        builder.setCenterPanel(contentPanel);
        builder.setDimensionServiceKey("GrepConsoleSound");
        builder.setTitle("Sound settings");
        builder.removeAllActions();

        builder.addOkAction();
        builder.addCancelAction();

//        soundSettingsForm.setData(item.getSound());
        boolean isOk = builder.show() == DialogWrapper.OK_EXIT_CODE;
        if (!isOk) {
            return false;
        }

        return true;
    }


}

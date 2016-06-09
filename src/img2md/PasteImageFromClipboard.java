package img2md;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static img2md.ImageUtils.*;

public class PasteImageFromClipboard extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Image imageFromClipboard = getImageFromClipboard();

        if (imageFromClipboard == null) {
            DialogBuilder builder = new DialogBuilder();

            builder.setCenterPanel(new JLabel("Clipboard does not contain any image"));
            builder.setDimensionServiceKey("PasteImageFromClipboard.NoImage");
            builder.setTitle("No image in Clipboard");
            builder.removeAllActions();


            builder.addOkAction();

            builder.show();
            return;
        }


        // add option to rescale image on the fly

        ImageInsertSettingsPanel insertSettingsPanel = showDialog();
        if (insertSettingsPanel == null) {
            return;
        }

        String imageName = insertSettingsPanel.getNameInput().getText();
        boolean whiteAsTransparent = insertSettingsPanel.getWhiteCheckbox().isSelected();
        boolean roundCorners = insertSettingsPanel.getRoundCheckbox().isSelected();


        BufferedImage bufferedImage = toBufferedImage(imageFromClipboard);
//        if (whiteAsTransparent)
//            bufferedImage = toBufferedImage(whiteToTransparent(bufferedImage));

//        if (roundCorners) {
//            bufferedImage = toBufferedImage(makeRoundedCorner(bufferedImage, 20));
//        }




        // deterimine save path for the image
        Editor ed = e.getData(PlatformDataKeys.EDITOR);
        if (ed == null) {
            return;
        }


        // from http://stackoverflow.com/questions/17915688/intellij-plugin-get-code-from-current-open-file
        Document currentDoc = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        File curDocument = new File(currentFile.getPath());

        File imageDir = new File(curDocument.getParent(), "assets");
        if(!imageDir.exists() || !imageDir.isDirectory()) imageDir.mkdir();

        File imageFile = new File(imageDir,  imageName + ".png");

        save(bufferedImage, imageFile, "png");



        // inject image element current markdown document
        insertImageElement(ed, curDocument.getParentFile().toPath().relativize(imageFile.toPath()).toFile(), e);


//            String text = ed.getSelectionModel().getSelectedText();
//            if (StringUtil.isEmptyOrSpaces(text)) {
//                ed.getSelectionModel().selectLineAtCaret();
//                ed.getSelectionModel().getSelectedText().replace("", "");
//
//            int caret = ed.getSelectionModel().getSelectionStart();
//            PsiFile file = e.getData(DataKeys.PSI_FILE);
//            if (file != null) {
//                for (PsiElement el : file.getChildren()) {
//                    if (el.getTextRange().contains(caret)) {
//                        if (!(el instanceof PsiWhiteSpace) && !(el instanceof PsiComment)) {
//                            push2R(el.getText());
//                        }
//                        return;
//                    }
//                }
//            }
//            } else {
//            }


    }


    public void insertImageElement(final @NotNull Editor editor, File imageFile, AnActionEvent e) {
//        ed.getCaretModel().getCurrentCaret().getEditor();

        Runnable r = ()-> EditorModificationUtil.insertStringAtCaret(editor, "![]("+imageFile.toString()+")");

        WriteCommandAction.runWriteCommandAction(e.getProject(), r);
    }


    // for more examples see
//    http://www.programcreek.com/java-api-examples/index.php?api=com.intellij.openapi.ui.DialogWrapper
    private static ImageInsertSettingsPanel showDialog() {
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
            return null;
        }


        return contentPanel;
    }
}

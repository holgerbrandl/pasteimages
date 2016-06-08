package img2md;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
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

        ImageInsertSettingsPanel insertSettingsPanel = showDialog(imageFromClipboard);
        if (insertSettingsPanel == null) {
            return;
        }

        String imageName = insertSettingsPanel.getNameInput().getText();
        boolean whiteAsTransparent = insertSettingsPanel.getWhiteCheckbox().isSelected();
        boolean roundCorners = insertSettingsPanel.getRoundCheckbox().isSelected();


        BufferedImage bufferedImage = toBufferedImage(imageFromClipboard);
        if (whiteAsTransparent)
            bufferedImage = toBufferedImage(whiteToTransparent(bufferedImage));

        if (roundCorners) {
            bufferedImage = toBufferedImage(makeRoundedCorner(bufferedImage, 20));
        }


        // deterimine save path
        Editor ed = e.getData(PlatformDataKeys.EDITOR);
        if (ed == null) {
            return;
        }


        String filePath = ed.getProject().getProjectFilePath();

        File imageFile = new File("sdf"); // make sure that this is relative

        // inject image element current markdown document
        insertImageElement(ed, imageFile, e);


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


//        PsiFile file = e.getData(PlatformDataKeys.PSI_FILE);
//
//        final AccessToken token = WriteAction.start();
//        try {
//            PsiElement elementAt = file.findElementAt(ed.getCaretModel().getOffset());
//            elementAt.addAfter()
//
//
//            PsiElement importStatement = RElementFactory.createFuncallFromText(project, "library(" + packageName + ");");
//            if (insertAfter != null && insertAfter.getTextOffset() < element.getTextOffset()) {
//                importStatement = insertAfter.getParent().addAfter(importStatement, insertAfter);
//                insertAfter.getParent().addBefore(RElementFactory.createLeafFromText(project, "\n"), importStatement);
//            } else {
//                insertAfter = file.getFirstChild();
//                importStatement = insertAfter.getParent().addBefore(importStatement, insertAfter);
//                insertAfter.getParent().addAfter(RElementFactory.createLeafFromText(project, "\n"), importStatement);
//            }
//
////        if (endsWithSemicolon(inserAfter)) {
////            addedImport.addBefore(BnfElementFactory.createLeafFromText(project, ";"), null);
////            if (inserAfter.getNextSibling() instanceof PsiWhiteSpace) {
////                inserAfter.getParent().addAfter(BnfElementFactory.createLeafFromText(project, "\n"), addedImport);
////            }
////        }
//            //            final FileEditor selectedEditor = FileEditorManager.getInstance(project).getSelectedEditor(insertAfter.getContainingFile().getVirtualFile());
////            if (selectedEditor instanceof TextEditor) {
////                final Editor ed = ((TextEditor) selectedEditor).getEditor();
////                ed.getCaretModel().moveToOffset(addedRule.getTextRange().getEndOffset() - (BnfIntroduceRuleHandler.endsWithSemicolon(addedRule) ? 1 : 0));
////                ed.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
////            }
//        } finally {
//            token.finish();
//        }
    }


    // for more examples see
//    http://www.programcreek.com/java-api-examples/index.php?api=com.intellij.openapi.ui.DialogWrapper
    private static ImageInsertSettingsPanel showDialog(Image image) {
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

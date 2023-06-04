package img2md;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.UUID;

import static img2md.ImageUtils.*;

public class PasteImageFromClipboard extends AnAction {


    private static final String DOC_BASE_NAME = "{document_name}";
    private static final String PI_WHITE_TRANSPARENT = "PI_WHITE_TRANSPARENT";
    private static final String PI_ROUND_CORNERS = "PI__IMG_ROUND_CORNERS";
    private static final String PI_IMG_SCALE = "PI__IMG_SCALE";
    private static final String PI_LAST_DIR_PATTERN = "PI__LAST_DIR_PATTERN";


    @Override
    public void actionPerformed(AnActionEvent e) {
        ImageWithInfo imageFromClipboard = getImageFromClipboard();

        // deterimine save path for the image
        Editor ed = e.getData(PlatformDataKeys.EDITOR);
        if (ed == null) {
            return;
        }


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


        // from http://stackoverflow.com/questions/17915688/intellij-plugin-get-code-from-current-open-file
        Document currentDoc = FileEditorManager.getInstance(ed.getProject()).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        File curDocument = new File(currentFile.getPath());


        // add option to rescale image on the fly
        BufferedImage bufferedImage = toBufferedImage(imageFromClipboard.image);

        if (bufferedImage == null) return;

        Dimension dimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        ImageInsertSettingsPanel insertSettingsPanel = showDialog(curDocument, dimension, imageFromClipboard.name);

        if (insertSettingsPanel == null) return;

        String imageName = insertSettingsPanel.getNameInput().getText();
        boolean whiteAsTransparent = insertSettingsPanel.getWhiteCheckbox().isSelected();
        boolean roundCorners = insertSettingsPanel.getRoundCheckbox().isSelected();
        double scalingFactor = ((Integer) insertSettingsPanel.getScaleSpinner().getValue()) * 0.01;


        if (whiteAsTransparent) {
            bufferedImage = toBufferedImage(whiteToTransparent(bufferedImage));
        }
//
        if (roundCorners) {
            bufferedImage = toBufferedImage(makeRoundedCorner(bufferedImage, 20));
        }

        if (scalingFactor != 1) {
            bufferedImage = scaleImage(bufferedImage,
                    (int) Math.round(bufferedImage.getWidth() * scalingFactor),
                    (int) Math.round(bufferedImage.getHeight() * scalingFactor));
        }

        // make selectable
//        File imageDir = new File(curDocument.getParent(), ".images");
        String mdBaseName = curDocument.getName().replace(".md", "").replace(".Rmd", "");

//        File imageDir = new File(curDocument.getParent(), "."+ mdBaseName +"_images");
        String dirPattern = insertSettingsPanel.getDirectoryField().getSelectedItem().toString();
        DirCache.addIfNotExist(dirPattern);
        String format = insertSettingsPanel.getFormatBox().getSelectedItem().toString().toLowerCase();


        File imageDir = new File(curDocument.getParent(), dirPattern.replace(DOC_BASE_NAME, mdBaseName));


        if (!imageDir.exists() || !imageDir.isDirectory()) imageDir.mkdirs();


        File imageFile = new File(imageDir, imageName + "." + format);

        // todo should we silently override the image if it is already present?
        save(bufferedImage, imageFile, format);

//        PropertiesComponent.getInstance()

//        VirtualFile baseDir = e.getProject().getBaseDir();
//        final VirtualFile targetDir = baseDir.getFileSystem().findFileByPath(imageFile.getParentFile().getAbsolutePath());
//        if(targetDir != null) {
//            WriteCommandAction.runWriteCommandAction(e.getProject(), new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        targetDir.createChildData(this, imageFile.getName());
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            });
//        }

        // inject image element current markdown document
        insertImageElement(ed, curDocument.getParentFile().toPath().relativize(imageFile.toPath()).toFile());

        // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206144389-Create-virtual-file-from-file-path
        VirtualFile fileByPath = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(imageFile);
        assert fileByPath != null;


        ApplicationManager.getApplication().invokeLater(() -> {
            AbstractVcs usedVcs = ProjectLevelVcsManager.getInstance(ed.getProject()).getVcsFor(fileByPath);
            if (usedVcs != null && usedVcs.getCheckinEnvironment() != null) {
                ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    usedVcs.getCheckinEnvironment().scheduleUnversionedFilesForAddition(Collections.singletonList(fileByPath));
                });
            }
        });


        // update directory pattern preferences for file and globally
        PropertiesComponent pc = PropertiesComponent.getInstance();
        pc.setValue("PI__DIR_PATTERN_FOR_" + currentFile.getPath(), dirPattern);

        pc.setValue(PI_WHITE_TRANSPARENT, whiteAsTransparent);
        pc.setValue(PI_ROUND_CORNERS, roundCorners);
        pc.setValue(PI_IMG_SCALE, (float) scalingFactor, -1);
    }


    private void insertImageElement(final @NotNull Editor editor, File imageFile) {
        String relImagePath = imageFile.toString().replace('\\', '/');
        Runnable r = () -> EditorModificationUtil.insertStringAtCaret(editor, "![](" + relImagePath + ")");

        WriteCommandAction.runWriteCommandAction(editor.getProject(), r);
    }


    // for more examples see
//    http://www.programcreek.com/java-api-examples/index.php?api=com.intellij.openapi.ui.DialogWrapper
    private static ImageInsertSettingsPanel showDialog(File curDocument, Dimension imgDim, String proposedName) {
        DialogBuilder builder = new DialogBuilder();
        ImageInsertSettingsPanel contentPanel = new ImageInsertSettingsPanel();


        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double scalingFactor = (Integer) contentPanel.getScaleSpinner().getValue() * 0.01;

                JLabel targetSizeLabel = contentPanel.getTargetSizeLabel();

                if (Math.abs(1.0 - scalingFactor) < 1E-5) {
                    targetSizeLabel.setText((int) imgDim.getWidth() + " x " + (int) imgDim.getHeight());

                } else {
                    long newWidth = Math.round(imgDim.getWidth() * scalingFactor);
                    long newHeight = Math.round(imgDim.getHeight() * scalingFactor);

                    targetSizeLabel.setText(newWidth + " x " + newHeight);
                }
            }
        };

        listener.stateChanged(null);
        contentPanel.getScaleSpinner().addChangeListener(listener);

        // restore directory pattern preferences for file and globally

        PropertiesComponent pc = PropertiesComponent.getInstance();
        String dirPattern = pc.getValue("PI__DIR_PATTERN_FOR_" + curDocument.getPath());

        if (dirPattern == null) dirPattern = "." + DOC_BASE_NAME + "_images";


//        contentPanel.getDirectoryField().setText(dirPattern);


        contentPanel.getNameInput().setText(
                (proposedName != null)
                    ? proposedName
                    : UUID.randomUUID().toString().substring(0, 8)
        );

        //  Load and set last used Image Properties
        boolean whiteAsTransparent = pc.getBoolean(PI_WHITE_TRANSPARENT, false);
        boolean roundCorners = pc.getBoolean(PI_ROUND_CORNERS, false);
        float scalingFactor = pc.getFloat(PI_IMG_SCALE, 1.0f);

        contentPanel.getScaleSpinner().setValue(Math.round(scalingFactor * 100));
        contentPanel.getWhiteCheckbox().setSelected(whiteAsTransparent);
        contentPanel.getRoundCheckbox().setSelected(roundCorners);

        builder.setCenterPanel(contentPanel);
        builder.setDimensionServiceKey("GrepConsoleSound");
        builder.setTitle("Paste Image Settings");
        builder.removeAllActions();

        builder.addOkAction();
        builder.addCancelAction();

        builder.setPreferredFocusComponent(contentPanel.getNameInput());

        boolean isOk = builder.show() == DialogWrapper.OK_EXIT_CODE;
        if (!isOk) {
            return null;
        }

        return contentPanel;
    }
}

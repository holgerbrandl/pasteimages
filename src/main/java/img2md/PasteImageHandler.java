/*
 * Copyright (c) 2015-2017 Vladimir Schneider <vladimir.schneider@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package img2md;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorTextInsertHandler;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Producer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.Transferable;

public class PasteImageHandler extends EditorActionHandler {
    private static final Logger LOG = Logger.getInstance("img2md.PasteHandler");
    private final EditorActionHandler myOriginalHandler;


    public PasteImageHandler(EditorActionHandler originalAction) {
        myOriginalHandler = originalAction;
    }


    private AnActionEvent createAnEvent(AnAction action, @NotNull DataContext context) {
        Presentation presentation = action.getTemplatePresentation().clone();
        return new AnActionEvent(null, context, ActionPlaces.UNKNOWN, presentation, ActionManager.getInstance(), 0);
    }

    @Override
    public void execute(@NotNull Editor editor, @Nullable DataContext dataContext) {
        super.execute(editor, dataContext);
    }
    //    @Override
//    public void execute(Editor editor, DataContext dataContext, Producer<Transferable> producer) {
//        Caret caret = editor.getCaretModel().getPrimaryCaret();
//        doExecute(editor, caret, dataContext);
//    }


    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        if (editor instanceof EditorEx) {
            VirtualFile virtualFile = ((EditorEx) editor).getVirtualFile();
            if (virtualFile != null) {
                FileType fileType = virtualFile.getFileType();
                if ("Markdown".equals(fileType.getName())) {
                    Image imageFromClipboard = ImageUtils.getImageFromClipboard();
                    if (imageFromClipboard != null) {
//                        assert caret == null : "Invocation of 'paste' operation for specific caret is not supported";
                        PasteImageFromClipboard action = new PasteImageFromClipboard();
                        AnActionEvent event = createAnEvent(action, dataContext);
                        action.actionPerformed(event);
                        return;
                    }
                }
            }
        }

        if (myOriginalHandler != null) {
            myOriginalHandler.execute(editor, null, dataContext);
        }
    }
}

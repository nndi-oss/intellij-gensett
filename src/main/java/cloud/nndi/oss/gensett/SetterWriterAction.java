package cloud.nndi.oss.gensett;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SetterWriterAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get all the required data from data keys
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if a selection exists
        e.getPresentation().setEnabledAndVisible(project != null && editor != null);
        final Document document = editor.getDocument();
        // Work off of the primary caret to get the selection info

        editor.getSelectionModel().selectLineAtCaret();
        final String line =  editor.getSelectionModel().getSelectedText();

        if (line == null)  return;
        if (line.isEmpty()) return;

        CaretModel caretModel = editor.getCaretModel();
        Caret primaryCaret = caretModel.getPrimaryCaret();

        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();

        // Must do this document change in a write action context.
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                SetterWriter sw = new SetterWriter(line);
                
                document.replaceString(start, end, sw.generateCode());
            } catch(Exception ioe) {
                Notification notification = new Notification(
                    "cloud.nndi.oss.gensett",
                    "Gensett failed to generate your code :(",
                    ioe.getMessage(),
                    NotificationType.ERROR
                );
                Notifications.Bus.notify(notification, project);
            }
        });
        // De-select the text range that was just replaced
        primaryCaret.removeSelection();
    }
}

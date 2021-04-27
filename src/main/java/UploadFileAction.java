import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author dongzhonghua
 * Created on 2021-04-21
 */
public class UploadFileAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取文件路径
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String path = file.getPath();
        Notifications.Bus.notify(new Notification("file-upload", "上传文件到测试机", path, NotificationType.INFORMATION));

        // 读取文件
        FormDialog formDialog = new FormDialog(e.getProject(), path);
        formDialog.setResizable(true);
        formDialog.show();
        // 获取服务

        // 上传

        // 通知上传结果
    }
}

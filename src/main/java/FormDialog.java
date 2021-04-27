import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

/**
 * @author dongzhonghua
 * Created on 2021-04-21
 */
public class FormDialog extends DialogWrapper {

    private String projectName; //假如需要获取到项目名，作为该类的属性放进来
    private String path;
    private FormTestSwing formTestSwing;

    // DialogWrapper没有默认的无参构造方法，所以需要重写构造方法，它提供了很多重载构造方法，
    // 这里使用传project类型参数的那个，通过Project对象可以获取当前IDEA内打开的项目的一些属性，
    // 比如项目名，项目路径等
    public FormDialog(@Nullable Project project, String path) {
        super(project);
        setTitle("upload file"); // 设置会话框标题
        this.projectName = project.getName();
        this.path = path;
        formTestSwing = new FormTestSwing(this);
        init();
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最上方的位置
    @Override
    protected JComponent createNorthPanel() {
        return formTestSwing.initNorth(); //返回位于会话框north位置的swing样式
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最下方的位置
    @Override
    protected JComponent createSouthPanel() {
        return formTestSwing.initSouth();
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的中央位置
    @Override
    protected JComponent createCenterPanel() {
        return formTestSwing.initCenter(path);
    }
}

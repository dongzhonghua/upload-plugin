import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.ComboBox;

import api.UploadFileApi;
import model.HostInfo;
import model.InstanceResult;
import model.Result;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dongzhonghua
 * Created on 2021-04-21
 */
public class FormTestSwing {

    private JPanel north = new JPanel();

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();

    private InstanceResult instances;
    private Set<String> dictionaries = new HashSet<>();

    private HashMap<String, String> hostnameUrlMap = new HashMap<>();

    //为了让位于底部的按钮可以拿到组件内容，这里把表单组件做成类属性

    private JLabel uploadPath = new JLabel("上传的路径：");
    private JLabel registerCenter = new JLabel("注册中心地址：");
    private JLabel targetHostKeyword = new JLabel("服务器keyword：");
    private JLabel targetHost = new JLabel("服务器地址：");
    private JLabel targetPathKeyword = new JLabel("文件夹keyword：");
    private JLabel targetPath = new JLabel("目的文件夹：");

    private JTextField uploadPathContent = new JTextField();
    private JTextField registerCenterContent = new JTextField();
    private JTextField targetHostKeywordContent = new JTextField();
    private ComboBox<String> targetHostContent = new ComboBox<>(); // 下拉框

    private JTextField targetPathKeywordContent = new JTextField();
    private ComboBox<String> targetPathContent = new ComboBox<>(); // 下拉框

    private static final String DICTIONARY_CACHE_FILE_PATH = "/tmp/idea-upload-plugin-cache.log";
    private FormDialog formDialog;

    public FormTestSwing(FormDialog formDialog) {
        this.formDialog = formDialog;
    }

    public JPanel initNorth() {

        //定义表单的标题部分，放置到IDEA会话框的顶部位置
        JLabel title = new JLabel("");
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26)); //字体样式
        title.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        title.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        north.add(title);
        return north;
    }

    public JPanel initCenter(String path) {

        //定义表单的主体部分，放置到IDEA会话框的中央位置

        //一个简单的3行2列的表格布局
        center.setLayout(new GridLayout(6, 2));

        center.add(uploadPath);
        uploadPathContent.setText(path);
        center.add(uploadPathContent);

        center.add(registerCenter);
        registerCenterContent.setText("channelinfo.test.gifshow.com");
        addHostList();
        center.add(registerCenterContent);

        center.add(targetHostKeyword);
        center.add(targetHostKeywordContent);

        center.add(targetHost);
        center.add(targetHostContent);

        center.add(targetPathKeyword);
        center.add(targetPathKeywordContent);

        center.add(targetPath);
        targetPathContent.setEditable(true);
        // 读取文件获取缓存的文件夹
        try {
            File cacheFile = new File(DICTIONARY_CACHE_FILE_PATH);
            if (!cacheFile.exists() && cacheFile.createNewFile() || cacheFile.exists()) {
                dictionaries = new HashSet<>(FileUtils.readLines(cacheFile, StandardCharsets.UTF_8));
                addDictionaryList();
            }
        } catch (Exception ignored) {
        }
        center.add(targetPathContent);


        registerCenterContent.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                targetHostContent.removeAllItems();
            }

            @Override
            public void focusLost(FocusEvent e) {
                addHostList();
            }
        });

        // 通过关键字过滤hostname
        targetHostKeywordContent.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                targetHostContent.removeAllItems();
                addHostAddrList();
                targetHostContent.showPopup();
            }
        });

        // 通过关键字过滤文件夹
        targetPathKeywordContent.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                targetPathContent.removeAllItems();
                addDictionaryList();
                targetPathContent.showPopup();
            }
        });
        return center;
    }

    private void addHostList() {
        String registerCenterHost = registerCenterContent.getText();
        // 向注册中心请求服务器列表, 向hostnameContent中赋值
        Retrofit retrofit = new Builder()
                .baseUrl(String.format("http://%s", registerCenterHost))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(UploadFileApi.class).getInstanceInfo()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Result<InstanceResult>> call,
                            Response<Result<InstanceResult>> response) {
                        Result<InstanceResult> result = response.body();
                        if (result != null) {
                            instances = result.getData();
                            addHostAddrList();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<InstanceResult>> call, Throwable t) {
                        Notifications.Bus.notify(new Notification("file-upload", "获取实例失败", t.getMessage(),
                                NotificationType.ERROR));
                    }
                });
    }

    private void addDictionaryList() {
        String dictKeyword = targetPathKeywordContent.getText();
        for (String dic : dictionaries) {
            if (dictKeyword.length() > 0) {
                if (!dic.contains(dictKeyword)) {
                    continue;
                }
            }
            targetPathContent.addItem(dic);
        }
    }

    private void addHostAddrList() {
        String hostnameKey = targetHostKeywordContent.getText();
        System.out.println(hostnameKey);
        for (HostInfo hostInfo : instances.getHostInfoList()) {
            String hostname = hostInfo.getHostname();
            if (hostnameKey.length() > 0) {
                if (!hostname.contains(hostnameKey)) {
                    continue;
                }
            }
            targetHostContent.addItem(hostname);
            hostnameUrlMap.put(hostname, hostInfo.getHomePageUrl());
        }
    }

    public JPanel initSouth() {

        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("提交");
        submit.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        submit.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        south.add(submit);

        //按钮事件绑定
        submit.addActionListener(e -> {
            //文件上传
            String targetHost = targetHostContent.getItem();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(String.format("http://%s", registerCenterContent.getText()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            File file = new File(uploadPathContent.getText());
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));

            String targetPath = targetPathContent.getItem();
            if (targetHost.equals(instances.getHostname())) {
                targetHost = "null";
            }
            retrofit.create(UploadFileApi.class).upload(part, targetPath, hostnameUrlMap.get(targetHost))
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Result<String>> call,
                                Response<Result<String>> response) {
                            Result<String> result = response.body();
                            if (result != null) {
                                Notifications.Bus.notify(new Notification("file-upload", "上传成功！", result.getData(),
                                        NotificationType.INFORMATION));
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<String>> call, Throwable t) {
                            t.printStackTrace();
                            Notifications.Bus.notify(new Notification("file-upload", "上传文件到测试机失败", t.getMessage(),
                                    NotificationType.ERROR));
                        }
                    });


            // 最后把文件夹路径缓存到文件中。
            dictionaries.add(targetPath);
            File cacheFile = new File(DICTIONARY_CACHE_FILE_PATH);
            try {
                if (!cacheFile.exists() && cacheFile.createNewFile() || cacheFile.exists()) {
                    FileUtils.writeLines(cacheFile, dictionaries);
                }
            } catch (IOException ignored) {
            }
            formDialog.disposeIfNeeded();
        });

        return south;
    }
}

package img2md;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author pengqingsong
 * @date 2020/1/7
 * @desc
 */
public class Setting implements Configurable {
    private JPanel panel1;
    private JTextField qiniuImgUrlPrefixField;
    private JTextField qiniuAccessKeyField;
    private JTextField qiniuSecretKeyField;
    private JTextField qiniuBucketNameField;
    private JTabbedPane saveImgPanel;
    private JLabel imgSaveLocationLabel;
    private String imageSaveLocation = "LOCAL";

    public Setting() {
        saveImgPanel.addChangeListener(e -> {
            JTabbedPane source = (JTabbedPane) e.getSource();
            int selectedIndex = source.getSelectedIndex();
            onImageSaveLocationChanged(selectedIndex);
        });
    }


    private void onImageSaveLocationChanged(int selectedIndex) {
        if (selectedIndex == 0) {
            imageSaveLocation = "LOCAL";
        } else if (selectedIndex == 1) {
            imageSaveLocation = "QINIU";
        } else {
            imageSaveLocation = "ALIYUN";
        }
        imgSaveLocationLabel.setText(imageSaveLocation);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "PasteImageToMarkdown";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel1;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        PropertiesComponent.getInstance().setValue(Constants.IMAGE_SAVE_LOCATION, imageSaveLocation);

        //qiniu
        PropertiesComponent.getInstance().setValue(Constants.QINIU_ACCESS_KEY, qiniuAccessKeyField.getText());
        PropertiesComponent.getInstance().setValue(Constants.QINIU_SECRET_KEY, qiniuSecretKeyField.getText());
        PropertiesComponent.getInstance().setValue(Constants.QINIU_IMG_URL_PREFIX, qiniuImgUrlPrefixField.getText());
        PropertiesComponent.getInstance().setValue(Constants.QINIU_BUCKET_NAME, qiniuBucketNameField.getText());

        //aliyun



        if("ALIYUN".equalsIgnoreCase(imageSaveLocation)){
            throw new ConfigurationException("NOT SUPPORT ALIYUN(阿里云) NOW!");
        }
    }


    @Override
    public void reset() {
        String imageSaveLocationValue = PropertiesComponent.getInstance().getValue(Constants.IMAGE_SAVE_LOCATION);
        if (imageSaveLocationValue == null || imageSaveLocationValue.trim().length() == 0) {
            imageSaveLocation = "LOCAL";
        } else {
            imageSaveLocation = imageSaveLocationValue;
        }

        if ("LOCAL".equalsIgnoreCase(imageSaveLocation)) {
            saveImgPanel.setSelectedIndex(0);
            onImageSaveLocationChanged(0);
        } else if ("QINIU".equalsIgnoreCase(imageSaveLocation)) {
            saveImgPanel.setSelectedIndex(1);
            onImageSaveLocationChanged(1);
        } else {
            saveImgPanel.setSelectedIndex(2);
            onImageSaveLocationChanged(2);
        }

        qiniuImgUrlPrefixField.setText(PropertiesComponent.getInstance().getValue(Constants.QINIU_IMG_URL_PREFIX));
        qiniuAccessKeyField.setText(PropertiesComponent.getInstance().getValue(Constants.QINIU_ACCESS_KEY));
        qiniuSecretKeyField.setText(PropertiesComponent.getInstance().getValue(Constants.QINIU_SECRET_KEY));
        qiniuBucketNameField.setText(PropertiesComponent.getInstance().getValue(Constants.QINIU_BUCKET_NAME));
    }
}

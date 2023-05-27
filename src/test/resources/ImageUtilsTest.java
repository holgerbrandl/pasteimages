package img2md;

import com.google.common.collect.Lists;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

public class ImageUtilsTest {
    @Test
    public void testGetImageFromClipboardString() {
        Assert.assertNull(ImageUtils.getImageFromClipboard(new TestTransferable<>("hello world", DataFlavor.stringFlavor)));
    }

    @Test
    public void testGetImageFromClipboardImage() {
        BufferedImage image = UIUtil.createImage(null, 10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        ImageWithInfo imageWithInfo = ImageUtils.getImageFromClipboard(new TestTransferable<>(image, DataFlavor.imageFlavor));
        Assert.assertNotNull(imageWithInfo.image);
        Assert.assertSame(image, imageWithInfo.image);
        Assert.assertNull(imageWithInfo.name);
    }

    @Test
    public void testGetImageFromClipboardFile() throws IOException {
        BufferedImage image = UIUtil.createImage(null, 10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        File tempFile = Files.createTempFile("image", "test_").toFile();
        FileUtils.writeByteArrayToFile(tempFile, bufferedImageToByteArray(image, "png"));

        ImageWithInfo imageWithInfo = ImageUtils.getImageFromClipboard(new TestTransferable<>(Lists.newArrayList(tempFile), DataFlavor.javaFileListFlavor));
        Assert.assertNotNull(imageWithInfo.image);
        Assert.assertEquals(10, imageWithInfo.image.getWidth(null));
        Assert.assertEquals(10, imageWithInfo.image.getHeight(null));
        Assert.assertEquals(tempFile.getName(), imageWithInfo.name);
    }

    private static byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return imageBytes;
    }

    class TestTransferable<T> implements Transferable {
        final public T value;
        final public DataFlavor dataFlavor;

        TestTransferable(T value, DataFlavor dataFlavor) {
            this.value = value;
            this.dataFlavor = dataFlavor;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {dataFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(this.dataFlavor);
        }

        @NotNull
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(this.dataFlavor)) {
                return this.value;
            }

            throw new IOException("Unsupported flavor: " + flavor);
        }
    }
}

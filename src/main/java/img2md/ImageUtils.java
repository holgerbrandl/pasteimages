package img2md;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ImageUtils {
    public static ImageWithInfo getImageFromClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        return getImageFromClipboard(transferable);
    }

    public static ImageWithInfo getImageFromClipboard(final Transferable transferable) {
        try {
            if (transferable == null) {
                return null;
            }

            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (files.size() > 0) {
                    File file = files.get(0);
                    return new ImageWithInfo(ImageIO.read(file), file.getName().split("\\.")[0]);
                }
            }

            if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                return new ImageWithInfo((Image) transferable.getTransferData(DataFlavor.imageFlavor), null);
            }

            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String string = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                // @TODO: Check the string is a URL?
                return null;
            }

            return null;
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public static BufferedImage scaleImage(BufferedImage sourceImage, int newWidth, int newHeight) {
        if (sourceImage == null) {
            return null;
        }

        if (newWidth == 0 || newHeight == 0) {
            return null;
        }

        AffineTransform at = AffineTransform.getScaleInstance((double) newWidth / sourceImage.getWidth(null),
                (double) newHeight / sourceImage.getHeight(null));

        //  http://nickyguides.digital-digest.com/bilinear-vs-bicubic.htm
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(sourceImage, null);
    }


    public static BufferedImage toBufferedImage(Image src) {
        if (src instanceof BufferedImage) {
            return (BufferedImage) src;
        }

        int w = src.getWidth(null);
        int h = src.getHeight(null);
        if (w < 0 || h < 0) {
            return null;
        }

        int type = BufferedImage.TYPE_INT_ARGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();

        return dest;
    }


    public static void save(BufferedImage image, File file, String format) {
        try {
            ImageIO.write(image, format, file);  // ignore returned boolean
        } catch (Throwable e) {
            throw new RuntimeException("Write error for " + file.getPath(), e);
//            System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
        }
    }


    /**
     * @return Could be <code>null</code> if the image could not be read from the file (because of whatever strange
     * reason).
     */
    public static BufferedImage loadImageFromFile(File cachedImageFile) {
        if (cachedImageFile == null || !cachedImageFile.isFile()) {
            return null;
        }

        try {
//            //            related to http://bugs.java.com/bugdatabase/view_bug.do;jsessionid=dc84943191e06dffffffffdf200f5210dd319?bug_id=6967419
            for (int i = 0; i < 3; i++) {
                BufferedImage read = null;
                try {
                    read = ImageIO.read(cachedImageFile);
                } catch (IndexOutOfBoundsException e) {
                    System.err.print("*");
                    System.err.println("could not read" + cachedImageFile);
                    continue;
                }

                if (i > 0) System.err.println();

                return read;
            }
//            return toBufferedImage(read);

//            return ImageIO.read(cachedImageFile);

        } catch (Throwable e) {
            System.err.println("deleting " + cachedImageFile);
            cachedImageFile.delete();
            return null;
        }

        return null;
    }


    public static BufferedImage loadImageFromURL(String imageURL) {
        try {
            return toBufferedImage(new ImageIcon(new URL(imageURL)).getImage());
        } catch (MalformedURLException ignored) {
        }

        return null;
    }


    /**
     * http://stackoverflow.com/questions/7603400/how-to-make-a-rounded-corner-image-in-java
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
//        g2.fillRect(0,0,256,256);


        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        output.setRGB(3, 3, 123);
        return output;
    }


    public static void main(String[] args) {
        new ImageIcon("/Users/holger/Library/Application Support/Movito/covercache/backdrop/2f87c882b52b30a6.png");
    }


    /**
     * http://stackoverflow.com/questions/464825/converting-transparent-gif-png-to-jpeg-using-java
     */
    public static BufferedImage removeAlpha(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
//Color.WHITE estes the background to white. You can use any other color
        g.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), Color.WHITE, null);
        g.dispose();

        return bufferedImage;
    }


    /**
     * http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
     */
    public static Image whiteToTransparent(BufferedImage image) {
//        ImageFilter filter = new RGBImageFilter() {
//            public final int filterRGB(int x, int y, int rgb) {
//                return (rgb << 8) & 0xFF000000;
//            }
//        };
//
//        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
//        return toBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public final int markerRGB = Color.WHITE.getRGB() | 0xFF000000;


            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);

    }


//    /**
//     * http://stackoverflow.com/questions/8039444/how-to-detect-corrupted-images-png-jpg-in-java
//     */
//    public static boolean isCorruptedBROKEN(final Path f) {
//        if(f.toString().endsWith(".jpg") || f.toString().endsWith(".jpeg")) {
//            try {
//
//                JPEGImageDecoder decoder = new JPEGImageDecoder(new FileImageSource(f.toFile().toString()), new FileInputStream(f.toFile()));
//                decoder.produceImage();
//
//            } catch (Throwable e) {
//                e.printStackTrace();
//                return true;
//            }
//
//        }else{
//            try {
//                new ImageIcon(f.toFile().toString());
//            } catch (Throwable e) {
//                e.printStackTrace();
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static class ImageAnalysisResult{
//
//        private boolean truncated;
//
//
//
//        public void setTruncated(boolean truncated) {
//            this.truncated = truncated;
//        }
//
//    }
//
//
//    public static boolean isCorruptedJpeg( Path file) {
//        if(file.toFile().length()<100) {
//            return true;
//        }
//
//        final ImageAnalysisResult result = new ImageAnalysisResult();
//
//         InputStream is = null;
//
//        try {
//
//            is = Files.newInputStream(file);
//
//            final ImageInputStream imageInputStream = ImageIO
//                    .createImageInputStream(is);
//            final Iterator<ImageReader> imageReaders = ImageIO
//                    .getImageReaders(imageInputStream);
//            if (!imageReaders.hasNext()) {
//                return true;
//            }
//            final ImageReader imageReader = imageReaders.next();
//            imageReader.setInput(imageInputStream);
//            final BufferedImage image = imageReader.read(0);
//            if (image == null) {
//                return true;
//            }
//            image.flush();
//            if (imageReader.getFormatName().equals("JPEG")) {
//                imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
//                final byte[] lastTwoBytes = new byte[2];
//                imageInputStream.read(lastTwoBytes);
//                if (lastTwoBytes[0] != 0xff && lastTwoBytes[1] != 0xd9) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } catch (final Throwable e) {
//                result.setTruncated(true);
//        } finally {
//            if(is!=null) try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//
//    @Test
//    public  void jpegCorruptionDetection() {
//        //http://stackoverflow.com/questions/8039444/how-to-detect-corrupted-images-png-jpg-in-java
//        final String baseDir = "/Users/holger/projects/miscmov/testdata/artwork/corruption/";
//
//
//        Assert.assertTrue(ImageUtils.isCorruptedBROKEN(new File(baseDir, "corrupted.jpeg").toPath()));
//        Assert.assertFalse(ImageUtils.isCorruptedBROKEN(new File(baseDir, "functional.jpeg").toPath()));
//    }


}

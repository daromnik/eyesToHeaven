package zebrains.team;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DetectEye {

    private BufferedImage originImage;
    private BufferedImage processingImage;
    private ByteArrayInputStream byteArrayInputStream;
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private Mat originMat;
    private final String classifierPath = "haarcascades/haarcascade_eye.xml";
    private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    public DetectEye() {
        OpenCV.loadLocally();
        faceCascade = new CascadeClassifier();
        absoluteFaceSize = 0;
        init();
    }

    private void init() {

        URL resource = classloader.getResource("images/pic3.jpg");
        if (resource != null) {
            File file = new File(resource.getFile());
            if (file.exists()) {
                System.out.println("Есть картинка");
                try {
                    originImage = ImageIO.read(file);

                    byte[] pixels = ((DataBufferByte) originImage.getRaster().getDataBuffer()).getData();
                    originMat = new Mat(originImage.getHeight(), originImage.getWidth(), CvType.CV_8UC3);
                    originMat.put(0, 0, pixels);

                    detect(originMat);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Нет картинки");
            }
        }
    }

    public ByteArrayInputStream getOriginImageStream() {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", originMat , buffer);
        return new ByteArrayInputStream(buffer.toArray());
    }

    private void detect(Mat originMat)
    {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(originMat, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height)
        if (this.absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
            {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        loadClassifier(classifierPath);

//        this.faceCascade.detectMultiScale(
//                grayFrame,
//                faces,
//                1.1,
//                2,
//                0 | Objdetect.CASCADE_SCALE_IMAGE,
//                new Size(this.absoluteFaceSize, this.absoluteFaceSize),
//                new Size()
//        );

        this.faceCascade.detectMultiScale(
                grayFrame,
                faces
        );

        // each rectangle in faces is a face
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(originMat, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }
    }

    private void loadClassifier(String classifierPath)
    {
        String resource = classloader.getResource(classifierPath).getPath();
        this.faceCascade.load(resource);
    }

}

package component.beans.filter;

import component.beans.dataobj.ImgDTO;
import component.beans.util.BeanMethods;
import component.beans.util.CacheHelper;
import component.beans.util.SetterHelper;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MedianFilter implements BeanMethods {

    private PropertyChangeSupport mPcs = new PropertyChangeSupport(this);
    private CacheHelper<ImgDTO> cacheHelper = new CacheHelper<>();
    private int ksize = 19;

    public MedianFilter() {
        System.out.println("Constructor: MedianFilter in Class: MedianFilter");
        SetterHelper.initOpenCV();
    }


    private ImgDTO process() {
        ImgDTO entity = cacheHelper.getCache();
        if (entity != null) {
            entity = entity.cloneDTO();
            Mat dst = new Mat();
            Imgproc.medianBlur(entity.getMat(), dst, ksize);
            entity.setMat(dst);
        }
        return entity;
    }

    @Override
    public void update() {
        System.out.println("Method: update in Class: MedianFilter");
        ImgDTO process = process();
        mPcs.firePropertyChange("medianNew", null, process);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mPcs.removePropertyChangeListener(listener);
    }

    public int getKsize() {
        return ksize;
    }

    public void setKsize(int ksize) {
        SetterHelper.notNeg(ksize, () -> {
            this.ksize = ksize;
            update();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Method: propertyChange in Class: MedianFilter");
        SetterHelper.ifNullableClass(evt.getNewValue(), ImgDTO.class, () -> {
            mPcs.firePropertyChange("medianNew", null, null);
        }, () -> {
            cacheHelper.setCache((ImgDTO) evt.getNewValue(), ImgDTO::cloneDTO);
            update();
        });
    }
}

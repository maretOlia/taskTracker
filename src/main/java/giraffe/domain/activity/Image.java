package giraffe.domain.activity;

import giraffe.domain.GiraffeEntity;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class Image extends GiraffeEntity {

    @Column(name = "path", nullable = false)
    private String path;


    public Image(String path) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        if (!super.equals(o)) return false;

        Image image = (Image) o;

        return path.equals(image.path);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}

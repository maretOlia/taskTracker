package giraffe.domain.activity;

import giraffe.domain.GiraffeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class Image extends GiraffeEntity<Image> {

    @Column(name = "path", nullable = false)
    private String path;


    public Image() { }

    @Override
    public Image self() {
        return this;
    }

    public String getPath() {
        return path;
    }

    public Image setPath(String path) {
        this.path = path;
        return self();
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

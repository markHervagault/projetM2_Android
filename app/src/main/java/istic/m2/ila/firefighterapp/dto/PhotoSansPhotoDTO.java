package istic.m2.ila.firefighterapp.dto;

/**
 * Created by markh on 02/05/2018.
 */
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * A DTO for the Photo entity.
 */
public class PhotoSansPhotoDTO implements Comparable<PhotoSansPhotoDTO> {

    private Long id;

    private Date dateHeure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoSansPhotoDTO photoDTO = (PhotoSansPhotoDTO) o;
        if(photoDTO.getId() == null || getId() == null) {
            return false;
        }
        return getId()==photoDTO.getId();
    }

    @Override
    public int compareTo(PhotoSansPhotoDTO o) {
        return o.getDateHeure().compareTo(getDateHeure());
    }
}
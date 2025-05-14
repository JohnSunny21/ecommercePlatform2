package com.devtale.ecommerceplatform2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Lob
    private byte[] image;

    private String downlaodUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="product_id")
    @ToString.Exclude
    private Product product;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", downlaodUrl='" + downlaodUrl + '\'' +
                ", product=" + product +
                ", imageSize=" + (image != null ? image.length : 0) + " bytes" +
                '}';
    }
}

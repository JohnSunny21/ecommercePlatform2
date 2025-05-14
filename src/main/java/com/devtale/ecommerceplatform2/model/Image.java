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
    private String fileName; // Ensures fileName is always present

    @Column(nullable = false)
    private String fileType; // Ensures fileType is always present

    /**
     * - Blob is database-specific and requires extra handling.
     * - byte[] is easier to work with in Java applications.
     * - Works well with Spring Boot REST APIs.
     */
    @Lob
    private byte[] image; // Changed from Blob to byte[] for better compatibility.

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

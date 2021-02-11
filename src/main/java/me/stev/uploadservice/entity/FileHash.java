package me.stev.uploadservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "file_hashes")
public class FileHash {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "hash")
    private String hash;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

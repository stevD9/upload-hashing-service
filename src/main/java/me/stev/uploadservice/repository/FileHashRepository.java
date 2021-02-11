package me.stev.uploadservice.repository;

import me.stev.uploadservice.entity.FileHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileHashRepository extends JpaRepository<FileHash, Integer> {
}

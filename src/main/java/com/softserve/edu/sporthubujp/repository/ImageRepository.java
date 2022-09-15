package com.softserve.edu.sporthubujp.repository;

import com.softserve.edu.sporthubujp.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
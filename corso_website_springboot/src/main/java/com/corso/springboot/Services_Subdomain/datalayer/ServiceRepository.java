package com.corso.springboot.Services_Subdomain.datalayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {

}

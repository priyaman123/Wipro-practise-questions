package com.music.admin.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.music.admin.entity.Admin;
import com.music.admin.repository.AdminRepository;

@Component
public class AdminDataInitializer implements CommandLineRunner {
    private final AdminRepository repo;
    public AdminDataInitializer(AdminRepository r){this.repo=r;}

    @Override
    public void run(String... args){
        if(repo.count()==0){
            Admin a=new Admin();
            a.setUsername("admin");
            a.setPassword("admin");
            repo.save(a);
        }
    }
}

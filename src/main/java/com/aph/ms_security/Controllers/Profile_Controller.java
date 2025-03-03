package com.aph.ms_security.Controllers;

import com.aph.ms_security.Models.Profile;
import com.aph.ms_security.Repositories.Profile_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profiles")
public class Profile_Controller {

    @Autowired
    private Profile_Repository theProfileRepository;

    @GetMapping("")
    public List<Profile> find() {
        return this.theProfileRepository.findAll();
    }

    @GetMapping("{id}")
    public Profile findById(@PathVariable String id) {
        return this.theProfileRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Profile create(@RequestBody Profile newProfile) {
        return this.theProfileRepository.save(newProfile);
    }

    @PutMapping("{id}")
    public Profile update(@PathVariable String id, @RequestBody Profile newProfile) {
        Profile actualProfile = this.theProfileRepository.findById(id).orElse(null);
        if (actualProfile != null) {
            actualProfile.setPhone(newProfile.getPhone());
            actualProfile.setPhoto(newProfile.getPhoto());
            return this.theProfileRepository.save(actualProfile);
        } else {
            return null;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Profile theProfile = this.theProfileRepository.findById(id).orElse(null);
        if (theProfile != null) {
            this.theProfileRepository.delete(theProfile);
        }
    }
}
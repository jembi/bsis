package service;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateGeneratorService {
    
    public Date generateDate() {
        return new Date();
    }

}

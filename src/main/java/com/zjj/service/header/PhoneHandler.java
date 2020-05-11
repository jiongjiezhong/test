package com.zjj.service.header;

import org.springframework.stereotype.Service;

@Service
public class PhoneHandler extends AbstractHandler {

    @Override
    public void saveEntity(String type) {
        System.out.println("into phoneHandler");
    }
}

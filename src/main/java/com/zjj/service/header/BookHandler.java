package com.zjj.service.header;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookHandler extends AbstractHandler {


    @Override
    public void saveEntity(String type) {
        System.out.println("into BookHandler");
    }
}

package com.rswy.getopenid.service.serviceImpl;

import com.rswy.getopenid.domain.entry.RulEntry;
import com.rswy.getopenid.mapper.RulMapper;
import com.rswy.getopenid.service.RulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RulServiceImpl implements RulService {
    @Autowired
    private RulMapper rulMapper;

    @Override
    public List<RulEntry> listAllRul(){
        return rulMapper.listAllRul();
    }

    @Override
    public RulEntry findRul(String rulKey){
        return rulMapper.findRul(rulKey);
    }
}

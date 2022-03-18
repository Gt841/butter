package com.rswy.getopenid.service;

import com.rswy.getopenid.domain.entry.RulEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RulService {

    List<RulEntry> listAllRul();

    RulEntry findRul(String rulKey);

}

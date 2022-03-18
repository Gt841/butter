package com.rswy.getopenid.mapper;

import com.rswy.getopenid.domain.entry.RulEntry;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RulMapper {

    List<RulEntry> listAllRul();

    RulEntry findRul(@Param("rul_key") String rulKey);
}

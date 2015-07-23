package com.mhe.bundle.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mhe.bundle.domain.ConfgurationDataInfo;

public interface ConfigurationRepository extends MongoRepository<ConfgurationDataInfo, String> {

}

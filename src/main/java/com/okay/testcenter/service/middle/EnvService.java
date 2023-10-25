package com.okay.testcenter.service.middle;

import com.okay.testcenter.domain.Env;

import java.util.List;

public interface EnvService {

    public List<Env> findEnvList();

    public Env findEnvByName(String name);
     Env findEnvById(int id);
}

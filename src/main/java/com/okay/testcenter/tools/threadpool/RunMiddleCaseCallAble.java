package com.okay.testcenter.tools.threadpool;

import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

import static com.okay.testcenter.runner.RunnerFactory.build;

/**
 * @author zhou
 * @date 2020/12/30
 */
public class RunMiddleCaseCallAble implements Callable<ResponseSampler> {

    private final static Logger logger = LoggerFactory.getLogger(RunMiddleCaseCallAble.class);
    private RequestSampler request = new RequestSampler();


    public RunMiddleCaseCallAble(RequestSampler requestSampler){
        this.request = requestSampler;
    }

    @Override
    public ResponseSampler call()  {

        return  build(request);

    }
}

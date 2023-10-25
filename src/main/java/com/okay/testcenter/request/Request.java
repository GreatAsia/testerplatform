package com.okay.testcenter.request;

import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;

public interface Request {

    ResponseSampler post(RequestSampler requestSampler);

    ResponseSampler get(RequestSampler requestSampler);
}

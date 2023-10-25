package com.okay.testcenter.service.performance;

import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.report.PerformanceHistory;

import java.util.List;

public interface ReportService {

    public void insertReport(PerformanceHistory performanceHistory);
    void insertReports(List<PerformanceHistory> performanceHistoryList);

    public void updateReport(PerformanceHistory performanceHistory);

    public PerformanceHistory findReport(int id);

    public void deleteReport(int id);

    public List<PerformanceHistory> findReportByRunId(int id);

    public Integer getLastRunId();

    public List<PerformanceHistory> findRunIdList();

    public PageInfo findRunIdList(int currentPage, int pageSize);

    public PageInfo findSerialnoList(int currentPage, int pageSize);

    public PageInfo findReportBySerialno(int currentPage, int pageSize, String serialno);

    public void deleteReportBySerialno(String serialno);

    public List<PerformanceHistory> sreachSerialno(String serialno);

    public int getTotal();

}
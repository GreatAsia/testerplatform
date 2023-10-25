package com.okay.testcenter.impl.performance;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.report.PerformanceHistory;
import com.okay.testcenter.mapper.performance.ReportMapper;
import com.okay.testcenter.service.performance.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;



@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    @Resource
    ReportMapper reportMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertReport(PerformanceHistory performanceHistory) {
        reportMapper.insertReport(performanceHistory);
    }

    @Override
    public void insertReports(List<PerformanceHistory> performanceHistoryList) {
        reportMapper.insertReports(performanceHistoryList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReport(PerformanceHistory performanceHistory) {
        reportMapper.updateReport(performanceHistory);
    }

    @Override
    public PerformanceHistory findReport(int id) {
        return reportMapper.findReport(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(int id) {
        reportMapper.deleteReport(id);
    }

    @Override
    public List<PerformanceHistory> findReportByRunId(int runid) {
        return reportMapper.findReportByRunId(runid);
    }


    @Override
    public Integer getLastRunId() {
        return reportMapper.getLastRunId();
    }

    @Override
    public List<PerformanceHistory> findRunIdList() {
        return reportMapper.findRunIdList();
    }


    @Override
    public PageInfo findRunIdList(int currentPage, int pageSize) {
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(currentPage, pageSize);

        List<PerformanceHistory> performanceHistoryList = reportMapper.findRunIdList();
        PageInfo pageInfo = new PageInfo(performanceHistoryList);
        return pageInfo;
    }

    @Override
    public PageInfo findSerialnoList(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<PerformanceHistory> serialnoList = reportMapper.findSerialnoList();
        PageInfo pageInfo = new PageInfo(serialnoList);
        return pageInfo;
    }

    @Override
    public PageInfo findReportBySerialno(int currentPage, int pageSize, String serialno) {
        PageHelper.startPage(currentPage, pageSize);
        List<PerformanceHistory> serialnoList = reportMapper.findReportBySerialno(serialno);
        PageInfo pageInfo = new PageInfo(serialnoList);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReportBySerialno(String serialno) {
        reportMapper.deleteReportBySerialno(serialno);
    }

    @Override
    public List<PerformanceHistory> sreachSerialno(String serialno) {
        return reportMapper.sreachSerialno(serialno);
    }

    @Override
    public int getTotal() {
        return reportMapper.getTotal();
    }

}

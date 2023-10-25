var project_id = 1;
var env_id = 1;
var currentPage = 1;
var totalPage = 1;


$(function () {
    $("#selectModelName").val(1);
    $("#selectAddress").val(1);
})



function switchPageInfo() {
    currentPage = 1;

    updatePageData();

}


// 首页
function homePage() {

    if (currentPage == 1) {
        return
    }
    currentPage = 1;
    updatePageData();

}


// 末页
function lastPage() {

    totalPage = parseInt($("#totalPage").text().replace("共", "").replace("页", ""));
    if (currentPage == totalPage) {
        return
    }

    currentPage = totalPage;
    updatePageData();

}

// 下一页
function nextPage() {

    totalPage = parseInt($("#totalPage").text().replace("共", "").replace("页", ""));
    currentPage = currentPage + 1;

    if ((currentPage > totalPage)) {
        currentPage = currentPage - 1;
        return;
    }

    updatePageData();

}

// 上一页
function previousPage() {

    currentPage = currentPage - 1;
    if (currentPage <= 0) {
        currentPage = currentPage + 1;
        return;
    }
    updatePageData();

}


// 更新分页的数据
function updatePageData() {

    project_id = $('#selectModelName').val();
    env_id = $('#selectAddress').val();

    var url = "/middle/report/getlist?env_id=" + env_id + "&project_id=" + project_id + "&currentPage=" + currentPage + "&pageSize=10";

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            $("#tbody").html(content(data.list));
            totalPage = data.pages;

            if (data.list.length == 0) {
                $("#currentPage").text("第0页");
            } else {
                $("#currentPage").text("第" + currentPage + "页");
            }
            $("#totalPage").text("共" + totalPage + "页");
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });

}

function content(report) {

    var contents = "";

    for (var i = 0; i < report.length; i++) {

        var result = "";
        if (report[i].result == "FAIL") {

            result = "<td style=\"font-weight:bold;color:red\"> " + report[i].result + "</td>\n";
        } else {

            result = "<td style=\"font-weight:bold;color:green\"> " + report[i].result + "</td>\n";
        }

        contents += "<tr><td>" + report[i].id + "</td>\n" +
            "        <td>" + report[i].projectName + "</td>\n" +
            "        <td>" + report[i].env + "</td>\n" +
            "        <td>" + report[i].startTime + "</td>\n" +
            "        <td>" + report[i].totalCase + "</td>\n" +
            "        <td>" + report[i].passCase + "</td>\n" +
            "        <td>" + report[i].failCase + "</td>\n" +
            result +
            "        <td>\n" +
            "        <a href=\"/middle/report/detail/" + report[i].id + "\">查看报告</a>\n" +
            "        </td></tr>\n";

    }

    return contents;


}
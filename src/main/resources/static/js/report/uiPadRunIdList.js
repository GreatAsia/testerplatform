var delObj;
var currentPage = 1;
var totalPage = 1;


//给确认删除模态框传值
function showDeleteModal(obj) {
    delObj = obj;
    var id = $(obj).attr("data-id");
    $("#deleteID").val(id);
    $("#deleteModal").modal({
        backdrop: 'static',
        keyboard: false
    });

}

//删除模块
function deleteInfo() {


    var deleteId = $('#deleteID').val();
    var SAVEDATA = [];
    var requestData = {
        "id": deleteId,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/uiPad/report/delete",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        timeout: 60000,
        success: function (data) {

            if (data.code == 200) {
                //隐藏模态框，删除对应的行
                $("#deleteModal").modal('hide');
                $(delObj).parents("tr").remove()
            } else {
                alert("删除失败：" + data);
            }

        },
        error: function (e) {
            alert("删除失败：" + e);
        }
    });


};


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


    var url = "/uiPad/report/getlist?currentPage=" + currentPage + "&pageSize=10";

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
        if (report[i].failDevice > 0) {

            result = "<td style=\"font-weight:bold;color:red\"> " + report[i].passRate + "</td>\n";
        } else {

            result = "<td style=\"font-weight:bold;color:green\"> " + report[i].passRate + "</td>\n";
        }

        contents += "<tr><td>" + report[i].id + "</td>\n" +
            "        <td>" + report[i].totalDevice + "</td>\n" +
            "        <td>" + report[i].passDevice + "</td>\n" +
            "        <td>" + report[i].failDevice + "</td>\n" +
            "        <td>" + report[i].errorDevice + "</td>\n" +
            "        <td>" + report[i].elapsedTime + "</td>\n" +
            "        <td>" + report[i].startTime + "</td>\n" +
            result +
            "<td>\n" +
            "     <a href=/uiPad/report/info/" + report[i].id + "  class=\"btn btn-sm btn-info\">" + "报告" + "</a>\n" +
            "     <a href=/uiPad/report/serialnolist/" + report[i].id + "  class=\"btn btn-sm btn-info\">" + "设备" + "</a>\n" +
            "     <a href=\"javascript:void(0)\" data-id=\" " + report[i].id + "\" class=\"btn btn-sm btn-danger\"\n" +
            "                     onclick=\"showDeleteModal(this)\">" + "删除" + "</a>\n" +
            "</td></tr>"

    }

    return contents;


}

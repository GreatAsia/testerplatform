var runObj;
var url;

//给运行case模态框传值
function runCaseModal(obj) {
    runObj = obj;
    var name = $(obj).attr("data-name");
    url = "http://okay-qaclient.xk12.cn/api/runUiTest?caseName=" + name;

    $("#respose").val("运行中...");
    //显示模态框
    $("#runCase").modal({
        backdrop: 'static',
        keyboard: false
    });

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        async: true,
        timeout: 10800000,
        success: function (data) {

            var json = JSON.stringify(data, null, 4);
            $("#respose").val(json);
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e, null, 4));

        }

    });
}


var currentPage = 1;
var totalPage = 1;


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

    if (currentPage > totalPage) {
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


    var url = "/uiRom/case/getRunList?currentPage=" + currentPage + "&pageSize=20";

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

function content(uicase) {

    var contents = "";

    for (var i = 0; i < uicase.length; i++) {

        contents += "<tr> " +
            "<td><input type=\"checkbox\" name=\"imgVo\" value= \'" + uicase[i].caseName + "\'>  </td>" +
            "<td>" + uicase[i].id + "</td>\n" +
            "                    <td>" + uicase[i].caseContent + "</td>\n" +
            "                    <td>" + uicase[i].caseName + "</td>\n" +
            "                    <td>" + uicase[i].caseDesc + "</td>\n" +
            "                    <td>\n" +
            "                        <a href=\"javascript:void(0)\"\n" +
            "                           data-name=\'" + uicase[i].caseName + "\'\n" +
            "                           class=\"btn btn-sm btn-info\"\n" +
            "                           onclick=\"runCaseModal(this)\">运行</a>\n" +
            "                    </td></tr>"

    }

    return contents;


}


function search() {

    var caseName = $("#searchName").val().trim();

    if (caseName == null || caseName == "" || caseName == undefined) {
        alert("请输入用例名称");
        return
    }

    var url = "/uiRom/findCaseByName?caseName=" + caseName;

    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: "application/json",
        timeout: 60000,
        success: function (data) {

            $("#tbody").html(content(data));

            currentPage = 1;
            totalPage = 1;
            $("#currentPage").text("第1页");
            $("#totalPage").text("共1页");
        },
        error: function (e) {
            alert("fail:" + JSON.stringify(e));
        }
    });


}

document.onkeydown = function (e) {
    var ev = document.all ? window.event : e;
    if (ev.keyCode == 13) {
        search();
    }
}
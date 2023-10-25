var currentPage = 1;
var totalPage = 1;


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


    var url = "/monitor/online/getlist?&currentPage=" + currentPage + "&pageSize=10";

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

function content(alarm) {

    var contents = "";

    for (var i = 0; i < alarm.length; i++) {

        contents += "<tr><td>" + alarm[i].id + "</td>\n" +
            "                    <td>" + alarm[i].taskId + "</td>\n" +
            "                    <td>" + alarm[i].serviceName + "</td>\n" +
            "                    <td>" + alarm[i].errorType + "</td>\n" +
            "                    <td>" + alarm[i].time + "</td>\n" +
            "</tr>"

    }

    return contents;


}


function exportHistoryData() {

    var weekTime = $.trim($('#weeks').find("option:selected").val());
    window.location.href = "/monitor/online/export?day=" + weekTime;


}
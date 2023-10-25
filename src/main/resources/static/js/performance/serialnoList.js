var delObj;

//给确认删除模态框传值
function showDeleteInterface(obj) {
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
        "serialno": deleteId,
    };
    SAVEDATA.push(requestData);

    $.ajax({
        type: "POST",
        url: "/performance/pad/serialno/delete",
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


function search() {

    var serialnoName = $("#searchName").val().trim();

    if (serialnoName == null || serialnoName == "" || serialnoName == undefined) {
        alert("请输入序列号");
        return
    }
    window.location.href = "/performance/pad/serialnoInfo/" + serialnoName;


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


    var url = "/performance/pad/serialno/getlist?currentPage=" + currentPage + "&pageSize=10";

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

function content(serialnoList) {

    var contents = "";

    for (var i = 0; i < serialnoList.length; i++) {


        contents += "<tr><td>" + serialnoList[i].serialno + "</td>\n" +
            "        <td>\n" +
            "        <a href=\"/performance/pad/serialnoInfo/" + serialnoList[i].serialno + "\">运行记录</a>" +
            "        </td>" +
            "       <td>\n" +
            "        <a href=\"javascript:void(0)\" data-id=" + serialnoList[i].serialno + " class=\"btn btn-sm btn-danger\"\n" +
            "                           onclick=\"showDeleteInterface(this)\">删除</a>\n" +
            "        </td></tr>"

    }

    return contents;


}



